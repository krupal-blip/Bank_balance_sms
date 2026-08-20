package com.check.bank.balance.banking.tool.helper

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.check.bank.balance.banking.tool.R
import com.check.bank.balance.banking.tool.activity.NewTransactionShowAActivity
import com.check.bank.balance.banking.tool.constants.AdsInitManager
import com.check.bank.balance.banking.tool.constants.Constant.CHANNEL_ID
import com.check.bank.balance.banking.tool.constants.Constant.CHANNEL_NAME
import com.check.bank.balance.banking.tool.constants.Constant.CREDIT
import com.check.bank.balance.banking.tool.constants.Constant.DEBIT
import com.check.bank.balance.banking.tool.constants.Constant.isNewTransactionAdded
import com.check.bank.balance.banking.tool.constants.Constant.VIA_CARD
import com.check.bank.balance.banking.tool.constants.Constant.isOutOfTheApp
import com.check.bank.balance.banking.tool.model.BankSMSModell
import com.check.bank.balance.banking.tool.utils.baseShared
import com.check.bank.balance.banking.tool.utils.canRequestBankAds
import com.check.bank.balance.banking.tool.utils.formatCurrency
import com.check.bank.balance.banking.tool.utils.log
import com.check.bank.balance.banking.tool.viewModelModules.SmsTransactionIngestor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration.Companion.milliseconds

class MessageListener : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent == null || intent.action != EXPECTED_ACTION) {
            "MessageListener: Received intent with unexpected action".log()
            return
        }

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withTimeoutOrNull(8_000L.milliseconds) {
                    val result = SmsTransactionIngestor(context).importIncoming(intent)
                    val newMsgList = result.insertedTransactions
                    if (newMsgList.isNotEmpty()) {
                        "MessageListener inserted=${newMsgList.size}".log()
                        isNewTransactionAdded = true

                        if (context.baseShared.transactionNotificationToggle) {
                            // Fire the SDK call here, still inside the goAsync() window, then
                            // wait (bounded) for it to actually be invoked before releasing --
                            // a detached coroutine outside this window can get killed with the
                            // process before AdsInitManager's internal 1500ms stagger elapses
                            // and MobileAds.initialize() ever runs (ads only started on tap).
                            (context.applicationContext as? Application)?.let { app ->
                                runCatching {
                                    if (app.canRequestBankAds()) AdsInitManager.init(app)
                                }
                            }

                            withContext(Dispatchers.Main) {
                                "MessageListener --> Notification Send".log()
                                sendNotification(context, newMsgList)
                            }

                            // 6.5s, not 3s: AdsInitManager's own 1500ms ANR-safety stagger (never
                            // remove that) + cold WebView bootstrap can outrun a short bound, so
                            // the old 3s often lost the race -- pendingResult.finish() released the
                            // process before MobileAds.initialize() was ever invoked, and the
                            // detached initScope coroutine got killed with it (ads only started on
                            // tap). Stays under the outer 8s withTimeoutOrNull with margin for the
                            // ingest + sendNotification work already spent by this point.
                            AdsInitManager.awaitInitCalled(timeoutMs = 6_500L)
                        }
                    }
                }
            } catch (t: Throwable) {
                "MessageListener failed: ${t.message}".log()
            } finally {
                pendingResult.finish()
            }
        }
    }

    @SuppressLint("ResourceAsColor", "RemoteViewLayout")
    private fun sendNotification(
        mCon: Context,
        newMsgList: List<BankSMSModell>,
    ) {
        val transaction = newMsgList.firstOrNull() ?: return

        val notificationManager = mCon.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        isOutOfTheApp = true

        // Channel creation is idempotent but not free (binder round-trip to NotificationManager
        // service). This runs on the background SMS-receive path, inside goAsync()'s budget --
        // create it once per process instead of on every message (blamed frame in ANR 754fee0b).
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationChannelReady.compareAndSet(false, true)) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(mCon, NewTransactionShowAActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("Bank", transaction.bankName)
            putExtra("Account", transaction.accountNumber)
            putExtra("balance", transaction.avlBal)
            putExtra("typeOf", transaction.typeOf)
            putExtra("fromNotification", true)
        }
        val pendingIntent = PendingIntent.getActivity(
            mCon,
            transaction.massageId.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val largeView = RemoteViews(mCon.packageName, R.layout.notification_large)
        val smallView = RemoteViews(mCon.packageName, R.layout.notification_small)

        val amount = transaction.amount
            .replace("₹ ", "")
            .toDoubleOrNull()
            ?.formatCurrency()
            ?: transaction.amount
        val accNo = transaction.accountNumber.replace("XX","")
        val cardLabel = mCon.getString(R.string.credit_card)
        val bankName = transaction.bankName.let { name ->
            if (transaction.typeOf == VIA_CARD && !name.contains(cardLabel, ignoreCase = true)) {
                "$name $cardLabel"
            } else {
                name
            }
        }

        largeView.apply {
            setTextViewText(R.id.tvHead, bankName)
            setTextViewText(R.id.tvAmt, amount)
            setTextViewText(R.id.tvDes, accNo)
            setTextViewText(R.id.tvMerc, transaction.merchantName)
        }

        smallView.apply{
            setTextViewText(R.id.tvHeadSmall, bankName)
            setTextViewText(R.id.tvAccSmall, accNo)
            setTextViewText(R.id.tvAmtSmall, amount)
        }

        when (transaction.transactiontype) {
            CREDIT -> {
                largeView.setImageViewResource(R.id.tvImage,R.drawable.ic_2)
                largeView.setTextColor(R.id.tvAmt, ContextCompat.getColor(mCon, R.color.Lime_Green))

                smallView.setTextColor(R.id.tvAmtSmall, ContextCompat.getColor(mCon, R.color.Lime_Green))
                smallView.setImageViewResource(R.id.tvImageSmall,R.drawable.ic_2)
            }
            DEBIT -> {
                largeView.setImageViewResource(R.id.tvImage,R.drawable.ic_1)
                largeView.setTextColor(R.id.tvAmt, ContextCompat.getColor(mCon, R.color.Love_Red))

                smallView.setTextColor(R.id.tvAmtSmall, ContextCompat.getColor(mCon, R.color.Love_Red))
                smallView.setImageViewResource(R.id.tvImageSmall,R.drawable.ic_1)
            }
            else -> {
                largeView.setTextViewText(R.id.tvAmt, "Balance is ${transaction.amount}")
                largeView.setTextViewText(R.id.tvDes, accNo)

                smallView.setTextViewText(R.id.tvAmtSmall, "Balance is ${transaction.amount}")
                smallView.setTextViewText(R.id.tvAccSmall, accNo)
            }
        }

        val NOTIFICATION_ID =
            transaction.massageId.toIntOrNull()
                ?: transaction.massageId.hashCode()

        val builder = NotificationCompat.Builder(mCon,CHANNEL_ID)
            .setSmallIcon(R.drawable.frame_1)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setContentText(": New")
            .setContentTitle(transaction.merchantName)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setContent(smallView)
            .setCustomBigContentView(largeView)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private companion object {
        private const val EXPECTED_ACTION = "android.provider.Telephony.SMS_RECEIVED"

        // Process-scoped: the channel only needs creating once per process lifetime.
        private val notificationChannelReady = java.util.concurrent.atomic.AtomicBoolean(false)
    }
}
