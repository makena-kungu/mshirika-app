package co.ke.mshirika.mshirika_app.ui_layer.ui.core.groups.new_group

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.CreateGroup
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.databinding.FragmentCreateNewGroupBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.fromShortDate
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.mshirikaDate
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.shortDate
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.attachNonVoidFields
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.clear
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.text
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.OperationalUtils.openDatePicker
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.setAdapter
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.snackL
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateGroupFragment : MshirikaFragment<FragmentCreateNewGroupBinding>(
    R.layout.fragment_create_new_group
) {
    private val viewModel: CreateGroupViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragment = this
        binding.apply {
            appBar.toolbarLarge.setup(R.string.create_group)

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
        viewModel.successState.observe(viewLifecycleOwner) {
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
            launch {
                val create = viewModel.create(
                    group = CreateGroup(
                        officeId = "",
                        name = groupName.text(),
                        externalId = groupExternalId.text(),
                        active = isActive,
                        activationDate = if (isActive) date else null,
                        submittedOnDate = subDate.mshirikaDate,
                        id = 0
                    ),
                    officeNameDeco = groupOffice.text()
                )

                when (create) {
                    is Outcome.Error -> {
                        root.snackL(R.string.error_creating_group)
                    }
                    is Outcome.Success -> {
                        root.snackL(R.string.group_created)
                        clear(groupName, groupOffice, groupStaff, groupSubmittedOn, groupExternalId)
                    }
                    else -> {}
                }
            }
        }
    }
}