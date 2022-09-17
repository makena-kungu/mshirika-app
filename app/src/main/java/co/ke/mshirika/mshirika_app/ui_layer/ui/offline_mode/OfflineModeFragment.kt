package co.ke.mshirika.mshirika_app.ui_layer.ui.offline_mode

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Operation
import androidx.work.WorkManager
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.work_manager.offline.SyncOffline
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.work_manager.online.SyncOnline
import co.ke.mshirika.mshirika_app.databinding.FragmentOfflineModeBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OfflineModeFragment : MshirikaFragment<FragmentOfflineModeBinding>(R.layout.fragment_offline_mode) {

    private val viewModel: OfflineModeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun syncAndGoOnline(): Boolean {
        return goOffline(false)
    }

    var work: Operation? = null
    @Suppress("SameParameterValue")
    private fun goOffline(enableOfflineMode: Boolean = true): Boolean {
        if (work != null) return false
        val context = requireContext()
        val workManager = WorkManager.getInstance(context)
        when {
            enableOfflineMode -> OneTimeWorkRequestBuilder<SyncOffline>()
            else -> OneTimeWorkRequestBuilder<SyncOnline>()
        }.build().also {
            work = workManager.enqueue(it)
            work?.run {
                state.observe(viewLifecycleOwner) { state ->
                    if (state is Operation.State.IN_PROGRESS) {
                        // TODO: show a kinda progress bar
                        return@observe
                    }
                    if (state is Operation.State.FAILURE) {
                        // show a failure message
                        return@observe
                    }

                    // when the work is successful
                    when {
                        enableOfflineMode -> {
//                            viewModel.goOffline()
                        }
                        else -> {
//                            viewModel.syncAndGoOnline()
                        }
                    }
                    work = null
                }
            }
        }

//        if (enableOfflineMode) viewModel.goOffline()
//        else viewModel.syncAndGoOnline()

        return true
    }
}