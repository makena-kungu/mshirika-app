package co.ke.mshirika.mshirika_app.ui.create.new_client

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import co.ke.mshirika.mshirika_app.ui.util.EditableUtils.text
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
            viewModel.template.collectLatest {
                // todo setup all the adapters
            }
        }
    }

    fun addFamilyMember() {
        binding.apply {
            viewModel.addFamilyMember(
                FamilyMember(
                    fullName.text(),
                    dob.text().fromShortDate,
                    // TODO: Setup an adapter for this field
                    relationship.text(),
                    gender.text().equals("male", true),
                    maritalStatus.text()
                )
            )
        }
    }

    override fun onNextPressed() {
        TODO("Not yet implemented")
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
    val gender: Boolean,
    val maritalStatus: String
) {
    companion object : DiffUtil.ItemCallback<FamilyMember>() {
        override fun areItemsTheSame(oldItem: FamilyMember, newItem: FamilyMember): Boolean =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: FamilyMember, newItem: FamilyMember): Boolean =
            oldItem == newItem
    }
}