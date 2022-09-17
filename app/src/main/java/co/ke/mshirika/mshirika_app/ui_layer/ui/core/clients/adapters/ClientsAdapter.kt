package co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import co.ke.mshirika.mshirika_app.GlideApp
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.Cliente
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Urls
import co.ke.mshirika.mshirika_app.databinding.ItemClientBinding
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.OnClientItemClickListener
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.MshirikaPagingDataAdapter
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ClientsAdapter(
    private val scope: CoroutineScope,
    private val authKey: Flow<String?>,
    private val listener: OnClientItemClickListener
) : MshirikaPagingDataAdapter<Cliente, ClientsAdapter.ClientViewHolder>(Cliente) {
    private var _authKey: String? = null

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

        private lateinit var _client: Cliente
        private var url: String? = null
        private var tempUrl: String? = null
        private var color = 0

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

        fun bind(client: Cliente) {
            _client = client
            binding.cliente = client
            val listener = this
            itemView.setOnClickListener(listener)

            if (!client.hasImage) {
                loadPlaceHolderImage()
                return
            }
            binding.clientImageError.isVisible = false

            scope.launch {
                if (_authKey == null) {
                    _authKey = authKey.first()!!
                }

                val headers = LazyHeaders
                    .Builder()
                    .addHeader("Authorization", _authKey!!)
                    .addHeader("Fineract-Platform-TenantId", "default").build()

                tempUrl = "${Urls.BASE_URL}clients/${client.id}/images"
                val glideUrl = GlideUrl(tempUrl, headers)
                GlideApp.with(binding.root)
                    .load(glideUrl)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .listener(listener)
                    .into(binding.clientImage)
            }
        }

        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            loadPlaceHolderImage()
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            url = tempUrl
            return false
        }

        private fun loadPlaceHolderImage() {
            binding.apply {
                clientImageError.apply {
                    isVisible = true
                    text = _client.displayName[0].toString().uppercase()
                    setBackgroundColor(_client.color)
                    /*context.randomColors.let {
                        colorMapping[absoluteAdapterPosition] = it
                        drawable(it)
                    }*/
                }
                clientImage.visibility = View.INVISIBLE
            }
        }
    }

    companion object {
        private const val TAG = "ClientAdapter"
    }
}