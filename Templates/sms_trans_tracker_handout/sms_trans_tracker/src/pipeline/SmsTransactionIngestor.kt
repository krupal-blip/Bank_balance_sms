package com.check.bank.balance.banking.tool.viewModelModules

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.provider.Telephony
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.room.util.getColumnIndexOrThrow
import com.check.bank.balance.banking.tool.BuildConfig
import com.check.bank.balance.banking.tool.database.BankDataBase
import com.check.bank.balance.banking.tool.finance.FinanceProjection
import com.check.bank.balance.banking.tool.kpdo.KpdoEngine
import com.check.bank.balance.banking.tool.kpdo.SmsInput
import com.check.bank.balance.banking.tool.model.BankSMSModell
import com.check.bank.balance.banking.tool.utils.clearExtensionCaches
import com.check.bank.balance.banking.tool.utils.log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import kotlin.time.Duration.Companion.milliseconds

class SmsTransactionIngestor(
    context: Context,
    private val database: BankDataBase = BankDataBase.Companion.getInstance(context.applicationContext)
) {
    private val appContext = context.applicationContext
    private val cardNoCache = ArrayList<String>()

    // Single-flight: overlapping first-sync calls (onResume + initDate both trigger)
    // each saw an empty DB and double-inserted the whole inbox.
    suspend fun importInbox(limit: Int = UNLIMITED_INBOX_LIMIT): ImportResult =
        importInboxMutex.withLock {
        withContext(Dispatchers.IO) {
            if (!canReadSmsInbox()) {
                return@withContext ImportResult(hasNoTransactions = true)
            }
            "SmsTransactionIngestor importInbox cal".log()

            val insertedTransactions = mutableListOf<BankSMSModell>()
            val sortOrder = if (limit > 0) "${Telephony.Sms.DATE} DESC LIMIT $limit" else "${Telephony.Sms.DATE} DESC"
            val cursor = try {
                appContext.contentResolver.query(
                    Telephony.Sms.CONTENT_URI,
                    SMS_PROJECTION,
                    null,
                    null,
                    sortOrder
                )
            } catch (t: Throwable) {
                "SmsTransactionIngestor importInbox query failed: ${t.message}".log()
                return@withContext ImportResult(hasNoTransactions = true)
            } ?: return@withContext ImportResult(hasNoTransactions = true)

            var processedCount = 0
            cursor.use { smsCursor ->
                val bankDao = database.bankDao()
                // Batch inserts at the end, so DB check alone misses duplicates
                // within the same inbox pass (dual-SIM/duplicate delivery copies).
                val seenBodies = HashSet<String>()
                // Collect first, parse as ONE batch: KPDO's Pass 2 needs every message
                // together to pair the NACH notice+ledger halves of a single payment
                // (two different bodies, so neither unique(body,date) nor per-message
                // dedup can catch it) and to resolve account inheritance in
                // chronological rather than cursor order.
                val inputs = ArrayList<SmsInput>()
                while (smsCursor.moveToNext()) {
                    val sms = smsCursor.toSmsRecord()
                    if (!seenBodies.add(sms.body)) continue
                    if (bankDao.isMassageAlreadyExistInDatabase(sms.body)) continue

                    inputs.add(
                        SmsInput(
                            address = sms.address,
                            body = sms.body,
                            date = sms.date,
                            massageId = sms.messageId,
                            typeID = sms.typeId,
                            thread = sms.threadId,
                        )
                    )
                    processedCount++
                    if (processedCount % YIELD_EVERY_MESSAGE_COUNT == 0) {
                        delay(50.milliseconds)
                        "SmsTransactionIngestor importInbox collected=$processedCount".log()
                        yield()
                    }
                }

                val kpdo = KpdoEngine.process(appContext, inputs)
                insertedTransactions.addAll(kpdo.rows)
                "KPDO kept=${kpdo.rows.size} notTxn=${kpdo.notTransaction} " +
                        "paired=${kpdo.pairedAway} dups=${kpdo.duplicates}".log()
                if (BuildConfig.DEBUG) {
                    kpdo.balanceMismatches.forEach { "KPDO_BAL $it".log() }
                }

                if (insertedTransactions.isNotEmpty()) {
                    "SmsTransactionIngestor insertAllBank=${insertedTransactions.size} ".log()
                    bankDao.insertAllBank(insertedTransactions)
                    FinanceProjection.rebuild(appContext)
                }

                FinanceProjection.ensureBuilt(appContext)

                if (processedCount > CACHE_CLEAR_MESSAGE_COUNT) {
                    clearExtensionCaches()
                }

                "SmsTransactionIngestor ImportResult ${insertedTransactions.size}--${processedCount}".log()
                ImportResult(
                    insertedTransactions = insertedTransactions,
                    processedCount = processedCount,
                    hasNoTransactions = bankDao.getAll().isEmpty()
                )
            }
        }
        }

    suspend fun importIncoming(intent: Intent): ImportResult =
        withContext(Dispatchers.IO) {
            if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
                "SmsTransactionIngestor received unexpected action=${intent.action}".log()
                return@withContext ImportResult(hasNoTransactions = true)
            }

            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            if (messages.isEmpty()) {
                return@withContext ImportResult(hasNoTransactions = true)
            }

            val body = messages.joinToString(separator = "") { it.messageBody.orEmpty() }
            if (body.isBlank()) {
                return@withContext ImportResult(
                    hasNoTransactions = database.bankDao().getAll().isEmpty()
                )
            }

            val bankDao = database.bankDao()
            if (bankDao.isMassageAlreadyExistInDatabase(body)) {
                return@withContext ImportResult(hasNoTransactions = bankDao.getAll().isEmpty())
            }

            val firstMessage = messages.first()
            val sms = findStoredSms(firstMessage.originatingAddress, body)
                ?: SmsRecord(
                    address = firstMessage.originatingAddress,
                    body = body,
                    date = firstMessage.timestampMillis.toString(),
                    messageId = firstMessage.timestampMillis.toString(),
                    typeId = Telephony.Sms.MESSAGE_TYPE_INBOX.toString(),
                    threadId = 0L
                )

            val transaction = sms.toTransaction("Incoming") ?: return@withContext ImportResult(
                hasNoTransactions = bankDao.getAll().isEmpty()
            )

            bankDao.insertAllBank(listOf(transaction))
            FinanceProjection.rebuild(appContext)

            ImportResult(
                insertedTransactions = listOf(transaction),
                processedCount = 1,
                hasNoTransactions = false
            )
        }

    private fun canReadSmsInbox(): Boolean {
        val hasTelephony = appContext.packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)
        val hasReadSmsPermission = ContextCompat.checkSelfPermission(
            appContext,
            Manifest.permission.READ_SMS
        ) == PackageManager.PERMISSION_GRANTED
        return hasTelephony && hasReadSmsPermission
    }

    private fun findStoredSms(address: String?, body: String): SmsRecord? {
        if (ContextCompat.checkSelfPermission(appContext, Manifest.permission.READ_SMS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }

        val selectionParts = mutableListOf("${Telephony.Sms.BODY} = ?")
        val selectionArgs = mutableListOf(body)
        if (!address.isNullOrBlank()) {
            selectionParts.add("${Telephony.Sms.ADDRESS} = ?")
            selectionArgs.add(address)
        }

        return try {
            appContext.contentResolver.query(
                Telephony.Sms.CONTENT_URI,
                SMS_PROJECTION,
                selectionParts.joinToString(separator = " AND "),
                selectionArgs.toTypedArray(),
                "${Telephony.Sms.DATE} DESC LIMIT 1"
            )?.use { cursor ->
                if (cursor.moveToFirst()) cursor.toSmsRecord() else null
            }
        } catch (t: Throwable) {
            "SmsTransactionIngestor findStoredSms failed: ${t.message}".log()
            null
        }
    }

    private suspend fun SmsRecord.toTransaction(source: String): BankSMSModell? {
        return try {
            bankTransactionFilter(
                context = appContext,
                body = body,
                address = address,
                date = date,
                messageId = messageId,
                typeId = typeId,
                threadId = threadId,
                cardNoCache = cardNoCache,
                bodyFrom = "SmsTransactionIngestor $source --> $body"
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            "SmsTransactionIngestor toTransaction failed id=$messageId: ${e.message}".log()
            null
        }
    }

    private fun Cursor.toSmsRecord(): SmsRecord {
        return SmsRecord(
            address = getString(getColumnIndexOrThrow(Telephony.Sms.ADDRESS)) ?: "address",
            body = getString(getColumnIndexOrThrow(Telephony.Sms.BODY)) ?: "",
            date = getString(getColumnIndexOrThrow(Telephony.Sms.DATE)) ?: "0",
            messageId = getString(getColumnIndexOrThrow(Telephony.Sms._ID)) ?: "0",
            typeId = getString(getColumnIndexOrThrow(Telephony.Sms.TYPE)) ?: "0",
            threadId = getLong(getColumnIndexOrThrow(Telephony.Sms.THREAD_ID))
        )
    }

    data class ImportResult(
        val insertedTransactions: List<BankSMSModell> = emptyList(),
        val processedCount: Int = 0,
        val hasNoTransactions: Boolean
    )

    private data class SmsRecord(
        val address: String?,
        val body: String,
        val date: String,
        val messageId: String,
        val typeId: String,
        val threadId: Long
    )

    private companion object {
        private val importInboxMutex = Mutex()
        private const val UNLIMITED_INBOX_LIMIT = -1
        private const val YIELD_EVERY_MESSAGE_COUNT = 50
        private const val CACHE_CLEAR_MESSAGE_COUNT = 100

        private val SMS_PROJECTION = arrayOf(
            Telephony.Sms.ADDRESS,
            Telephony.Sms.BODY,
            Telephony.Sms.DATE,
            Telephony.Sms._ID,
            Telephony.Sms.TYPE,
            Telephony.Sms.THREAD_ID
        )
    }
}
