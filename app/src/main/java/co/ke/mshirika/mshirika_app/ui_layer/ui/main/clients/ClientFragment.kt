package co.ke.mshirika.mshirika_app.ui_layer.ui.main.clients

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.FloatRange
import androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Client
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.LoanAccount
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Transaction
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Outcome.Error
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Outcome.Loading
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Urls
import co.ke.mshirika.mshirika_app.databinding.ContentLoansAndTransactionsBinding
import co.ke.mshirika.mshirika_app.databinding.FragmentClientBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.DetailsFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.main.clients.ClientFragmentDirections.Companion.actionGlobalLoanRepaymentFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.main.clients.ClientFragmentDirections.Companion.actionGlobalTransactionFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.main.clients.adapters.LoanAccountsAdapter
import co.ke.mshirika.mshirika_app.ui_layer.ui.main.clients.adapters.TransactionsAdapter
import co.ke.mshirika.mshirika_app.ui_layer.ui.main.clients.adapters.TransactionsAdapter.OnTransactionsItemClickListener
import co.ke.mshirika.mshirika_app.ui_layer.ui.main.clients.viewModels.ClientViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.Transitions.itemToDetailReentry
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.Transitions.itemToDetailsTransitionId
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.UIText
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class ClientFragment : DetailsFragment<FragmentClientBinding>(R.layout.fragment_client),
    co.ke.mshirika.mshirika_app.ui_layer.ui.loans.OnLoanClickListener,
    OnMenuItemClickListener,
    OnTransactionsItemClickListener {

    private val args by navArgs<ClientFragmentArgs>()
    private val viewModel by viewModels<ClientViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setClient(args.client)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemToDetailReentry(view)
        binding.fragment = this
        binding.apply {
            accounts()
            errorState()
            loadingState()
            setupAppBar()
            setupLoans()
            loansAndTransactions.setupTransactions()
            viewModel.apply {
                totalSavings.collectLatestLifeCycleNonNull { balance.text = it }
                client.collectLatestLifeCycleNonNull { loadImage(it) }
            }
        }
    }

    override val menuResId: Int
        get() = R.id.client

    override val toolbar: MaterialToolbar
        get() = binding.clientToolbar

    override val toolbarTitle: String
        get() = getString(R.string.client)

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

    private suspend fun loadImage(client: Client) {
        val authKey = viewModel.authKey()
        val headers = LazyHeaders
            .Builder()
            .addHeader("Authorization", authKey)
            .addHeader("Fineract-Platform-TenantId", "default").build()
        val url =
            GlideUrl(
                "${Urls.BASE_URL}clients/${client.id}/images",
                headers
            )
        Glide.with(requireActivity())
            .load(url)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .addListener(LoadImageListener())
            .into(binding.clientImage)
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

    private fun FragmentClientBinding.accounts() {
        viewModel.accounts.collectLatestLifecycle { outcome ->
            when (outcome) {
                is Loading -> true
                is Error -> {
                    Snackbar.make(root, outcome.msg, LENGTH_LONG).apply {
                        setAction(R.string.retry) {
                            viewModel.reload()
                        }
                        show()
                    }
                    false
                }
                else -> false
            }.also {
                progressHorizontal.isVisible = it
            }
        }
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
        viewModel.loans.collectLatestLifecycle {
            adapter.submitList(it)
        }
    }

    private fun ContentLoansAndTransactionsBinding.setupTransactions() {
        val adapter = TransactionsAdapter(this@ClientFragment)
        transactions.setHasFixedSize(true)
        transactions.adapter = adapter
        viewModel.transactions.collectLatestLifeCycleNonNull {
            adapter.submitList(it.transactions)
        }
    }

    override fun onLoanClicked(loanAccount: LoanAccount) {
        //todo view a loan
    }

    override fun onLoanRepayClicked(
        loanAccount: LoanAccount,
        position: Int,
        container: View
    ): Boolean {
        itemToDetailsTransitionId(
            actionGlobalLoanRepaymentFragment(), container to R.string.loan_card_transition_name
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

    inner class LoadImageListener : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            binding.loadBackupImage()
            return true
        }

        private fun FragmentClientBinding.loadBackupImage() {
            viewModel.client.collectLatestLifeCycleNonNull {
                clientImage.visibility = View.INVISIBLE
                clientImageError.apply {
                    isVisible = true
                    drawable(args.colors)
                    text = it.displayName[0].uppercase()
                    animate().apply {
                        scaleX(1f)
                        scaleY(1f)
                        duration = 200L
                        start()
                    }
                }
                bindClient(it)
            }
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            //bind all other details if the image has been loaded
            viewModel.client.collectLatestLifeCycleNonNull { client ->
                binding.bindClient(client)
            }
            return true
        }

        private fun FragmentClientBinding.bindClient(client: Client) {
            client.apply {
                clientName.text = displayName
                clientMobileNumber.text = mobileNo
                clientMembershipNo.text = externalId
            }
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
            //progress [0, 1]
            //say name = 36, bal = 24
            //if progress 0 textSize = name, bal
            // progress 1 textSize = 22, 16
            //diff = 14, 8
            binding.apply {
                clientName.textSize = textSize(45F, 22F, progress)
                balance.textSize = textSize(24F, 16F, progress)
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
