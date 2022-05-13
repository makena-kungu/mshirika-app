package co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.today
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoanRepaymentFragmentTest:TestCase() {

    //creating a fragment for testing
    private lateinit var scenario: FragmentScenario<LoanRepaymentFragment>

    @Before
    fun setup() {
        scenario = launchFragmentInContainer(
            themeResId = R.style.Theme_MshirikaApp
        )
        //scenario.moveToState(Lifecycle.State.STARTED)
    }

    @Test
    fun testTheButtonActionIsPerformed() {
    }

    @Test
    fun testTheButtonIsVisibleWhenAllFieldsAreFilled() {
        scenario.moveToState(Lifecycle.State.STARTED)
        val repaymentAmount = "1000"
        val paymentType = "Bank Sl"
        val receipt = "KJFAS943${(0..100).random()}${('A'..'Z').random()}"
        val repaymentDate = today
        val repaymentBankDate = today

        onView(withId(R.id.repayment_amount)).perform(typeText(repaymentAmount))
        onView(withId(R.id.payment_type)).perform(typeText(paymentType))
        onView(withId(R.id.receipt)).perform(typeText(receipt))
        onView(withId(R.id.repayment_date)).perform(typeText(repaymentDate))

        onView(withId(R.id.make_repayment)).check(matches(isNotEnabled()))

        onView(withId(R.id.repayment_bank_date)).perform(typeText(repaymentBankDate))

        onView(withId(R.id.make_repayment)).check(matches(isEnabled()))
    }
}