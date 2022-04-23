package co.ke.mshirika.mshirika_app.ui.create.new_client

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentNewClientGeneralBinding
import co.ke.mshirika.mshirika_app.ui.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui.OnImageSelectedListener
import co.ke.mshirika.mshirika_app.ui.PickImageDialog
import co.ke.mshirika.mshirika_app.ui.create.ViewerFragment
import co.ke.mshirika.mshirika_app.ui.util.DateUtil.fromShortDate
import co.ke.mshirika.mshirika_app.ui.util.EditableUtils.andd
import co.ke.mshirika.mshirika_app.ui.util.EditableUtils.attachNonVoidFields
import co.ke.mshirika.mshirika_app.ui.util.EditableUtils.emailAddressValidator
import co.ke.mshirika.mshirika_app.ui.util.EditableUtils.s
import co.ke.mshirika.mshirika_app.ui.util.EditableUtils.viewsOpeningTheDatePicker
import com.bumptech.glide.Glide

class GeneralFragment :
    MshirikaFragment<FragmentNewClientGeneralBinding>(R.layout.fragment_new_client_general),
    ViewerFragment, OnImageSelectedListener {

    private val viewModel: ViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            setupRequiredFields()
            setupViewsWithCalendars()
        }
    }

    fun pickImage() {
        PickImageDialog(requireContext(), this).show()
    }

    override fun onImageSelected(path: String) {
        Glide.with(requireContext())
            .load(path)
            .into(binding.createClientImage)
    }

    override fun onNextPressed() {
        binding.submit()
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

    private fun FragmentNewClientGeneralBinding.submit() {
        viewModel.saveGeneralData(
            GeneralData(
                firstNameRequired.s,
                middleName.s,
                lastName.s,
                dob.s.fromShortDate,
                memNo.s,
                mobileNo.s,
                (genderGroup.checkedRadioButtonId == male.id),
                isStaff.isChecked,
                clientType.s,
                clientClassification.s,
                submitDate.s.fromShortDate,
                nationalId.s,
                email.s,
                activationDate.s.fromShortDate
            )
        )
    }
}


data class GeneralData(
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val dob: Long,
    val memNo: String,
    val mobileNo: String,
    val gender: Boolean,
    val isStaff: Boolean,
    val clientType: String,
    val clientClassification: String,
    val submitDate: Long,
    val nationalId: String,
    val email: String,
    val activationDate: Long
) {
    fun name(): String {
        return firstName + middleName + lastName
    }
}