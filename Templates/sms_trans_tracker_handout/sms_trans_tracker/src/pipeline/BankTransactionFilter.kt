package com.check.bank.balance.banking.tool.viewModelModules

import android.content.Context
import com.check.bank.balance.banking.tool.model.BankSMSModell
import com.check.bank.balance.banking.tool.smsmodel.BankSenderResolver
import com.check.bank.balance.banking.tool.utils.log

suspend fun bankTransactionFilter(
    context: Context, body: String, address: String?, date: String,
    messageId: String, typeId: String, threadId: Long,
    cardNoCache: MutableCollection<String>, bodyFrom: String
): BankSMSModell? {
    return bankTransactionFilterMl(
        context, body, address, date, messageId, typeId, threadId, cardNoCache
    )
}
