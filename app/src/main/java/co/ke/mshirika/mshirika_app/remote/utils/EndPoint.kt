package co.ke.mshirika.mshirika_app.remote.utils

import co.ke.mshirika.mshirika_app.remote.utils.EndPoint.Paths.CENTER_ID
import co.ke.mshirika.mshirika_app.remote.utils.EndPoint.Paths.CLIENT_ID
import co.ke.mshirika.mshirika_app.remote.utils.EndPoint.Paths.LOAN_ID

object EndPoint {
    //This class contains all the Constants for API End Points
    const val AUTHENTICATION = "authentication"

    const val CHARGES = "charges"
    const val CENTERS = "centers"
    const val CENTER = "$CENTERS/{$CENTER_ID}"
    const val CLIENTS = "clients"
    const val CLIENT = "$CLIENTS/{$CLIENT_ID}"
    const val CLIENT_ACCOUNTS = "$CLIENTS/{$CLIENT_ID}/accounts"
    const val COLLECTION_SHEET = "collectionsheet"
    const val CREATE_LOANS_ACCOUNTS = "loans"
    const val CREATE_LOANS_PRODUCTS = "loanproducts"
    const val CREATE_SAVINGS_ACCOUNTS = "savingsaccounts"
    const val CREATE_SAVINGS_PRODUCTS = "savingsproducts"

    const val DATATABLES = "datatables"
    const val DISBURSE = "disburse"
    const val DOCUMENTS = "documents"
    const val GROUPS = "groups"
    const val IDENTIFIERS = "identifiers"
    const val LOANS = "loans"
    const val LOANS_PATH = "${LOANS}/{$LOAN_ID}"
    const val LOANS_TRANSACTIONS = "${LOANS}/{$LOAN_ID}/transactions"

    const val MAKER_CHECKER = "makercheckers"
    const val NOTES = "notes"
    const val OFFICES = "offices"

    const val RUN_REPORTS = "runreports"
    const val RECURRING_ACCOUNTS = "recurringdepositaccounts"

    const val SAVINGS_ACCOUNTS = "savingsaccounts"
    const val SEARCH = "search"
    const val STAFF = "staff"
    const val SURVEYS = "surveys"

    const val PAYMENT_TYPES = "paymenttypes"

    object Paths {
        const val LOAN_ID = "{loanId}"
        const val CENTER_ID = "{centerId}"
        const val CLIENT_ID = "{clientId}"
    }

}