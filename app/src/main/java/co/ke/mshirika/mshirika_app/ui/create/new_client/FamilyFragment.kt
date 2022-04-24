package co.ke.mshirika.mshirika_app.ui.create.new_client

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
import co.ke.mshirika.mshirika_app.R.layout.fragment_new_client_family
import co.ke.mshirika.mshirika_app.databinding.FragmentNewClientFamilyBinding
import co.ke.mshirika.mshirika_app.ui.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui.create.ViewerFragment
import co.ke.mshirika.mshirika_app.ui.create.new_client.FamilyMembersAdapter.FamViewHolder
import co.ke.mshirika.mshirika_app.ui.util.DateUtil.fromShortDate
import co.ke.mshirika.mshirika_app.ui.util.EditableUtils.clear
import co.ke.mshirika.mshirika_app.ui.util.EditableUtils.text
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import kotlinx.coroutines.flow.collectLatest

class FamilyFragment :
    MshirikaFragment<FragmentNewClientFamilyBinding>(fragment_new_client_family), ViewerFragment {

    private val viewModel: ViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = FamilyMembersAdapter()
        binding.familyMembers.adapter = adapter
        lifecycleScope.launchWhenCreated {
            viewModel.familyMembers.collectLatest {
                adapter.submitList(it)
            }
            binding.setupAdapters()
            binding.setupClearing()
        }
    }

    private suspend fun FragmentNewClientFamilyBinding.setupClearing() {
        viewModel.clientCreated.collectLatest {
            if (it) clear(fullName, dob, relationship, gender, maritalStatus)
        }
    }

    private suspend fun FragmentNewClientFamilyBinding.setupAdapters() {
        viewModel.template.collectLatest { template ->
            template.familyMemberOptions.maritalStatusIdOptions.map { it.name }.also { list ->
                maritalStatus.setAdapter(list)
            }
            template.familyMemberOptions.relationshipIdOptions.filter {
                it.name != "father-in-law"
            }.map { it.name }.also { list ->
                relationship.setAdapter(list)
            }
            template.genderOptions.map {
                it.name
            }.also {
                gender.setAdapter(it)
            }
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
            val dOB = dob.text().fromShortDate
            viewModel.addFamilyMember(
                FamilyMember(
                    name = fullName.text(),
                    dob = dOB,
                    relationship = relationship.text(),
                    gender = gender.text(),
                    maritalStatus = maritalStatus.text()
                )
            )
        }
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
    val relationship: String,
    val gender: String,
    val maritalStatus: String
) {
    companion object : DiffUtil.ItemCallback<FamilyMember>() {
        override fun areItemsTheSame(oldItem: FamilyMember, newItem: FamilyMember): Boolean =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: FamilyMember, newItem: FamilyMember): Boolean =
            oldItem == newItem
    }
}