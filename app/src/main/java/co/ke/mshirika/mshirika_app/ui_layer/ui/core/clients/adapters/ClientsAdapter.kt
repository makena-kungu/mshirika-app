package co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.client.Client
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Urls
import co.ke.mshirika.mshirika_app.databinding.ItemClientBinding
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.OnClientItemClickListener
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.MshirikaPagingDataAdapter
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ClientsAdapter(
    scope: CoroutineScope,
    private val authKey: Flow<String?>,
    private val listener: OnClientItemClickListener
) :
    MshirikaPagingDataAdapter<Client, ClientsAdapter.ClientViewHolder>(Client) {
    private var _authKey: String? = null

    init {
        scope.launch(IO) {
            _authKey = authKey.first()!!
        }
    }

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ClientViewHolder(
        ItemClientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    inner class ClientViewHolder(private val binding: ItemClientBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener, RequestListener<Drawable> {

        private lateinit var _client: Client
        private var url: String? = null
        private var tempUrl: String? = null

        override fun onClick(v: View?) {
            if (v == null) return
            if (absoluteAdapterPosition == NO_POSITION) return
            val client = getItem(absoluteAdapterPosition) ?: return
            val colors = colorMapping[absoluteAdapterPosition]

            listener.onClickClient(
                containerView = v,
                client = client,
                colors = colors,
                imageUrl = url
            )
        }

        fun bind(client: Client) {
            _client = client

            val headers = LazyHeaders
                .Builder()
                .addHeader("Authorization", _authKey!!)
                .addHeader("Fineract-Platform-TenantId", "default").build()

            tempUrl = "${Urls.BASE_URL}clients/${client.id}/images"
            val glideUrl = GlideUrl(tempUrl, headers)
            Glide.with(binding.root)
                .load(glideUrl)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .listener(this)
                .into(binding.clientImage)
        }

        private fun ItemClientBinding.bindToViews() {
            _client.apply {
                clientName.text = displayName
                clientMembershipNumber.text = externalId
                clientStatus.text = subStatus.description
            }
            itemView.setOnClickListener(this@ClientViewHolder)
        }

        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean = binding.run {
            clientImageError.apply {
                isVisible = true
                text = _client.displayName[0].toString().uppercase()
                context.randomColors.let {
                    colorMapping[absoluteAdapterPosition] = it
                    drawable(it)
                }
            }
            clientImage.visibility = INVISIBLE
            bindToViews()
            false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            url = tempUrl
            binding.bindToViews()
            return false
        }
    }

    companion object {
        private const val TAG = "ClientAdapter"
    }
}