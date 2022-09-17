package co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.payment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.ChargesTemplate
import co.ke.mshirika.mshirika_app.databinding.FragmentPaymentBinding
import co.ke.mshirika.mshirika_app.databinding.ItemPaymentBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.view_loan.observeNonNull
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.fromShortDate
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.mshirikaDate
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.today
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.attachNonVoidFields
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.text
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.viewsOpeningTheDatePicker
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.setAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentFragment : MshirikaFragment<FragmentPaymentBinding>(R.layout.fragment_payment) {

    private val args by navArgs<PaymentFragmentArgs>()
    private val viewModel: PaymentViewModel by viewModels()

    private val clientId by lazy { args.clientId }
    private val savingsAccountId by lazy { args.savingsAccountId }
    private val bottomSheet by lazy {
        val view = BottomSheetBehavior.from(binding.bottomSheet)
        view.state = STATE_HIDDEN
        binding.bottomSheet.isVisible = true
        view
    }

    private lateinit var adapter: PaymentAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.account(savingsAccountId)
        viewModel.clientId(clientId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.frag = this
        binding.appBar.toolbarLarge.setup(R.string.make_payment)
        binding.setupViews()
        binding.paymentCharges.apply {
            val chargesAdapter = ChargesAdapter()
            adapter = chargesAdapter
            viewModel.charges.observe(viewLifecycleOwner) {
                chargesAdapter.submitList(it)
            }
        }

        bottomSheet()
        binding.savingsAccount.setEndIconOnClickListener {
            showBottomSheet()
        }

        binding.apply {
            arrayOf(transactionDate, bankDate).forEach { it.setText(today) }

            //todo
            receiptNo.setText(generateCode)
            amount.setText("1000")
        }

        viewsOpeningTheDatePicker(
            Triple(R.string.transaction_date, binding.transactionDate, binding.transactionDateLo),
            Triple(R.string.bank_date, binding.bankDate, binding.bankDateLo)
        )
    }

    private fun showBottomSheet() {
        bottomSheet.state = STATE_EXPANDED
    }

    private fun hideBottomSheet() {
        bottomSheet.state = STATE_HIDDEN
    }

    private fun bottomSheet() {
        binding.apply {
            submit.attachNonVoidFields(paymentMode, amount, receiptNo, bankDate)
        }
        var template: ChargesTemplate? = null

        launch {
            template = viewModel.chargeTemplate(savingsAccountId)
            val charges = template?.chargeOptions?.map { it.name } ?: return@launch
            binding.charge.setSimpleItems(charges.toTypedArray())
        }
        binding.charge.doAfterTextChanged { editable ->
            val charge = editable?.text() ?: return@doAfterTextChanged

            launch {
                val chargesTemplate = template ?: return@launch
                val chargeId = chargesTemplate.chargeOptions
                    .firstOrNull { it.name == charge }?.id ?: return@launch
                val template2 = viewModel.chargeTemplate2(chargeId = chargeId) ?: return@launch
                val amount = template2.amount.toInt().toString()
                binding.chargeAmount.setText(amount)
            }
        }
    }

    private fun FragmentPaymentBinding.setupViews() {
        adapter = PaymentAdapter()
        launch {
            val accounts = viewModel.accounts(clientId) ?: return@launch
            val prestamos = accounts.loans
            adapter.submitList(prestamos)
            loanAccs.adapter = adapter
        }

        viewModel.templateFlow.observeNonNull(viewLifecycleOwner) { template ->
            val list = template.paymentTypes.map { it.name }
            paymentMode.setAdapter(list)
        }

        launch {
            val template = viewModel.temp(clientId) ?: return@launch
            val list = template.paymentTypes.map { it.name }
            paymentMode.setAdapter(list)
        }
    }

    fun submit() {
        binding.submit()
    }

    fun submitCharge() {
        val amount = binding.chargeAmount.text().toInt()
        viewModel.charge(savingsAccountId, amount)
        hideBottomSheet()
    }

    fun FragmentPaymentBinding.submit() {
        val loans = mutableListOf<PaymentLoan>()
        val itemCount = adapter.itemCount
        (0..itemCount).forEach { position ->
            loanAccs.layoutManager?.findViewByPosition(position)?.let { view ->
                with(ItemPaymentBinding.bind(view)) {
                    val text = amount.text()
                    val amt = if (text.isNotBlank()) text.toDouble() else null

                    amt?.let { amount ->
                        val loan = adapter.getLoan(position)
                        loans += PaymentLoan(loan.id, amount)
                    }
                }
            }
        }

        val amountText = amount.text()
        val amount = if (amountText.isNotBlank()) amountText.toDouble() else null
        val charges = mutableListOf<PaymentCharge>()
        viewModel.charges.value?.let {
            it.forEach { (chargeId, _, _, _, _, _, _, _, _, _, amount) ->
                charges += PaymentCharge(chargeId, amount)
            }
        }

        Log.d(TAG, "submit: check point")

        val deposits = mutableListOf<PaymentDeposit>()
        if (amount != null) {
            deposits += PaymentDeposit(savingsAccountId, amount, charges)
        }

        viewModel.pay(
            transactionDate = transactionDate.text().fromShortDate.mshirikaDate,
            paymentType = paymentMode.text(),
            bankDate = bankDate.text().fromShortDate.mshirikaDate,
            receiptNumber = receiptNo.text(),
            deposits = deposits,
            loanRepayments = loans
        )
    }

    companion object {
        private const val TAG = "PaymentFragment"

        private val generateCode: String
            get() {
                val builder = StringBuilder()
                val len = 15
                val chars = 'A'..'Z'
                val numbers = '0'..'9'
                val validChars = chars.toList() + numbers.toList()
                repeat(len) {
                    builder.append(validChars.random())
                }
                val string = builder.toString()
                builder.clear()
                return string
            }
    }
}