package co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.new_loan.content

import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.NewLoan
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.repositories.FakeLoansRepo
import co.ke.mshirika.mshirika_app.data_layer.repositories.Util.template2
import co.ke.mshirika.mshirika_app.data_layer.repositories.loans.LoansRepo
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.new_loan.content.NewLoanDetailsFragment.Companion.Details
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.new_loan.content.NewLoanTermsFragment.Companion.Terms
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.mshirikaDate
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CreateLoanUseCaseTest {
    private lateinit var loansRepo: LoansRepo
    private lateinit var useCase: CreateLoanUseCase

    @Before
    fun setup() {
        loansRepo = FakeLoansRepo()
        useCase = CreateLoanUseCase(loansRepo)
    }

    @Test
    fun `test that the created json matches with the expected result`() {
        runBlocking {
            val today = System.currentTimeMillis().mshirikaDate
            val outcome = useCase(
                savingsAccountId = 555334,
                details = Details(
                    product = 213,
                    officer = 957,
                    purpose = 163,
                    date = today,
                    disbursementDate = today,
                    formNumber = "090835"
                ),
                terms = Terms(
                    principal = 23,
                    loanTerm = 15,
                    frequency = 2,
                    repaidEvery = 1,
                    repaymentFrequency = "Months"
                ),
                template2 = template2,
                charges = listOf(
                    NewLoan.Charge(
                        1280,
                        100
                    )
                )
            )

            Assert.assertTrue(outcome is Outcome.Empty)
        }
    }
}