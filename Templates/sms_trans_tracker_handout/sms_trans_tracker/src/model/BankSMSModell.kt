package com.check.bank.balance.banking.tool.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(indices = [Index(value = ["body", "date"], unique = true)])
data class BankSMSModell(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val bankName: String,
    val accountNumber: String,
    val amount: String,
    val avlBal: String,
    val merchantName : String,
    val body: String,
    val date: String,
    val transactiontype: String,
    val address: String,
    val logoCode : String,
    val massageId: String,
    val typeID: String,
    val thread: Long? = 0L,
    val typeOf: String
) : Serializable

data class SM(
    var bankSMSModell : BankSMSModell,
    var balance : String,
) : Serializable

