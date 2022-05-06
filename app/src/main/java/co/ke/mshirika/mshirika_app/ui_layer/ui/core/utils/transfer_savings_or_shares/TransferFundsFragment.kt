package co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.transfer_savings_or_shares

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Client
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.SavingsAccount
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.templates.TransferFundsTemplateWithToClientsAccounts
import co.ke.mshirika.mshirika_app.databinding.FragmentTransferFundsBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.fromShortDate
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.mshirikaDate
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.attachNonVoidFields
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.text
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.amt
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.setAdapter
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.snackS
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.resourceText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TransferFundsFragment : MshirikaFragment<FragmentTransferFundsBinding>(
    R.layout.fragment_transfer_funds
) {
    //register an argument, client, to be passed to this fragment

    private val viewModel by viewModels<TransferFundsViewModel>()
    private val args: TransferFundsFragmentArgs by navArgs()
    private val _client: Client get() = args.client
    private val account: SavingsAccount get() = args.account

    private var _templateWithToClientsAccounts: TransferFundsTemplateWithToClientsAccounts? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appBar.toolbar.setupToolbar(R.string.transfer)
        binding.setupFields()
        binding.availableBalance.text = account.accountBalance.amt
        setupError()
        setupLoading()
        setupSuccess()
    }

    private fun FragmentTransferFundsBinding.setupFields() {
        transfer.attachNonVoidFields(
            office,
            client,
            accountType,
            account,
            amount,
            date,
            description
        )

        lifecycleScope.launch {
            try {
                val template = requireNotNull(viewModel.getTemplate(_client))
                val offices = template.toOfficeOptions.map { it.name }
                office.setAdapter(offices)

                office.setOnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
                    val officeName = offices[position]
                    val office = template.toOfficeOptions.find { it.name == officeName }
                        ?: return@setOnItemClickListener
                    lifecycleScope.launch {
                        val templateWithToClients = requireNotNull(
                            viewModel.getTemplate(
                                _client,
                                office.id
                            )
                        )

                        val clients = templateWithToClients
                            .toClientOptions
                            .map { it.displayName }
                        client.setAdapter(clients)

                        val accountTypes = templateWithToClients
                            .toAccountTypeOptions
                            .map { it.value }
                        accountType.setAdapter(accountTypes)
                        @Suppress("UsePropertyAccessSyntax")
                        accountType.setOnItemClickListener AccountSelected@{ _: AdapterView<*>, _: View?, position: Int, _: Long ->
                            val accountType = templateWithToClients.toAccountTypeOptions.find {
                                it.value == accountTypes[position]
                            } ?: return@AccountSelected

                            viewModel.viewModelScope.launch {
                                val templateWithToClientsAccounts = requireNotNull(
                                    viewModel.getTemplate(
                                        _client,
                                        templateWithToClients.toOffice.id,
                                        accountType.id
                                    )
                                )
                                _templateWithToClientsAccounts = templateWithToClientsAccounts

                                val accounts = templateWithToClientsAccounts.toAccountOptions.map {
                                    it.productName
                                }
                                account.setAdapter(accounts)
                            }
                        }
                    }
                }
            } catch (e: IllegalArgumentException) {
                val error = resourceText(R.string.an_error_occurred)
                viewModel.errorChannel.send(error)
                delay(1000)
                findNavController().navigateUp()
            }
        }
        //query the necessary required options
        // we will use the users default savingsProductId
    }


    private fun setupError() {
        viewModel.errorState.collectLatestLifecycle {
            val text = it.text(requireContext())
            binding.root.snackS(text)
        }
    }

    private fun setupLoading() {
        viewModel.loadingState.collectLatestLifecycle {
            binding.transferLoading.isVisible = it
        }
    }

    private fun setupSuccess() {
        viewModel.successState.collectLatestLifecycle {
            binding.root.snackS(it.text(requireContext()))
            findNavController().navigateUp()
        }
    }

    fun transfer() {
        _templateWithToClientsAccounts?.apply {
            binding.transfer()
        }
    }

    context (TransferFundsTemplateWithToClientsAccounts)
            private fun FragmentTransferFundsBinding.transfer() {
        val transfer = TransferSavingsOrShares(
            officeId = toOffice.id,
            clientId = toClient.id,
            accountTypeId = toAccountType.id,
            accountId = toAccountOptions.first { it.productName == account.text() }.id,
            amount = amount.text().toInt(),
            date = date.text().fromShortDate.mshirikaDate,
            description = description.text()
        )
        viewModel.transfer(_client, transfer)
    }
}

data class TransferSavingsOrShares(
    val officeId: Int,
    val clientId: Int,
    val accountTypeId: Int,
    val accountId: Int,
    val amount: Int,
    val date: String,
    val description: String,
)