package co.ke.mshirika.mshirika_app.ui_layer.ui.checker_inbox

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.checker_inbox.CheckerTask
import co.ke.mshirika.mshirika_app.databinding.FragmentCheckerInboxBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.checker_inbox.CheckerInboxViewModel.Companion.Filter
import co.ke.mshirika.mshirika_app.ui_layer.ui.checker_inbox.CheckerInboxViewModel.Companion.Filter.*
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.OperationalUtils.openDatePicker
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.snackI
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.snackL
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckerFragment : MshirikaFragment<FragmentCheckerInboxBinding>(
    R.layout.fragment_checker_inbox
), CheckerTaskListAdapter.OnItemClickListener {
    private val viewModel by viewModels<CheckerInboxViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = CheckerTaskListAdapter()
        adapter.setOnItemClickListener(this)
        binding.toolbar.setup(R.string.checker_inbox)
        binding.tasks.adapter = adapter
        viewModel.success.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.loading.observe(viewLifecycleOwner) {
binding.tasksLoading.isVisible = it
        }

        binding.apply {
            actionChip.setOnClickListener { v ->
                Log.d(TAG, "onViewCreated: action clip clicked ${viewModel.actions.value}")
                viewModel.actions.value?.let { showMenu(v, Action, it) }
            }

            entityChip.setOnClickListener {
                viewModel.entities.value?.let { list -> showMenu(it, Entity, list) }
            }
        }

        viewModel.errorState.observe(viewLifecycleOwner) {
            binding.root.snackI(it.text(requireContext()))
        }
    }

    private fun showMenu(v: View, filter: Filter, list: List<String>) {

        val popup = PopupMenu(requireContext(), v, Gravity.BOTTOM)
        val menu = popup.menu

        list.forEach {
            menu.add(it)
        }

        popup.setOnMenuItemClickListener {
            val title = it.title.toString().uppercase()
            filter.value = title
            viewModel.filter(filter)
            true
        }
        popup.show()
    }

    override fun onApproveClick(task: CheckerTask) {
        taskActionDialog(R.string.approve_task) {
            viewModel.approve(task)
        }
    }

    override fun onRejectClick(task: CheckerTask) {
        taskActionDialog(R.string.reject_task) {
            Log.d(TAG, "onRejectClick")
            viewModel.reject(task)
        }
    }

    override fun onDeleteClick(task: CheckerTask) {
        taskActionDialog(R.string.delete_task) {
            viewModel.delete(task)
        }
    }

    private fun taskActionDialog(
        @StringRes titleId: Int,
        positiveAction: () -> Unit
    ) {
        val dialogBuilder = MaterialAlertDialogBuilder(requireContext())
        dialogBuilder.apply {
            setTitle(titleId)
            setMessage(R.string.dialog_message)
            setPositiveButton(R.string.yes) { dialog, _ ->
                positiveAction()
                dialog.dismiss()
            }
            setNegativeButton(R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
        }
        dialogBuilder.show()
    }

    companion object {
        private const val TAG = "CheckerFragment"
    }
}