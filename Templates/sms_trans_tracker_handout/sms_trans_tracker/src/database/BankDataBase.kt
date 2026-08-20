package com.check.bank.balance.banking.tool.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.check.bank.balance.banking.tool.finance.AccountEntity
import com.check.bank.balance.banking.tool.finance.FinanceConverters
import com.check.bank.balance.banking.tool.finance.FinanceDao
import com.check.bank.balance.banking.tool.finance.InstrumentEntity
import com.check.bank.balance.banking.tool.finance.PostingEntity
import com.check.bank.balance.banking.tool.model.BankSMSModell
import com.check.bank.balance.banking.tool.model.CardDetails
import com.check.bank.balance.banking.tool.model.PdfFileData


@Database(
    entities = [
        BankDataModel::class,
        BankSMSModell::class,
        PdfFileData::class,
        CardDetails::class,
        AccountEntity::class,
        InstrumentEntity::class,
        PostingEntity::class,
    ],
    version = 14
)
@TypeConverters(FinanceConverters::class)
abstract class BankDataBase: RoomDatabase() {

    abstract fun bankDao(): BankDao

    abstract fun financeDao(): FinanceDao

    companion object {
        private const val DATABASE_NAME = "BankName"


        @Volatile
        private var bankDataBase: BankDataBase? = null


        fun getInstance(context: Context): BankDataBase {
            return bankDataBase ?: synchronized(this) {
                bankDataBase ?: buildDatabase(context.applicationContext).also { bankDataBase = it }
            }
        }

        private fun buildDatabase(context: Context): BankDataBase {
            return try {
                Room.databaseBuilder(context, BankDataBase::class.java, DATABASE_NAME)
                    .addMigrations(
                        MIGRATION_1_2,
                        MIGRATION_2_3,
                        MIGRATION_3_4,
                        MIGRATION_4_5,
                        MIGRATION_5_6,
                        MIGRATION_6_7,
                        MIGRATION_7_8,
                        MIGRATION_8_9,
                        MIGRATION_9_10,
                        MIGRATION_10_11,
                        MIGRATION_11_12,
                        MIGRATION_12_13,
                        MIGRATION_13_14
                    ).build().apply {
                        openHelper.writableDatabase
                    }
            } catch (e: Exception) {
                context.deleteDatabase(DATABASE_NAME)
                Room.databaseBuilder(context, BankDataBase::class.java, DATABASE_NAME)
                    .addMigrations(
                        MIGRATION_1_2,
                        MIGRATION_2_3,
                        MIGRATION_3_4,
                        MIGRATION_4_5,
                        MIGRATION_5_6,
                        MIGRATION_6_7,
                        MIGRATION_7_8,
                        MIGRATION_8_9,
                        MIGRATION_9_10,
                        MIGRATION_10_11,
                        MIGRATION_11_12,
                        MIGRATION_12_13,
                        MIGRATION_13_14
                    ).build()
            }
        }


        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE UserBankProfile ADD COLUMN logoCode TEXT NOT NULL DEFAULT 0")
            }
        }

        private val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE UserBankProfile ADD COLUMN isNothing INTEGER NOT NULL DEFAULT 0")
            }
        }

        private val MIGRATION_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS `BankSMSModell` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `bankName` TEXT NOT NULL, `accountNumber` TEXT NOT NULL, `amount` TEXT NOT NULL, `avlBal` TEXT NOT NULL, `merchantName` TEXT NOT NULL, `body` TEXT NOT NULL, `date` TEXT NOT NULL, `transactiontype` TEXT NOT NULL, `address` TEXT NOT NULL, `logoCode` TEXT NOT NULL,`massageId` TEXT NOT NULL, `typeID` TEXT NOT NULL, `thread` INTEGER)")
            }
        }

        private val MIGRATION_4_5: Migration = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE UserBankProfile ADD COLUMN mAccountType TEXT NOT NULL DEFAULT 'Saving Account'")
                db.execSQL("ALTER TABLE UserBankProfile ADD COLUMN mEmail TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE UserBankProfile ADD COLUMN mIdentity TEXT NOT NULL DEFAULT ''")
            }
        }

        private val MIGRATION_5_6: Migration = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DELETE FROM UserBankProfile")
            }
        }

        private val MIGRATION_6_7: Migration = object : Migration(6, 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DROP TABLE IF EXISTS UserBankProfile")
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `BankDataModel` (" +
                            "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "`mUserName` TEXT NOT NULL, " +
                            "`mBankAccountNumber` TEXT NOT NULL, " +
                            "`mAccountType` TEXT NOT NULL, " +
                            "`mBankName` TEXT NOT NULL, " +
                            "`mBranchCode` TEXT NOT NULL, " +
                            "`mCustomerId` TEXT NOT NULL, " +
                            "`mIfscCode` TEXT NOT NULL, " +
                            "`mState` TEXT NOT NULL, " +
                            "`mICRCode` TEXT NOT NULL, " +
                            "`mDistrict` TEXT NOT NULL, " +
                            "`mBranch` TEXT NOT NULL, " +
                            "`mBankAddress` TEXT NOT NULL, " +
                            "`logoCode` TEXT NOT NULL, " +
                            "`isNothing` INTEGER DEFAULT 0 NOT NULL)"
                )
            }
        }

        private val MIGRATION_7_8: Migration = object : Migration(7, 8) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `PdfFileData` (" +
                            "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                            "`accountNo` TEXT NOT NULL," +
                            "`pathOfPdf` TEXT NOT NULL," +
                            "`sizeOfPdf` TEXT NOT NULL," +
                            "`nameOfPdf` TEXT NOT NULL," +
                            "`thumbnailOfPdf` BLOB NOT NULL)"
                )
            }
        }

        private val MIGRATION_8_9: Migration = object : Migration(8, 9) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE `BankSMSModell` ADD COLUMN `typeOf` TEXT NOT NULL DEFAULT 'BANK'")
            }
        }

        private val MIGRATION_9_10: Migration = object : Migration(9, 10) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `CardDetails` (" +
                            "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                            "`bankName` TEXT NOT NULL," +
                            "`cardNumber` TEXT NOT NULL," +
                            "`cardHolderName` TEXT NOT NULL," +
                            "`expireDate` TEXT NOT NULL," +
                            "`cardCategory` TEXT NOT NULL," +
                            "`cardTypeCategory` TEXT NOT NULL," +
                            "`cvv` TEXT NOT NULL," +
                            "`bgImage` INTEGER NOT NULL," +
                            "`bgImageTag` TEXT NOT NULL," +
                            "`typeImage` INTEGER NOT NULL," +
                            "`typeImageTag` TEXT NOT NULL," +
                            "`isLocked` INTEGER DEFAULT 0 NOT NULL)"
                )
            }
        }
        // Dedup existing rows (duplicate-insert bug in SmsTransactionIngestor first sync),
        // then unique-index as permanent backstop; inserts use OnConflictStrategy.IGNORE.
        val MIGRATION_11_12 = object : Migration(11, 12) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "DELETE FROM `BankSMSModell` WHERE `id` NOT IN " +
                            "(SELECT MIN(`id`) FROM `BankSMSModell` GROUP BY `body`, `date`)"
                )
                db.execSQL(
                    "CREATE UNIQUE INDEX IF NOT EXISTS `index_BankSMSModell_body_date` " +
                            "ON `BankSMSModell` (`body`, `date`)"
                )
            }
        }

        val MIGRATION_13_14 = object : Migration(13, 14) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE `instruments` ADD COLUMN `linkEvidence` TEXT")
            }
        }

        // Finance domain model: accounts hold balances, instruments are access
        // methods pointing at an account, postings are one row per (txn, account).
        // Shadow tables only — BankSMSModell keeps its current role.
        val MIGRATION_12_13 = object : Migration(12, 13) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `accounts` (" +
                            "`id` TEXT NOT NULL, " +
                            "`bankName` TEXT NOT NULL, " +
                            "`kind` TEXT NOT NULL, " +
                            "`last4` TEXT NOT NULL, " +
                            "`logoCode` TEXT NOT NULL, " +
                            "`balanceMinor` INTEGER, " +
                            "`balanceAsOf` INTEGER, " +
                            "`creditLimitMinor` INTEGER, " +
                            "`availableLimitMinor` INTEGER, " +
                            "PRIMARY KEY(`id`))"
                )
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `instruments` (" +
                            "`id` TEXT NOT NULL, " +
                            "`bankName` TEXT NOT NULL, " +
                            "`last4` TEXT NOT NULL, " +
                            "`kind` TEXT NOT NULL, " +
                            "`accountId` TEXT, " +
                            "`linkTier` TEXT NOT NULL, " +
                            "PRIMARY KEY(`id`))"
                )
                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS `index_instruments_accountId` " +
                            "ON `instruments` (`accountId`)"
                )
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `postings` (" +
                            "`id` TEXT NOT NULL, " +
                            "`accountId` TEXT NOT NULL, " +
                            "`instrumentId` TEXT, " +
                            "`direction` TEXT NOT NULL, " +
                            "`amountMinor` INTEGER NOT NULL, " +
                            "`date` INTEGER NOT NULL, " +
                            "`merchant` TEXT NOT NULL, " +
                            "`reportedBalanceMinor` INTEGER, " +
                            "`messageId` TEXT NOT NULL, " +
                            "`body` TEXT NOT NULL, " +
                            "`eventId` TEXT NOT NULL, " +
                            "`inferred` INTEGER NOT NULL, " +
                            "PRIMARY KEY(`id`))"
                )
                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS `index_postings_accountId` " +
                            "ON `postings` (`accountId`)"
                )
                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS `index_postings_messageId` " +
                            "ON `postings` (`messageId`)"
                )
            }
        }

        val MIGRATION_10_11 = object : Migration(10, 11) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // 1) add new TEXT column with a safe default
                db.execSQL("ALTER TABLE `CardDetails` ADD COLUMN `bgres` TEXT NOT NULL DEFAULT 'card_type_1'")

                // 2) backfill names from old ints (your 1.1 mapping)
                db.execSQL(
                    """
            UPDATE `CardDetails`
            SET `bgres` = CASE `bgImage`
                WHEN 2131231034 THEN 'card_type_1'
                WHEN 2131231043 THEN 'card_type_2'
                WHEN 2131231044 THEN 'card_type_3'
                WHEN 2131231045 THEN 'card_type_4'
                WHEN 2131231046 THEN 'card_type_5'
                WHEN 2131231047 THEN 'card_type_6'
                WHEN 2131231048 THEN 'card_type_7'
                WHEN 2131231049 THEN 'card_type_8'
                WHEN 2131231050 THEN 'card_type_9'
                WHEN 2131231035 THEN 'card_type_10'
                WHEN 2131231036 THEN 'card_type_11'
                WHEN 2131231037 THEN 'card_type_12'
                WHEN 2131231038 THEN 'card_type_13'
                WHEN 2131231039 THEN 'card_type_14'
                WHEN 2131231040 THEN 'card_type_15'
                WHEN 2131231041 THEN 'card_type_16'
                WHEN 2131231042 THEN 'card_type_17'
                ELSE 'card_type_1' -- fallback if something unexpected is stored
            END
        """.trimIndent()
                )
            }
        }


    }

}
