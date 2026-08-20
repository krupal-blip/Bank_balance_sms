package com.check.bank.balance.banking.tool.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.check.bank.balance.banking.tool.model.BankSMSModell
import com.check.bank.balance.banking.tool.model.CardDetails
import com.check.bank.balance.banking.tool.model.PdfFileData
import kotlinx.coroutines.flow.Flow


@Dao
interface BankDao {

    @Insert
    suspend fun insertData(bankDataModel: BankDataModel)

    @Delete
    fun deleteData(bankDataModel: BankDataModel)

    @Query("DELETE FROM BankDataModel WHERE id = :id")
    suspend fun deleteBankById(id: Long)

    @Insert
    suspend fun insertPdfData(pdfFileData: PdfFileData)

    @Delete
    suspend fun deletePdfData(pdfFileModels: ArrayList<PdfFileData>)

    @Query("SELECT EXISTS (SELECT * FROM PdfFileData WHERE accountNo= :accountNom)")
    fun checkPDFFileIsExist(accountNom: String): Boolean

    @Query("SELECT * FROM PdfFileData WHERE accountNo = :account ORDER BY id DESC")
    fun getPdfFileByAccountNo(account: String): LiveData<List<PdfFileData>>

    @Query("SELECT * FROM BankDataModel ORDER BY id DESC")
    fun getUserBankData(): LiveData<List<BankDataModel>>

    @Query("SELECT EXISTS(SELECT * FROM BankDataModel WHERE mBankAccountNumber = :accountNo)")
    fun isRowIsExist(accountNo: String): Boolean

    @Query("SELECT mBankAccountNumber FROM BankDataModel WHERE id = :id")
    fun getSingleAccountNoByID(id: Int): String?

    @Query(
        "UPDATE BankDataModel SET mUserName = :mUserName1, " +
                "mBankAccountNumber = :mBankAccountNumber1, " +
                "mAccountType = :mAccountType," +
                "mBankName = :mBankName1, " +
                "mBranchCode = :mBrachCode1, " +
                "mCustomerId = :mCustomerId1, " +
                "mIfscCode = :mIfscCode1, " +
                "mState = :mState1, " +
                "mICRCode = :mICRCode1, " +
                "mDistrict = :mDistrict1, " +
                "mBranch = :mBranch1, " +
                "logoCode = :mLogocode, " +
                "mBankAddress = :mBankAddress1 WHERE id = :id"
    )
    fun updateData(
        id: Int,
        mUserName1: String,
        mBankAccountNumber1: String,
        mAccountType: String,
        mBankName1: String,
        mBrachCode1: String,
        mCustomerId1: String,
        mIfscCode1: String,
        mState1: String,
        mICRCode1: String,
        mDistrict1: String,
        mBranch1: String,
        mBankAddress1: String,
        mLogocode: String
    )

    @Query("UPDATE BankSMSModell SET avlBal= :newBalance WHERE id= :id")
    fun updateBalance(id: Int, newBalance: String)

//    @Insert
//    suspend fun insertBankTransaction(bankSMSModell: BankSMSModell)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllBank(bankSMSModell: List<BankSMSModell>)

    @Query("SELECT * FROM BankSMSModell ORDER BY date DESC")
    fun getAllBankTransactions(): LiveData<List<BankSMSModell>>

    @Query("SELECT * FROM BankSMSModell")
    fun getAll(): List<BankSMSModell>

    @Query("DELETE FROM BankSMSModell")
    fun nukeTable()

    @Query("SELECT EXISTS(SELECT 1 FROM BankSMSModell WHERE accountNumber = :accountNumber)")
    fun isTransactionAccountNoIsExist(accountNumber: String): Boolean

    @Query("SELECT MAX(CAST(massageId AS INTEGER)) FROM BankSMSModell")
    suspend fun getMaxMassageId(): Int?

    @Query("SELECT EXISTS(SELECT 1 FROM BankSMSModell WHERE body = :body)")
    fun isMassageAlreadyExistInDatabase(body: String): Boolean

    @Query("SELECT * FROM BankSMSModell WHERE accountNumber = :accountNumber ORDER BY id DESC")
    suspend fun getAllTransactionByAccountNumber(accountNumber: String): List<BankSMSModell>

    // No transactiontype filter -- 'Other' includes real transactions the parser couldn't
    // confidently tag DEBIT/CREDIT (e.g. some ATM-withdrawal wording), not just balance-only
    // anchor SMS. Callers already drop blank/zero-amount rows (renderTransactions' amount > 0
    // filter), which is what actually excludes pure balance-update SMS from the list.
    @Query("SELECT * FROM BankSMSModell WHERE accountNumber = :accountNumber ORDER BY date DESC")
    suspend fun getAllTransactionByAccountNo(accountNumber: String): List<BankSMSModell>

    @Query("SELECT * FROM BankSMSModell WHERE accountNumber = :accountNumber ORDER BY date DESC")
    fun getDatelist(accountNumber: String): List<BankSMSModell>

    @Query("SELECT * FROM BankSMSModell WHERE transactiontype = :transactiontype AND accountNumber = :accountNumber ORDER BY date DESC")
    fun getTransactionByType(transactiontype: String, accountNumber: String): List<BankSMSModell>

    // No transactiontype filter -- see getAllTransactionByAccountNo above for why.
    @Query("SELECT * FROM BankSMSModell WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getDateRange(startDate: Long, endDate: Long): List<BankSMSModell?>?

    @Query("SELECT body FROM BankSMSModell")
    suspend fun getAllBodyValuesFromDatabase(): List<String>

    @Query("DELETE FROM BankSMSModell WHERE body IN (:bodies)")
    suspend fun deleteBodyIfTransactionIsNotValid(bodies: List<String>)

    @Query("DELETE FROM BankSMSModell WHERE id IN (:ids)")
    suspend fun deleteTransactionsByIds(ids: List<Int>)

    @Update
    suspend fun updateTransactions(rows: List<BankSMSModell>)

    @Insert
    suspend fun insertCard(card: CardDetails)

    @Delete
    suspend fun deleteCard(card: CardDetails)

    @Query("SELECT COUNT(*) FROM BankSMSModell")
    suspend fun getTransactionCount(): Int

    @Query("DELETE FROM CardDetails WHERE id = :id")
    suspend fun deleteCardById(id: Long)

    @Query("SELECT * FROM CardDetails ORDER BY id DESC")
    fun getAllCards(): Flow<List<CardDetails>>

    @Query("SELECT EXISTS(SELECT 1 FROM CardDetails)")
    fun checkCardList(): Boolean

    @Query("SELECT * FROM CardDetails WHERE cardCategory = :category ORDER BY id DESC")
    suspend fun getListOfCardsByCategory(category: String): List<CardDetails>

    @Query("UPDATE CardDetails SET bankName = :bankName, cardNumber = :cardNumber, cardHolderName = :cardHolderName, expireDate = :expireDate, cardCategory = :cardCategory, cardTypeCategory = :cardTypeCategory, cvv = :cvv, bgImage = :bgImage, typeImage = :typeImage, bgres = :bgRes,isLocked = :isLocked WHERE id = :id")
    suspend fun updateCard(
        id: Long,
        bankName: String,
        cardNumber: String,
        cardHolderName: String,
        expireDate: String,
        cardCategory: String,
        cardTypeCategory: String,
        cvv: String,
        bgImage: Int?,
        typeImage: Int?,
        isLocked: Boolean,
        bgRes:String
    )

}
