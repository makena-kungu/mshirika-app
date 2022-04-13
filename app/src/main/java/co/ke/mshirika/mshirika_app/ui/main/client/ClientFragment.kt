package co.ke.mshirika.mshirika_app.ui.main.client

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.View.INVISIBLE
import androidx.annotation.FloatRange
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data.response.Client
import co.ke.mshirika.mshirika_app.data.response.LoanAccount
import co.ke.mshirika.mshirika_app.data.response.SavingsAccount
import co.ke.mshirika.mshirika_app.data.response.Transaction
import co.ke.mshirika.mshirika_app.databinding.ContentLoansAndTransactionsBinding
import co.ke.mshirika.mshirika_app.databinding.FragmentClientBinding
import co.ke.mshirika.mshirika_app.remote.utils.Outcome.Error
import co.ke.mshirika.mshirika_app.remote.utils.Outcome.Loading
import co.ke.mshirika.mshirika_app.remote.utils.Urls
import co.ke.mshirika.mshirika_app.ui.main.client.adapters.LoanAccountsAdapter
import co.ke.mshirika.mshirika_app.ui.main.client.adapters.LoanAccountsAdapter.LoanClickListener
import co.ke.mshirika.mshirika_app.ui.main.client.adapters.SavingsAccountsAdapter.SavingsClickListener
import co.ke.mshirika.mshirika_app.ui.main.client.adapters.TransactionsAdapter
import co.ke.mshirika.mshirika_app.ui.main.client.adapters.TransactionsAdapter.OnTransactionsItemClickListener
import co.ke.mshirika.mshirika_app.ui.main.client.viewModels.ClientViewModel
import co.ke.mshirika.mshirika_app.ui.util.DetailsFragment
import co.ke.mshirika.mshirika_app.utility.ui.ViewUtils.drawable
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.abs

@AndroidEntryPoint
class ClientFragment : DetailsFragment<FragmentClientBinding>(R.layout.fragment_client),
    SavingsClickListener,
    LoanClickListener,
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

        binding.fragment = this
        binding.apply {
            setupAppBar()
            setupToolbar()
            lifecycleScope.launchWhenCreated {
                accounts()
                errorState()
                loadingState()
                setupLoans()
                loansAndTransactions.setupTransactions()
                viewModel.totalSavings.collectLatest { it?.let(balance::setText) }
                viewModel.client.collectLatest { it?.let(this@ClientFragment::loadImage) }
            }
        }
    }

    override val resId: Int
        get() = R.id.client

    override val toolbar: Toolbar
        get() = binding.clientToolbar

    override fun onMenuItemClick(item: MenuItem?): Boolean = item?.run {
        when (itemId) {
            R.id.edit -> {
                binding.setupToolbar()
                //edit the client
                TODO("NOT YET IMPLEMENTED")
            }
            else -> false
        }
    } ?: false

    private fun loadImage(client: Client) {
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

    private suspend fun errorState() {
        viewModel.errorState.collectLatest {
            Snackbar.make(
                binding.root,
                it.text(requireContext()),
                LENGTH_LONG
            ).show()
        }
    }

    private suspend fun loadingState() {
        viewModel.loadingState.collectLatest {
            binding.progressHorizontal.isVisible = it
        }
    }

    private suspend fun FragmentClientBinding.accounts() {
        viewModel.accounts.collectLatest { outcome ->
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

    private suspend fun FragmentClientBinding.setupLoans() {
        val adapter = LoanAccountsAdapter(this@ClientFragment)
        loansAndTransactions.loans.adapter = adapter
        viewModel.loans.collectLatest {
            adapter.submitList(it)
        }
    }

    private suspend fun ContentLoansAndTransactionsBinding.setupTransactions() {
        val adapter = TransactionsAdapter(this@ClientFragment)
        transactions.setHasFixedSize(true)
        transactions.adapter = adapter
        viewModel.transactions.collectLatest { response ->
            response?.run {
                adapter.submitList(transactions)
            }
        }
    }

    private fun FragmentClientBinding.setupToolbar() {
        with(clientToolbar) {
            // TODO: move this logic to the abstract custom class
            /*val navController = findNavController()
            val appBarConfiguration = AppBarConfiguration(navController.graph)
            setNavigationOnClickListener {
                //go back or pop up
                navController.navigateUp(appBarConfiguration)
            }*/

            /*inflateMenu(R.menu.client)
            setOnMenuItemClickListener(this@ClientFragment)*/
        }
    }

    override fun onSavingsClick(acc: SavingsAccount) {
        //view account
    }

    override fun onClickLoan(acc: LoanAccount) {
        TODO("Not yet implemented")
    }

    override fun onClickTransaction(transaction: Transaction) {
        TODO("Not yet implemented")
    }

    fun newSavingsAccount() {

    }

    fun newLoanAccount() {

    }

    fun payment() {

    }

    fun edit() {

    }

    fun charges() {

    }

    fun actions() {
        TODO("Display Bottom sheet with transfer and close actions")
    }

    inner class LoadImageListener : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            lifecycleScope.launchWhenCreated {
                binding.loadBackupImage()
            }
            return true
        }

        private suspend fun FragmentClientBinding.loadBackupImage() {
            viewModel.client.collectLatest {
                if (it == null) return@collectLatest

                clientImage.visibility = INVISIBLE
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
            lifecycleScope.launch {
                viewModel.client.collectLatest { client ->
                    client?.let { binding.bindClient(it) }
                }
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
