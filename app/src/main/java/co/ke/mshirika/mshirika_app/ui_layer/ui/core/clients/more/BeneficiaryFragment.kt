package co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.more

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.Family
import co.ke.mshirika.mshirika_app.databinding.BottomSheetAddBeneficiaryBinding
import co.ke.mshirika.mshirika_app.databinding.FragmentClientOtherDetailsUtilityViewBinding
import co.ke.mshirika.mshirika_app.databinding.ItemClientFamilyMemberBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.fromShortDate
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.attachNonVoidFields
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.text
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.OperationalUtils.openDatePicker
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.setAdapter
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.snackL
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BeneficiaryFragment : MshirikaFragment<FragmentClientOtherDetailsUtilityViewBinding>(
    R.layout.fragment_client_other_details_utility_view
) {

    private val viewModel by viewModels<MoreViewModel>({ requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = Adapter()
        binding.items.adapter = adapter
        viewModel.familia.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun executeInChild() {
        val bottomSheet = BottomSheet()
        bottomSheet.show(
            childFragmentManager,
            "add_beneficiary"
        )
    }

    internal inner class BottomSheet : BottomSheetDialogFragment() {

        private var _binding: BottomSheetAddBeneficiaryBinding? = null
        private val binding: BottomSheetAddBeneficiaryBinding get() = _binding!!

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = BottomSheetAddBeneficiaryBinding.inflate(inflater, container, false)
            return binding.root
        }


        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            binding.apply {
                launch {
                    val template = viewModel.template() ?: return@launch
                    val relationships = template
                        .familyMemberOptions
                        .relationshipIdOptions
                        .map { it.name }
                    beneficiaryRelationship.setAdapter(relationships)
                }

                beneficiaryDobLo.openDatePicker(R.string.date_of_birth, beneficiaryDob)

                addBeneficiary.attachNonVoidFields(
                    beneficiaryName,
                    beneficiaryRelationship,
                    beneficiaryDob
                )

                addBeneficiary.setOnClickListener {
                    lifecycleScope.launch { add() }
                }
            }
        }

        private suspend fun BottomSheetAddBeneficiaryBinding.add() {
            val name = beneficiaryName.text()
            val relationship = beneficiaryRelationship.text()
            val idNumber = beneficiaryIdNumber.text()
            val phoneNumber = "+254${beneficiaryPhoneNumber.text()}"

            val beneficiary = viewModel.addBeneficiary(
                name = name,
                relationship = relationship,
                idNumber = idNumber,
                phoneNumber = phoneNumber,
                dob = beneficiaryDob.text().fromShortDate,
                percent = beneficiaryPercentAllocation.text()
            ) ?: kotlin.run {
                Log.d(TAG, "onViewCreated: beneficiary not created")
                root.snackL(R.string.beneficiary_not_added)
                return
            }

            Log.d(TAG, "onViewCreated: $beneficiary")
            root.snackL(R.string.beneficiary_added)
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }

    internal inner class Adapter : ListAdapter<Family.Fam, Adapter.FVH>(Family.Fam) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FVH {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemClientFamilyMemberBinding.inflate(inflater, parent, false)
            return FVH(binding)
        }

        override fun onBindViewHolder(holder: FVH, position: Int) {
            val fam = getItem(position) ?: return
            holder.bind(fam)
        }

        inner class FVH(
            private val binding: ItemClientFamilyMemberBinding
        ) : RecyclerView.ViewHolder(binding.root) {

            fun bind(fam: Family.Fam) {
                binding.fam = fam
            }

        }
    }

    companion object {
        private const val TAG = "BeneficiaryFragment"
    }
}