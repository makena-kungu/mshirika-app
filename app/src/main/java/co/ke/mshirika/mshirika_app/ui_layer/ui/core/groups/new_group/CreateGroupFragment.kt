package co.ke.mshirika.mshirika_app.ui_layer.ui.core.groups.new_group

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.CreateGroup
import co.ke.mshirika.mshirika_app.databinding.FragmentCreateNewGroupBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.fromShortDate
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.mshirikaDate
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.shortDate
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.attachNonVoidFields
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.text
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.OperationalUtils.openDatePicker
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.setAdapter
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CreateGroupFragment :
    MshirikaFragment<FragmentCreateNewGroupBinding>(R.layout.fragment_create_new_group) {
    private val viewModel: ViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragment = this
        binding.apply {
            submit.attachNonVoidFields(groupName, groupOffice)
            groupSubmittedOnLayout.setEndIconOnClickListener {
                openDatePicker(R.string.submitted_on) {
                    groupSubmittedOn.setText(it.shortDate)
                }
            }

            lifecycleScope.launchWhenCreated {
                setupOffices()
                setupSuccess()
            }
        }
    }

    private suspend fun FragmentCreateNewGroupBinding.setupOffices() {
        launch {
            val template = viewModel.offices()?.asIterable() ?: return@launch
            val offices = template.map { it.nameDecorated }
            groupOffice.setAdapter(offices)
        }
    }

    private suspend fun setupSuccess() {
        viewModel.groupCreated()
        viewModel.successState.collectLatest {
            Snackbar.make(binding.root, it.text(requireContext()), LENGTH_LONG).run {
                setAction(R.string.okay) { dismiss() }
                show()
            }
        }
    }

    fun submit() {
        binding.apply {
            groupName.text()
            groupOffice.text()
            groupStaff.text()
            val date = System.currentTimeMillis().mshirikaDate
            val subDate = groupSubmittedOn.text().fromShortDate

            val isActive = groupIsActive.isChecked
            viewModel.create(
                group = CreateGroup(
                    officeId = "",
                    name = groupName.text(),
                    externalId = groupExternalId.text(),
                    active = isActive,
                    activationDate = if (isActive) date else null,
                    submittedOnDate = subDate.mshirikaDate
                ),
                officeNameDeco = groupOffice.text()
            )
        }
    }
}