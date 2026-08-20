package com.check.bank.balance.banking.tool.viewModelModules

import android.content.Context
import com.check.bank.balance.banking.tool.database.BankDataBase
import com.check.bank.balance.banking.tool.finance.FinanceProjection
import com.check.bank.balance.banking.tool.utils.baseShared
import com.check.bank.balance.banking.tool.utils.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// One-time upgrade sweep: rows inserted before TransactionRowValidator existed
// (mandate creation notices, amount-parsed-as-account phantoms, blank accounts)
// survive the dedup migration and keep drawing ghost account cards.
object PhantomRowCleanup {

    suspend fun runOnce(context: Context) = withContext(Dispatchers.IO) {
        val shared = context.applicationContext.baseShared
        if (shared.phantomRowCleanupDone) return@withContext
        runCatching {
            val dao = BankDataBase.getInstance(context.applicationContext).bankDao()
            val badIds = dao.getAll()
                .filter { TransactionRowValidator.isInvalidRow(it.accountNumber, it.amount, it.body) }
                .map { it.id }
            if (badIds.isNotEmpty()) {
                dao.deleteTransactionsByIds(badIds)
                FinanceProjection.rebuild(context.applicationContext)
                "PhantomRowCleanup removed ${badIds.size} rows".log()
            }
            shared.phantomRowCleanupDone = true
        }.onFailure {
            "PhantomRowCleanup failed: ${it.message}".log()
        }
    }
}
