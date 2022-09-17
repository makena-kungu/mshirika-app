package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.core.loans.OfflineDetailedLoan
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.transactions.loans.OfflineRepayLoan
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.NewLoan
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.ConservativeLoanAccount
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.DetailedLoanAccount
import kotlinx.coroutines.flow.Flow
import org.intellij.lang.annotations.Language

@Dao
interface LoansDao {

    @Language("RoomSql")
    @Query("select * from loans_table order by id asc")
    fun clients(): PagingSource<Int, ConservativeLoanAccount>

    @Insert(onConflict = REPLACE)
    suspend fun insert(vararg loans: ConservativeLoanAccount)

    @Query("delete from loans_table")
    fun clear()

    @Insert(onConflict = REPLACE)
    suspend fun insert(loan: OfflineDetailedLoan)

    @Insert(onConflict = REPLACE)
    suspend fun insert(vararg loan: OfflineDetailedLoan)

    @Query("select loanAccount from detailed_loans_table where loanId = :loanId")
    suspend fun detailedLoanAccount(loanId: Int): DetailedLoanAccount?

    @Query("select * from loans_table where clientId like :clientId")
    fun loans(clientId: Int): ConservativeLoanAccount?

    @Query("select * from new_loan_table")
    fun createLoans(): Flow<List<NewLoan>>

    @Insert
    suspend fun insert(vararg newLoans: NewLoan)

    @Insert
    suspend fun insert(repayLoan: OfflineRepayLoan)

    @Query("select * from offline_repay_loan")
    @Language("RoomSql")
    suspend fun getRepayLoans(): List<OfflineRepayLoan>
}