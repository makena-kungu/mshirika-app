package co.ke.mshirika.mshirika_app.ui_layer.ui.core.groups

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.PagingData
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.Cliente
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.group.Grupo
import co.ke.mshirika.mshirika_app.databinding.FragmentGroupBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.OnClientItemClickListener
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.adapters.ClientsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupFragment : MshirikaFragment<FragmentGroupBinding>(R.layout.fragment_group), OnClientItemClickListener {

    private val args by navArgs<GroupFragmentArgs>()
    private val group: Grupo by lazy {
        args.group
    }

    val viewModel by viewModels<GroupViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appBar.toolbar.setup(group.name, group.id.toString())

        val adapter = ClientsAdapter(
            scope = lifecycleScope,
            authKey = viewModel.key,
            listener = this
        )
        launch {
            val group = viewModel.detailedGroup(group.id) ?: return@launch
            val clients = group.activeClientMembers
            adapter.submitData(PagingData.from(clients))
        }
    }

    override fun onClickClient(containerView: View, client: Cliente, imageUrl: String?, colors: IntArray?) {
        val directions = GroupsFragmentDirections.actionGlobalClientFragment(
            client = client,
            clientImageUri = imageUrl,
            colors = colors
        )

        findNavController().navigate(directions)
    }
}