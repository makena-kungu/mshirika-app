package co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.new_client

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.Cliente
import co.ke.mshirika.mshirika_app.databinding.FragmentEditClientBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.new_client.content.GeneralFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.new_client.content.GeneralFragment.Companion.KEY_CLIENTE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditClientFragment :
    MshirikaFragment<FragmentEditClientBinding>(
        R.layout.fragment_edit_client
    ) {

    private val frag by lazy {
        val frag = GeneralFragment()
        val args = Bundle()
        args.putParcelable(KEY_CLIENTE, client)
        frag.arguments = args
        frag
    }
    private val viewModel: CreateClientViewModel by viewModels()
    private val args by navArgs<EditClientFragmentArgs>()
    private val client: Cliente by lazy { args.cliente }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.task(Task.Edit)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appBarLarge.toolbarLarge.setup(R.string.edit_client)

        launch {
            val template = viewModel.getEditTemplate(client.id) ?: return@launch
            template.groups
        }

        childFragmentManager
            .beginTransaction()
            .replace(
                binding.container.id,
                frag,
                "general_fragment"
            ).commit()
    }

    fun edit() {
        frag.edit()
    }
}