package co.ke.mshirika.mshirika_app.ui.main.client

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.View.INVISIBLE
import androidx.annotation.FloatRange
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data.response.Client
import co.ke.mshirika.mshirika_app.data.response.LoanAccount
import co.ke.mshirika.mshirika_app.data.response.SavingsAccount
import co.ke.mshirika.mshirika_app.databinding.FragmentClientBinding
import co.ke.mshirika.mshirika_app.remote.Urls
import co.ke.mshirika.mshirika_app.ui.main.client.adapters.LoanAccountsAdapter
import co.ke.mshirika.mshirika_app.ui.main.client.adapters.LoanAccountsAdapter.LoanClickListener
import co.ke.mshirika.mshirika_app.ui.main.client.adapters.SavingsAccountsAdapter.SavingsClickListener
import co.ke.mshirika.mshirika_app.ui.main.client.viewModels.ClientViewModel
import co.ke.mshirika.mshirika_app.utility.network.Status.*
import co.ke.mshirika.mshirika_app.utility.ui.ViewUtils.amt
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
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.abs

@AndroidEntryPoint
class ClientFragment : Fragment(R.layout.fragment_client),
    SavingsClickListener,
    LoanClickListener,
    Toolbar.OnMenuItemClickListener {

    private var _binding: FragmentClientBinding? = null

    private val binding: FragmentClientBinding get() = _binding!!
    private val viewModel by viewModels<ClientViewModel>()
    private val args by navArgs<ClientFragmentArgs>()
    private val _activity by lazy {
        requireActivity() as AppCompatActivity
    }
    private val lifecycleScope get() = viewLifecycleOwner.lifecycleScope


    @Inject
    lateinit var authKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setClient(args.client)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentClientBinding.bind(view)

        binding.fragment = this
        binding.apply {
            clientToolbar.setupToolbar()

            lifecycleScope.launchWhenCreated {
                viewModel.accounts.collectLatest { resource ->
                    when (resource.status) {
                        SUCCESS -> {
                            resource.data?.let { response ->
                                //do some calculations
                                response.savingsAccounts
                                    .sumOf { account ->
                                        account.accountBalance
                                    }.also { sum ->
                                        availableBal.text = sum.amt
                                    }

                                response.loanAccounts.forEach { loanAccount ->
                                    //query the repayment schedule
                                    viewModel.query(loanAccount)
                                    withContext(IO) {
                                        viewModel.query(loanAccount).run {
                                            when (status) {
                                                SUCCESS -> data?.let {
                                                    viewModel.save(it)
                                                }
                                                ERROR -> {
                                                    message?.let {
                                                        Snackbar.make(
                                                            root,
                                                            it,
                                                            LENGTH_SHORT
                                                        ).show()
                                                    }

                                                }
                                                else -> {}
                                            }

                                        }
                                    }
                                }
                            }
                            false
                        }
                        ERROR -> {
                            resource.message?.let {
                                Snackbar.make(root, it, LENGTH_LONG).apply {
                                    setAction(R.string.retry) {
                                        viewModel.reload()
                                    }
                                    show()
                                }
                            }
                            false
                        }
                        LOADING -> {
                            //display a
                            true
                        }
                        else -> null
                    }?.also {
                        //show or hide a progress bar depending on this value
                        progressBar.isVisible = it
                    }
                }
                viewModel.client.collectLatest { client ->
                    client?.let { loadImage(it) }
                }
            }

            AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                val scrollRange = appBarLayout.totalScrollRange.toFloat()
                val percent = abs(verticalOffset) / scrollRange
                constraintLayout.progress = percent
                constraintLayout.addTransitionListener(AppBarTransition())
            }.also {
                appBarLayout.addOnOffsetChangedListener(it)
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean = item?.run {
        when (itemId) {
            R.id.edit -> {
                //edit the client
                true
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
                "${Urls.BASE_URL}/fineract-provider/api/v1/clients/${client.id}/images",
                headers
            )
        Glide.with(requireActivity())
            .load(url)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .addListener(LoadImageListener())
            .into(binding.clientImage)
    }

    private fun MaterialToolbar.setupToolbar() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setNavigationOnClickListener {
            //go back or pop up
            navController.navigateUp(appBarConfiguration)
        }

        inflateMenu(R.menu.client)
        setOnMenuItemClickListener(this@ClientFragment)
    }

    override fun savingsClicked(acc: SavingsAccount) {
        //view account
    }

    override fun loanClicked(acc: LoanAccount) {
        //make payment for the loan

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class LoadImageListener : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            binding.apply {
                lifecycleScope.launch {
                    viewModel.client.collectLatest { client ->
                        client?.let {
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
                }
            }
            return true
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
                    client?.let {
                        binding.bindClient(it)
                    }
                }
            }
            return true
        }

        private fun FragmentClientBinding.bindClient(client: Client) {
            val loanAccountsAdapter = LoanAccountsAdapter(this@ClientFragment)
            viewModel.accounts

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