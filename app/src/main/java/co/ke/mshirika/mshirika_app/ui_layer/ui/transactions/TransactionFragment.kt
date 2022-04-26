package co.ke.mshirika.mshirika_app.ui_layer.ui.transactions

import android.os.Bundle
import android.view.View
import android.widget.SimpleAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentTransactionDetailsBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment

class TransactionFragment :
    MshirikaFragment<FragmentTransactionDetailsBinding>(R.layout.fragment_transaction_details) {

    private val args: TransactionFragmentArgs by navArgs()
    private val viewModel: TransactionViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tr = args.transaction
        binding.transaction = tr
        viewModel.transaction(tr)

        viewModel.data.collectLatestLifecycle {
            val data = it.map { map ->
                mapOf(
                    FIRST to getString(map.first),
                    SECOND to map.second
                )
            }
            val adapter = SimpleAdapter(
                requireContext(),
                data,
                R.layout.item_transaction_details,
                arrayOf(FIRST, SECOND),
                intArrayOf(R.id.details_heading, R.id.details_value)
            )
            binding.transactionDetails.adapter = adapter
        }
    }

    companion object {
        const val FIRST = "first"
        const val SECOND = "second"
    }
}