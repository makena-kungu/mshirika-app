package co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.new_client.content

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.Cliente
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.client.EditClientTemplate
import co.ke.mshirika.mshirika_app.databinding.FragmentNewClientGeneralBinding
import co.ke.mshirika.mshirika_app.ui_layer.OnImageSelectedListener
import co.ke.mshirika.mshirika_app.ui_layer.PickImageDialog
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.new_client.CreateClientViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.new_client.MainFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.new_client.Task
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.create.ViewerFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.fromShortDate
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.shortDate
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.today
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.andd
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.attachNonVoidFields
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.emailAddressValidator
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.s
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.viewsOpeningTheDatePicker
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.setAdapter
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.parcelize.Parcelize
import java.util.concurrent.atomic.AtomicReference

@AndroidEntryPoint
class GeneralFragment : MshirikaFragment<FragmentNewClientGeneralBinding>(
    R.layout.fragment_new_client_general
), ViewerFragment, OnImageSelectedListener {

    private val viewModel: CreateClientViewModel by viewModels({ requireParentFragment() })
    private val client: Cliente by lazy {
        val args = requireArguments()
        args.getParcelable(KEY_CLIENTE)!!
    }
    private val _template = AtomicReference<EditClientTemplate>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.updateTitle(R.string.general_data)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            setupFields()
            setupRequiredFields()
            setupViewsWithCalendars()
            resume()

            // TODO remove these statements
            firstNameRequired.setText("First")
            lastName.setText("Last")
            middleName.setText("Middle")
            dob.setText(today)
            memNo.setText("${(8923..43445).random()}")
            mobileNo.setText("7${(10_000_000 until 100_000_000).random()}")
            clientType.setText("Member")
            clientClassification.setText("Catholic")

            nationalId.setText("${(235246..342956).random()}")
            email.setText("some@domain.com")

            viewModel.task.observe(viewLifecycleOwner) {
                if (it is Task.Create) return@observe
                launch {
                    loadTemplate()
                }
            }
        }
    }

    private suspend fun FragmentNewClientGeneralBinding.loadTemplate() {
        memNo.isVisible = false
        val template = viewModel.getEditTemplate(client.id) ?: return
        _template.set(template)

        firstNameRequired.setText(template.firstname)
        middleName.setText(template.middlename)
        lastName.setText(template.lastname)

        dob.setText(client.dateOfBirth?.shortDate)
        memNo.setText(client.memberNumber)
        mobileNo.setText(client.mobileNo)
        val isMale = template.gender.name.equals("male", false)
        genderGroup.check(if (isMale) male.id else female.id)
        isStaff.isChecked = client.isStaff
        savingsProduct.isVisible = false
        clientType.setText(client.clientType.name)
        clientClassification.setText(client.clientClassification?.name)
        nationalId.setText(template.nationalId)
    }

    private fun FragmentNewClientGeneralBinding.resume() {
        viewModel.generalData?.let {
            firstNameRequired.setText(it.firstName)
            middleName.setText(it.middleName)
            lastName.setText(it.lastName)
            dob.setText(it.dob.shortDate)
            memNo.setText(it.memNo)
            mobileNo.setText(it.mobileNo)
            if (it.gender) male.isChecked = true
            else female.isChecked = true
            isStaff.isChecked = it.isStaff
            savingsProduct.setText(it.savingsAccount)
            clientType.setText(it.clientType)
            clientClassification.setText(it.clientClassification)
            nationalId.setText(it.nationalId)
            email.setText(it.email)
        }
    }

    private fun FragmentNewClientGeneralBinding.setupFields() {
        launch {
            val (_, typeOptions, classificationOptions, savingsOptions) = viewModel.template()
                ?: return@launch

            val types = typeOptions.map { it.name }
            val classifications = classificationOptions.map { it.name }
            val savings = savingsOptions.map { it.name }
            clientType.setAdapter(types)
            clientClassification.setAdapter(classifications)
            savingsProduct.setAdapter(savings)
        }
    }

    private fun FragmentNewClientGeneralBinding.setupRequiredFields() {
        viewModel.task.observe(viewLifecycleOwner) {
            if (it is Task.Edit) return@observe

            with(requireParentFragment() as MainFragment) {
                binding.goToNext.attachNonVoidFields(
                    firstNameRequired,
                    lastName,
                    savingsProduct,
                    clientType,
                    clientClassification
                )
            }
            email.emailAddressValidator()
        }
    }

    private fun FragmentNewClientGeneralBinding.setupViewsWithCalendars() {
        viewsOpeningTheDatePicker(
            R.string.date_of_birth andd (dob to dobLo)
        )
    }

    fun pickImage() {
        PickImageDialog(requireContext(), this).show()
    }

    override fun onImageSelected(path: String) {
        Glide.with(requireContext())
            .load(path)
            .into(binding.createClientImage)
    }

    override fun onNextPressed(): Boolean {
        binding.submit()
        return true
    }

    private fun FragmentNewClientGeneralBinding.submit(edit: Boolean = false) {
        viewModel.saveGeneralData(
            data = GeneralData(
                firstName = firstNameRequired.s,
                middleName = middleName.s,
                lastName = lastName.s,
                dob = dob.s.fromShortDate,
                memNo = memNo.s,
                mobileNo = mobileNo.s,
                gender = (genderGroup.checkedRadioButtonId == male.id),
                isStaff = isStaff.isChecked,
                savingsAccount = savingsProduct.s,
                clientType = clientType.s,
                clientClassification = clientClassification.s,
                submitDate = System.currentTimeMillis(),
                nationalId = nationalId.s,
                email = email.s,
                isEdit = edit//,
                //activationDate.s.fromShortDate
            ),
            template = _template.get()
        )
    }

    fun edit() {
        binding.submit(edit = true)
    }

    companion object {
        const val KEY_CLIENTE = "CLIENTE"
    }
}

@Parcelize
data class GeneralData(
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val dob: Long,
    val memNo: String,
    val mobileNo: String,
    val gender: Boolean,
    val isStaff: Boolean,
    val savingsAccount: String,
    val clientType: String,
    val clientClassification: String,
    val submitDate: Long,
    val nationalId: String,
    val email: String,
    val activationDate: Long = System.currentTimeMillis(),
    val isEdit: Boolean = false
) : Parcelable {
    val name: String
        get() = "$firstName $middleName $lastName"
}