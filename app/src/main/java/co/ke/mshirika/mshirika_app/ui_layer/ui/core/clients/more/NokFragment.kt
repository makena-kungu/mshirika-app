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
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.NextOfKin
import co.ke.mshirika.mshirika_app.databinding.BottomSheetAddNokBinding
import co.ke.mshirika.mshirika_app.databinding.FragmentClientOtherDetailsUtilityViewBinding
import co.ke.mshirika.mshirika_app.databinding.ItemClientNextOfKinBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.attachNonVoidFields
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.text
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.setAdapter
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.snackL
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NokFragment : MshirikaFragment<FragmentClientOtherDetailsUtilityViewBinding>(
    R.layout.fragment_client_other_details_utility_view
) {

    private val viewModel by viewModels<MoreViewModel>({ requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = Adapter()
        binding.items.adapter = adapter
        viewModel.parienteMasCercano.observe(viewLifecycleOwner) {
            val list = it ?: return@observe
            adapter.submitList(list.data)
        }
    }

    override fun executeInChild() {
        BottomSheet().show(childFragmentManager, "add_nok")
    }

    internal inner class BottomSheet : BottomSheetDialogFragment() {

        private var _binding: BottomSheetAddNokBinding? = null
        private val binding: BottomSheetAddNokBinding get() = _binding!!

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = BottomSheetAddNokBinding.inflate(inflater, container, false)
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
                    nokRelationship.setAdapter(relationships)
                }
                addNextOfKin.attachNonVoidFields(nokName, nokRelationship)
                addNextOfKin.setOnClickListener {
                    val name = nokName.text()
                    val relationship = nokRelationship.text()
                    val idNumber = nokIdNumber.text()
                    val phoneNumber = "+254${nokPhoneNumber.text()}"

                    lifecycleScope.launch {
                        val nok = viewModel.addNok(
                            name = name,
                            relationship = relationship,
                            idNumber = idNumber,
                            phoneNumber = phoneNumber
                        ) ?: kotlin.run {
                            Log.d(TAG, "onViewCreated: nok not created")
                            root.snackL(R.string.nok_not_added)
                            return@launch
                        }

                        Log.d(TAG, "onViewCreated: $nok")
                        root.snackL(R.string.nok_added)
                    }
                }
            }
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }

    internal inner class Adapter : ListAdapter<NextOfKin.Datum, Adapter.NVH>(NextOfKin.Datum) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NVH {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemClientNextOfKinBinding.inflate(inflater, parent, false)
            return NVH(binding)
        }

        override fun onBindViewHolder(holder: NVH, position: Int) {
            val fam = getItem(position) ?: return
            holder.bind(fam)
        }

        inner class NVH(
            private val binding: ItemClientNextOfKinBinding
        ) : RecyclerView.ViewHolder(binding.root) {

            fun bind(nok: NextOfKin.Datum) {
                binding.nok = nok
            }

        }
    }

    companion object {
        private const val TAG = "NokFragment"
    }
}