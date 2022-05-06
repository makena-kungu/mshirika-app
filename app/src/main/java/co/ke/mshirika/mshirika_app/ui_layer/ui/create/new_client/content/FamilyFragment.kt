package co.ke.mshirika.mshirika_app.ui_layer.ui.create.new_client.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.R.layout.fragment_new_client_family
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.templates.ClientTemplate
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.databinding.FragmentNewClientFamilyBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.create.ViewerFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.create.new_client.ViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.create.new_client.content.FamilyMembersAdapter.FamViewHolder
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.fromShortDate
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.shortDate
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.clear
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.text
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.OperationalUtils.openDatePicker
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.atomic.AtomicReference

@AndroidEntryPoint
class FamilyFragment : MshirikaFragment<FragmentNewClientFamilyBinding>(
    fragment_new_client_family
), ViewerFragment {

    private val viewModel: ViewModel by viewModels()
    private val _template = AtomicReference<ClientTemplate>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.updateTitle(R.string.family_members)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = FamilyMembersAdapter()
        binding.familyMembers.adapter = adapter
        viewModel.familyMembers.collectLatestLifecycle {
            adapter.submitList(it)
        }
        binding.setupAdapters()
        binding.setupClearing()
        binding.dobLo.setEndIconOnClickListener {
            openDatePicker(R.string.date_of_birth) {
                binding.dob.setText(it.shortDate)
            }
        }
    }

    private fun FragmentNewClientFamilyBinding.setupClearing() {
        viewModel.clientCreated.collectLatestLifecycle {
            if (it) clear(fullName, dob, relationship, gender, maritalStatus)
        }
    }

    private fun FragmentNewClientFamilyBinding.setupAdapters() {
        launch {
            val outcome = viewModel.template()
            if (outcome !is Outcome.Success) return@launch
            val template = outcome.data ?: return@launch
            _template.set(template)

            val maritalStatusOptions = template
                .familyMemberOptions
                .maritalStatusIdOptions
                .map { it.name }
            maritalStatus.setAdapter(maritalStatusOptions)

            val familyMemberOptions = template.familyMemberOptions.relationshipIdOptions
                .filter { it.name != "father-in_law" }
                .map { it.name }
            relationship.setAdapter(familyMemberOptions)

            val genderOptions = template.genderOptions.map { it.name }
            gender.setAdapter(genderOptions)
        }
    }

    private fun MaterialAutoCompleteTextView.setAdapter(list: List<String>) {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            android.R.id.text1,
            list
        )
        setAdapter(adapter)
    }

    fun addFamilyMember() {
        binding.apply {
            _template.get().apply {
                addFam()
            }
        }
    }

    context (FragmentNewClientFamilyBinding, ClientTemplate) private fun addFam() {
        val dOB = dob.text().fromShortDate
        viewModel.addFamilyMember(
            FamilyMember(
                name = fullName.text(),
                dob = dOB,
                relationship = genderOptions.find { it.name == relationship.text() }!!.id,
                gender = familyMemberOptions.relationshipIdOptions.find {
                    it.name == gender.text()
                }!!.id,
                maritalStatus = familyMemberOptions.maritalStatusIdOptions.find {
                    it.name == maritalStatus.text()
                }!!.id
            )
        )
    }

    override fun onNextPressed() {

    }
}

class FamilyMembersAdapter : ListAdapter<FamilyMember, FamViewHolder>(FamilyMember) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FamViewHolder =
        with(LayoutInflater.from(parent.context)) {
            inflate(android.R.layout.simple_list_item_1, parent, false)
        }.let {
            FamViewHolder(it)
        }

    override fun onBindViewHolder(holder: FamViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FamViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: FamilyMember) {
            val textView: TextView = itemView.findViewById(android.R.id.text1)
            textView.text = item.name
        }
    }
}

data class FamilyMember(
    val name: String,
    val dob: Long,
    val relationship: Int,
    val gender: Int,
    val maritalStatus: Int
) {
    companion object : DiffUtil.ItemCallback<FamilyMember>() {
        override fun areItemsTheSame(oldItem: FamilyMember, newItem: FamilyMember): Boolean =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: FamilyMember, newItem: FamilyMember): Boolean =
            oldItem == newItem
    }
}