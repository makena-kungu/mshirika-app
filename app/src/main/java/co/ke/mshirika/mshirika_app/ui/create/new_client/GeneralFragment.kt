package co.ke.mshirika.mshirika_app.ui.create.new_client

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentNewClientGeneralBinding
import co.ke.mshirika.mshirika_app.ui.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui.create.ViewerFragment
import co.ke.mshirika.mshirika_app.ui.create.new_client.ViewModel.Companion.KEY_ACTIVATION_DATE
import co.ke.mshirika.mshirika_app.ui.create.new_client.ViewModel.Companion.KEY_CLIENT_CLASSIFICATION
import co.ke.mshirika.mshirika_app.ui.create.new_client.ViewModel.Companion.KEY_CLIENT_TYPE
import co.ke.mshirika.mshirika_app.ui.create.new_client.ViewModel.Companion.KEY_DOB
import co.ke.mshirika.mshirika_app.ui.create.new_client.ViewModel.Companion.KEY_EMAIL
import co.ke.mshirika.mshirika_app.ui.create.new_client.ViewModel.Companion.KEY_FIRST_NAME
import co.ke.mshirika.mshirika_app.ui.create.new_client.ViewModel.Companion.KEY_GENDER_GROUP
import co.ke.mshirika.mshirika_app.ui.create.new_client.ViewModel.Companion.KEY_IS_STAFF
import co.ke.mshirika.mshirika_app.ui.create.new_client.ViewModel.Companion.KEY_LAST_NAME
import co.ke.mshirika.mshirika_app.ui.create.new_client.ViewModel.Companion.KEY_MEM_NO
import co.ke.mshirika.mshirika_app.ui.create.new_client.ViewModel.Companion.KEY_MIDDLE_NAME
import co.ke.mshirika.mshirika_app.ui.create.new_client.ViewModel.Companion.KEY_MOBILE_NO
import co.ke.mshirika.mshirika_app.ui.create.new_client.ViewModel.Companion.KEY_NATIONAL_ID
import co.ke.mshirika.mshirika_app.ui.create.new_client.ViewModel.Companion.KEY_SUBMIT_DATE
import co.ke.mshirika.mshirika_app.ui.util.DateUtil.fromShortDate
import co.ke.mshirika.mshirika_app.ui.util.EditableUtils.andd
import co.ke.mshirika.mshirika_app.ui.util.EditableUtils.attachNonVoidFields
import co.ke.mshirika.mshirika_app.ui.util.EditableUtils.emailAddressValidator
import co.ke.mshirika.mshirika_app.ui.util.EditableUtils.s
import co.ke.mshirika.mshirika_app.ui.util.EditableUtils.viewsOpeningTheDatePicker

class GeneralFragment :
    MshirikaFragment<FragmentNewClientGeneralBinding>(R.layout.fragment_new_client_general),
    ViewerFragment {

    private val viewModel: ViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            setupRequiredFields()
            setupViewsWithCalendars()
        }
        lifecycleScope.launchWhenCreated {
        }
    }

    override fun onNextPressed() {
        binding.apply {
            viewModel.saveGeneralDate(
                KEY_FIRST_NAME to firstNameRequired.s,
                KEY_MIDDLE_NAME to middleName.s,
                KEY_LAST_NAME to lastName.s,
                KEY_DOB to dob.s.fromShortDate,
                KEY_MEM_NO to memNo.s,
                KEY_MOBILE_NO to mobileNo.s,
                KEY_GENDER_GROUP to (genderGroup.checkedRadioButtonId == male.id),
                KEY_IS_STAFF to isStaff.isChecked,
                KEY_CLIENT_TYPE to clientType.s,
                KEY_CLIENT_CLASSIFICATION to clientClassification.s,
                KEY_SUBMIT_DATE to submitDate.s.fromShortDate,
                KEY_NATIONAL_ID to nationalId.s,
                KEY_EMAIL to email.s,
                KEY_ACTIVATION_DATE to activationDate.s.fromShortDate
            )
        }
    }

    private fun FragmentNewClientGeneralBinding.setupRequiredFields() {
        with(parentFragment as MainFragment) {
            binding.goToNext.attachNonVoidFields(
                firstNameRequired,
                lastName,
                activationDate
            )
        }
        email.emailAddressValidator()
    }

    private fun FragmentNewClientGeneralBinding.setupViewsWithCalendars() {
        viewsOpeningTheDatePicker(
            "date_of_birth" andd (dob to dobLo),
            "submitting_date" andd (submitDate to submitDateLo),
            "activation_date" andd (activationDate to activationDateLo)
        )
    }

    private suspend fun FragmentNewClientGeneralBinding.onSubmit() {
        //get all the text and push it to the view_model
        //create client object
    }
}