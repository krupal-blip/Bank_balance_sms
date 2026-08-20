package com.check.bank.balance.banking.tool.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class BankDataModel(
    @PrimaryKey(autoGenerate = true)
    val id : Long,
    val mUserName : String,
    val mBankAccountNumber : String,
    val mAccountType: String,
    val mBankName : String,
    val mBranchCode : String,
    val mCustomerId : String,
    val mIfscCode : String,
    val mState : String,
    val mICRCode : String,
    val mDistrict : String,
    val mBranch : String,
    val mBankAddress : String,
    val logoCode : String,
    val isNothing : Boolean = false
) : Serializable
