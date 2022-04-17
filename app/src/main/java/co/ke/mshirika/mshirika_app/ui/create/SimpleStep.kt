package co.ke.mshirika.mshirika_app.ui.create

import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError

interface SimpleStep : Step {
    override fun verifyStep(): VerificationError? {
        TODO("Not yet implemented")
    }

    override fun onSelected()

    override fun onError(error: VerificationError) {
        TODO("Not yet implemented")
    }
}