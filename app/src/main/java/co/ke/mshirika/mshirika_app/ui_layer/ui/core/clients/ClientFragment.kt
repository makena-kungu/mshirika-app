package co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.annotation.FloatRange
import androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.accessibility.AccessibilityEventCompat.setAction
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Transaction
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.client.Client
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.loan.ConservativeLoanAccount
import co.ke.mshirika.mshirika_app.databinding.ContentLoansAndTransactionsBinding
import co.ke.mshirika.mshirika_app.databinding.FragmentClientBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.DetailsFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.ClientFragmentDirections.Companion.actionGlobalTransactionFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.adapters.LoanAccountsAdapter
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.adapters.TransactionsAdapter
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.adapters.TransactionsAdapter.OnTransactionsItemClickListener
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.viewModels.ClientViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.LoansFragmentDirections
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.OnLoanClickListener
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.Transitions.itemToDetailReentry
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.Transitions.itemToDetailsTransitionId
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.UIText
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.amt
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.drawable
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.randomColors
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.abs

private const val TAG = "ClientFragment"

@AndroidEntryPoint
class ClientFragment : DetailsFragment<FragmentClientBinding>(
    R.layout.fragment_client
), OnLoanClickListener, OnMenuItemClickListener, OnTransactionsItemClickListener {

    private val args by navArgs<ClientFragmentArgs>()
    private val viewModel by navGraphViewModels<ClientViewModel>(R.id.clientFragment)
    private val client get() = args.client
    private val _colors get() = args.colors
    private val imageUrl get() = args.clientImageUri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.client(client)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemToDetailReentry(view)
        binding.fragment = this
        binding.apply {
            clientToolbar.setup(R.string.client)
            loadImage()
            client.apply {
                bindClient()
            }
            viewModel.cuentaDeAhorros.observe(viewLifecycleOwner) { account ->
                if (account == null) return@observe

                binding.balance.text = account.accountBalance.amt
            }

            errorState()
            loadingState()
            setupAppBar()
            setupLoans()
            loansAndTransactions.setupTransactions()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.client(client)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean = item?.run {
        when (itemId) {
            R.id.edit -> {
                edit()
            }
            R.id.withdraw -> {
                withdraw()
                true
            }
            R.id.charge -> {
                charges()
                true
            }
            R.id.transfer -> {
                transfer()
                true
            }
            else -> false
        }
    } == true

    private fun loadImage() {
        val listener = LoadImageListener()
        if (imageUrl == null) {
            //this implies that the client does not have an image
            listener.loadBackupImage()
            return
        }

        launch {
            val key = withContext(IO) { viewModel.authKey()!! }
            val headers = LazyHeaders
                .Builder()
                .addHeader("Authorization", key)
                .addHeader("Fineract-Platform-TenantId", "default").build()
            val url = GlideUrl(
                imageUrl,
                headers
            )
            Glide.with(requireActivity())
                .load(url)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .addListener(listener)
                .into(binding.clientImage)
        }
    }

    private fun errorState() {
        viewModel.errorState.collectLatestLifecycle { uiText ->
            var title: String? = null
            var action: (() -> Unit)? = null

            val text = when (uiText) {
                is UIText.DynamicText -> {
                    title = uiText.title
                    action = uiText.action
                    uiText.text
                }
                is UIText.ResourceText -> {
                    uiText.text(requireContext())
                }
                is UIText.PlainText -> uiText.s
            }

            Snackbar.make(
                binding.root,
                text,
                LENGTH_LONG
            ).apply {
                title?.let { title ->
                    //if the title is not null then there's an action present
                    setAction(title) {
                        action?.let { function -> function() }
                    }
                }
                show()
            }
        }
    }

    private fun loadingState() {
        viewModel.loadingState.collectLatestLifecycle {
            binding.progressHorizontal.isVisible = it
        }
    }

    context (FragmentClientBinding, Client) private
    fun bindClient() {
        Log.d(TAG, "bindClient: starting")

        clientName.text = displayName
        clientMobileNumber.text = mobileNo
        clientMembershipNo.text = externalId
    }

    private fun FragmentClientBinding.setupAppBar() {
        AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val scrollRange = appBarLayout.totalScrollRange.toFloat()
            val percent = abs(verticalOffset) / scrollRange
            constraintLayout.progress = percent
            constraintLayout.addTransitionListener(AppBarTransition())
        }.also {
            appBarLayout.addOnOffsetChangedListener(it)
        }
    }

    private fun FragmentClientBinding.setupLoans() {
        val adapter = LoanAccountsAdapter(this@ClientFragment)
        loansAndTransactions.loans.adapter = adapter
        viewModel.prestamos.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun ContentLoansAndTransactionsBinding.setupTransactions() {
        val adapter = TransactionsAdapter(this@ClientFragment)
        transactions.adapter = adapter
        viewModel.actas.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onLoanClicked(
        loanAccount: ConservativeLoanAccount,
        position: Int,
        container: View
    ) {
        //todo view a loan
    }

    override fun onLoanRepayClicked(
        loanAccount: ConservativeLoanAccount,
        position: Int,
        container: View
    ): Boolean {
        val dirs = LoansFragmentDirections.actionGlobalLoanRepaymentFragment(
            clientName = loanAccount.clientName,
            clientId = loanAccount.clientId,
            loanId = loanAccount.id
        )
        itemToDetailsTransitionId(
            dirs,
            container to R.string.loan_card_transition_name
        )
        return false
    }

    override fun onClickTransaction(container: View, transaction: Transaction) {
        itemToDetailsTransitionId(
            actionGlobalTransactionFragment(transaction),
            container to R.string.transition_card_transition_name
        )
    }

    //todo the following methods
    fun newSavingsAccount() {

    }

    fun newLoan() {
        val directions = ClientFragmentDirections.actionClientFragmentToNewLoanFragment(client)
        findNavController().navigate(directions)
    }

    fun payment() {

    }

    private fun withdraw() {

    }

    private fun transfer() {

    }

    private fun edit() {
        // todo edit the client use the create client fragment only the general data
    }

    private fun charges() {

    }

    fun openTransactionHistoryFragment() {}

    inner class LoadImageListener : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            mBinding?.loadBackupImage()
            return true
        }

        fun loadBackupImage() {
            binding.loadBackupImage()
        }

        private fun FragmentClientBinding.loadBackupImage() {
            clientImage.visibility = View.INVISIBLE
            clientImageError.apply {
                isVisible = true
                val colors = _colors ?: requireContext().randomColors
                drawable(colors)
                text = client.displayName[0].uppercase()
                animate().apply {
                    scaleX(1f)
                    scaleY(1f)
                    duration =
                        resources.getInteger(R.integer.material_motion_duration_short_1).toLong()
                    start()
                }
            }
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            return false
        }
    }

    inner class AppBarTransition : MotionLayout.TransitionListener {

        override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {}

        override fun onTransitionChange(
            motionLayout: MotionLayout?,
            startId: Int,
            endId: Int,
            progress: Float
        ) {

            motionLayout?.let {

            }
            //the text size is as it is say if text-size = 28 then it would be 28sp in xml
            binding.apply {
                clientName.textSize = textSize(28F, 20F, progress)
                balance.textSize = textSize(24F, 14F, progress)

                // error progress ranges from 0 - .4 if greater than .4, ignore it
                if (progress > .4 && clientImageError.textSize > 30) {
                    clientImageError.textSize = 30F
                    return
                }
                val errorProgress = progress / .4f
                clientImageError.textSize = textSize(60F, 30F, errorProgress)
            }
        }

        override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {}

        override fun onTransitionTrigger(
            motionLayout: MotionLayout?,
            triggerId: Int,
            positive: Boolean,
            progress: Float
        ) {
        }


        private fun textSize(
            originalTextSize: Float,
            finalTextSize: Float,
            @FloatRange(
                from = 0.0,
                to = 1.0,
                fromInclusive = true,
                toInclusive = true
            ) progress: Float
        ): Float {
            val diff = originalTextSize - finalTextSize
            return originalTextSize - abs(progress * diff)
        }
    }
}
