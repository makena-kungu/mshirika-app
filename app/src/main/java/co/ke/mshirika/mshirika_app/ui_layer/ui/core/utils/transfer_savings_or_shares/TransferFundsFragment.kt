package co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.transfer_savings_or_shares

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.transfer_funds.TransferFundsTemplateWithToClientsAccounts
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
    private val args by navArgs<TransferFundsFragmentArgs>()

    private val accountBalance: Double by lazy { args.accountBalance.toDouble() }
    private val clientId: Int by lazy { args.clientId }
    private val savingsAccountId: Int by lazy { args.savingsAccountId }
    private val officeId: Int by lazy { args.officeId }

    private var _templateWithToClientsAccounts: TransferFundsTemplateWithToClientsAccounts? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appBar.toolbar.setup(R.string.transfer)
        binding.setupFields()
        binding.availableBalance.text = accountBalance.amt
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

        launch {
            try {
                val template = viewModel.getTemplate(savingsAccountId) ?: return@launch
                val offices = template.toOfficeOptions.map { it.name }
                office.setAdapter(offices)

                office.setOnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
                    val officeName = offices[position]
                    val office = template.toOfficeOptions.find { it.name == officeName }
                        ?: return@setOnItemClickListener
                    launch clients@{
                        val templateWithToClients = viewModel.getTemplate(
                            savingsAccountId,
                            office.id
                        ) ?: return@clients

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

                            launch accounts@{
                                val templateWithToClientsAccounts = viewModel.getTemplate(
                                    clientId,
                                    templateWithToClients.toOffice.id,
                                    accountType.id
                                ) ?: return@accounts
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
        viewModel.errorState.observe(viewLifecycleOwner) {
            val text = it.text(requireContext())
            binding.root.snackS(text)
        }
    }

    private fun setupLoading() {
        viewModel.loadingState.observe(viewLifecycleOwner) {
            binding.transferLoading.isVisible = it
        }
    }

    private fun setupSuccess() {
        viewModel.successState.observe(viewLifecycleOwner) {
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
        val client = Triple(clientId, savingsAccountId, officeId)
        viewModel.transfer(client, transfer)
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