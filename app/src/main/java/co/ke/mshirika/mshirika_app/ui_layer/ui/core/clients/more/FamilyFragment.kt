package co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.Beneficiary
import co.ke.mshirika.mshirika_app.databinding.FragmentClientOtherDetailsUtilityViewBinding
import co.ke.mshirika.mshirika_app.databinding.ItemClientBeneficiaryBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FamilyFragment : MshirikaFragment<FragmentClientOtherDetailsUtilityViewBinding>(
    R.layout.fragment_client_other_details_utility_view
) {

    private val viewModel by viewModels<MoreViewModel>({ requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = Adapter()
        binding.items.adapter = adapter
        viewModel.beneficiario.observe(viewLifecycleOwner) {
            val beneficiaries = it ?: return@observe
            adapter.submitList(beneficiaries.data)
        }
    }

    override fun executeInChild() {
        // open create family dialog
    }



    internal inner class Adapter : ListAdapter<Beneficiary.Data, Adapter.BVH>(Beneficiary.Data) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BVH {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemClientBeneficiaryBinding.inflate(inflater, parent, false)
            return BVH(binding)
        }

        override fun onBindViewHolder(holder: BVH, position: Int) {
            val fam = getItem(position) ?: return
            holder.bind(fam)
        }

        inner class BVH(
            private val binding: ItemClientBeneficiaryBinding
        ) : RecyclerView.ViewHolder(binding.root) {

            fun bind(beneficiary: Beneficiary.Data) {
                binding.beneficiary = beneficiary
            }

        }
    }
}