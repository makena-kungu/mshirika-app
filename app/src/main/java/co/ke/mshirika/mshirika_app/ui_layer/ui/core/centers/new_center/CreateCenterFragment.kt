package co.ke.mshirika.mshirika_app.ui_layer.ui.core.centers.new_center

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.CreateCenter
import co.ke.mshirika.mshirika_app.databinding.FragmentCreateNewCenterBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.fromShortDate
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.mshirikaDate
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.shortDate
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.attachNonVoidFields
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.text
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.OperationalUtils.openDatePicker
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.UIText
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateCenterFragment : MshirikaFragment<FragmentCreateNewCenterBinding>(
    R.layout.fragment_create_new_center
) {
    private val viewModel: ViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appBar.toolbarLarge.setup(
            titleId = R.string.create_center,
            menuId = R.menu.creation
        )
        binding.apply {
            centerSubmittedOnLayout.setEndIconOnClickListener {
                openDatePicker(R.string.submitted_on) {
                    centerSubmittedOn.setText(it.shortDate)
                }
            }
            submit.attachNonVoidFields(centerName, centerOffice)
        }
        viewModel.offices.collectLatestLifecycle {
            val offices = it.map { office -> office.nameDecorated }
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                offices
            )
            binding.centerOffice.setAdapter(adapter)
        }

        viewModel.errorState.collectLatestLifecycle { snack(it) }
        viewModel.successState.collectLatestLifecycle { snack(it, LENGTH_SHORT) }
    }

    private fun snack(text: UIText, duration: Int = LENGTH_LONG) {
        Snackbar.make(binding.root, text.text(requireContext()), duration).show()
    }

    fun submit() {
        binding.apply {
            val office = centerOffice.text()
            val isActive = centerIsActive.isChecked
            val submittedDate = centerSubmittedOn.text().fromShortDate.mshirikaDate

            val center = CreateCenter(
                name = centerName.text(),
                officeId = 0,
                active = isActive,
                submittedOnDate = submittedDate,
                externalId = centerExternalId.text()
            )
            viewModel.create(center, office)
        }
    }

    override fun onMenuItemClick(item: MenuItem?) = item?.let {
        if (it.itemId != R.id.refresh) return false
        viewModel.refresh()
        true
    } ?: super.onMenuItemClick(item)
}