package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.Converter
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.daos.*
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.core.clients.OfflineAccounts
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.core.clients.OfflineTransactions
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.core.clients.SyncClientsConverter
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.core.loans.LoansConverter
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.core.loans.OfflineDetailedLoan
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.transactions.clients_savings.*
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.transactions.clients_savings.Deposit
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.transactions.clients_savings.Deposit.DepositsConverter
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.transactions.loans.OfflineRepayLoan
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.transactions.loans.TransactionsLoanConverters
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.*
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.center.Center
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.center.CenterConverter
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.ClientConverter
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.Cliente
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.group.GroupConverter
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.group.Grupo
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.ConservativeLoanAccount
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.LoanConverter
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.client.ClientTemplateConverter

@Database(
    entities = [
        Center::class,
        Cliente::class,
        CreateClient::class,
        CreateGroup::class,
        NewLoan::class,
        Deposit::class,
        ConservativeLoanAccount::class,
        OfflineDetailedLoan::class,
        OfflineRepayLoan::class,
        Grupo::class,
        OfflineClientPaymentTemplate::class,
        OfflineTransactions::class,
        OfflineAccounts::class,
        OfflineCharge::class,
        OfflineTransferFundsTemplateWithToClients::class,
        OfflineTransferFundsTemplateWithToClientsAccounts::class,
        OfflineTransferFundsTemplate::class,
        OfflineDeposit::class,
        CreateCenter::class,
        TransferFunds::class
    ],
    version = 1
)
@TypeConverters(
    CenterConverter::class,
    ClientConverter::class,
    ClientTemplateConverter::class,
    Converter::class,
    DepositsConverter::class,
    GroupConverter::class,
    LoanConverter::class,
    LoansConverter::class,
    SavingsConverter::class,
    SyncClientsConverter::class,
    TransactionsLoanConverters::class,
)
abstract class SyncDatabase : RoomDatabase() {
    abstract fun clientDao(): ClientDao
    abstract fun groupDao(): GroupDao
    abstract fun centerDao(): CenterDao
    abstract fun loanDao(): LoansDao
    abstract fun searchDao(): SearchDao
}