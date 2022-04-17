package co.ke.mshirika.mshirika_app.ui.create.new_client

import android.os.Bundle
import android.view.View
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentNewClientGeneralBinding
import co.ke.mshirika.mshirika_app.ui.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui.create.SimpleStep

class GeneralFragment :
    MshirikaFragment<FragmentNewClientGeneralBinding>(R.layout.fragment_new_client_general),
    SimpleStep {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setupViewsWithCalendars()
        lifecycleScope.launchWhenCreated {
        }
    }

    private fun FragmentNewClientGeneralBinding.setupViewsWithCalendars() {
        dobLo.setEndIconOnClickListener {

        }

        submitDateLo.setEndIconOnClickListener {

        }
    }

    fun onNextPressed() {

    }

    private suspend fun FragmentNewClientGeneralBinding.onSubmit() {
        //get all the text and push it to the view_model
        //create client object
    }

    override fun onSelected() {
        with(requireParentFragment() as MainFragment) {
            this.binding.apply {
                goToPrevious.isEnabled = false
                if (!goToNext.isEnabled)
                    goToNext.isEnabled = true
            }
        }
    }
}