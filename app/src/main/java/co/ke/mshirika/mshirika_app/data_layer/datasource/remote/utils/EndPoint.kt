package co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils

import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.EndPoint.Paths.CENTER_ID
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.EndPoint.Paths.CHARGE_ID
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.EndPoint.Paths.CLIENT_ID
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.EndPoint.Paths.GROUP_ID
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.EndPoint.Paths.LOAN_ID
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.EndPoint.Paths.SAVINGS_ACCOUNT_ID

object EndPoint {
    //This class contains all the Constants for API End Points
    const val AUTHENTICATION = "authentication"
    const val SAVINGS_ACCOUNTS = "savingsaccounts"

    const val ACCOUNTS_TRANSFER = "accounttransfers"
    const val ACCOUNTS_TRANSFER_TEMPLATE = "$ACCOUNTS_TRANSFER/template"

    const val CHARGES = "charges"
    const val CENTERS = "centers"
    const val CENTER = "$CENTERS/{$CENTER_ID}"
    const val CLIENTS = "clients"
    const val CLIENTS_EDIT_TEMPLATE = "clients/{$CLIENT_ID}?template=true&staffInSelectedOfficeOnly=true"
    const val CLIENTS_FAMILY = "$CLIENTS/{$CLIENT_ID}/familymembers"
    const val CLIENTS_TEMPLATE = "$CLIENTS/template"
    const val CHARGE_TEMPLATE = "savingsaccounts/{$SAVINGS_ACCOUNT_ID}/charges/template"
    const val CREATE_CHARGES = "savingsaccounts/{$SAVINGS_ACCOUNT_ID}/charges"
    const val CHARGE_TEMPLATE2 = "charges/{$CHARGE_ID}?template=true"
    const val CLIENT_DEPOSIT = "$SAVINGS_ACCOUNTS/{$SAVINGS_ACCOUNT_ID}/transactions"
    const val CLIENT_DEPOSIT_CHARGE = "$SAVINGS_ACCOUNTS/{$SAVINGS_ACCOUNT_ID}/charges" +
            "/{$CHARGE_ID}?command=paycharge"
    const val CLIENT_PAYMENT = "clientpayment/{$CLIENT_ID}"
    const val CLIENT = "$CLIENTS/CLIENT_ID"
    const val CLIENT_ACCOUNTS = "$CLIENTS/{$CLIENT_ID}/accounts?fields=loanAccounts,savingsAccounts"
    const val CLIENT_ACCOUNTS_DETAILED = "savingsaccounts/{$SAVINGS_ACCOUNT_ID}?associations=all&" +
            "checkOfficeHierarchy=false"
    const val COLLECTION_SHEET = "collectionsheet"
    const val CREATE_LOANS_ACCOUNTS = "loans"
    const val CREATE_LOANS_PRODUCTS = "loanproducts"
    const val CREATE_SAVINGS_ACCOUNTS = "savingsaccounts"
    const val CREATE_SAVINGS_PRODUCTS = "savingsproducts"

    const val DATATABLES = "datatables"
    const val DATATABLES_BENEFICIARY = "$DATATABLES/NextofKin/{$CLIENT_ID}"
    const val DATATABLES_NEXT_OF_KIN = "$DATATABLES/Next of Kin/{$CLIENT_ID}"
    const val DISBURSE = "disburse"
    const val DOCUMENTS = "documents"
    const val GROUPS = "groups"
    const val GROUP = "$GROUPS/{$GROUP_ID}"
    const val GROUP_DETAILED = "groups/{$GROUP_ID}?associations=all"

    const val CLIENTS_STATS_DAY = "runreports/ClientTrendsByDay?R_officeId=1&genericResultSet=false"
    const val CLIENTS_STATS_WEEK = "runreports/ClientTrendsByWeek?R_officeId=1&genericResultSet=false"
    const val CLIENTS_STATS_MONTH = "runreports/ClientTrendsByMonth?R_officeId=1&genericResultSet=false"
    const val LOANS_STATS_DAY = "runreports/LoanTrendsByDay?R_officeId=1&genericResultSet=false"
    const val LOANS_STATS_WEEK = "runreports/LoanTrendsByWeek?R_officeId=1&genericResultSet=false"
    const val LOANS_STATS_MONTH = "runreports/LoanTrendsByMonth?R_officeId=1&genericResultSet=false"

    const val IDENTIFIERS = "identifiers"
    const val LOANS = "loans"
    const val LOANS_GUARANTORS = "$LOANS/{$LOAN_ID}/guarantors"
    const val LOANS_GUARANTORS_TEMPLATE = "$LOANS/{$LOAN_ID}/guarantors/template"
    const val LOANS_TEMPLATE = "$LOANS/template"
    const val LOANS_PATH = "$LOANS/{$LOAN_ID}"
    const val LOANS_TRANSACTIONS = "$LOANS/{$LOAN_ID}/transactions"

    const val MAKER_CHECKER = "makercheckers"
    const val NOTES = "notes"
    const val OFFICES = "offices"

    const val RUN_REPORTS = "runreports"
    const val RECURRING_ACCOUNTS = "recurringdepositaccounts"

    const val SEARCH = "search"
    const val SMS = "sms"
    const val STAFF = "staff"
    const val SURVEYS = "surveys"

    const val PAYMENT_TYPES = "paymenttypes"

    object Paths {
        const val LOAN_ID = "loanId"
        const val CENTER_ID = "centerId"
        const val GROUP_ID = "groupId"
        const val CLIENT_ID = "clientId"
        const val SAVINGS_ACCOUNT_ID = "savingsAccountId"
        const val CHARGE_ID = "chargeId"
        const val TIME_VALUE = "time_value"
    }

}