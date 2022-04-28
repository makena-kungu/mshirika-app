package co.ke.mshirika.mshirika_app.ui_layer.ui.main.clients.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Client
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Urls
import co.ke.mshirika.mshirika_app.databinding.ItemClientBinding
import co.ke.mshirika.mshirika_app.ui_layer.ui.main.clients.OnClientItemClickListener
import co.ke.mshirika.mshirika_app.ui_layer.ui.main.utils.MyPagingDataAdapter
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.drawable
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.random
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
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
) :
    MyPagingDataAdapter<Client, ClientsAdapter.ClientViewHolder>(Client) {

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
        private lateinit var url: String

        override fun onClick(v: View?) {
            if (absoluteAdapterPosition != NO_POSITION)
                getItem(absoluteAdapterPosition)?.let { client ->
                    v?.let {
                        listener.onClickClient(
                            containerView = it,
                            client = client,
                            colors = colorMapping[absoluteAdapterPosition]!!,
                            imageUrl = url
                        )
                    }
                }
        }

        fun bind(client: Client) {
            _client = client
            url = "${Urls.BASE_URL}clients/${client.id}/images"
            itemView.setOnClickListener(this)

            scope.launch {
                val authKey = authKey.first()!!
                val headers = LazyHeaders
                    .Builder()
                    .addHeader("Authorization", authKey)
                    .addHeader("Fineract-Platform-TenantId", "default").build()

                val glideUrl =
                    GlideUrl(
                        url,
                        headers
                    )

                Glide.with(binding.root)
                    .load(glideUrl)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .listener(this@ClientViewHolder)
                    .into(binding.clientImage)
            }
        }

        private fun ItemClientBinding.bindToViews() {
            _client.apply {
                clientName.text = displayName
                clientMembershipNumber.text = externalId
                clientStatus.text = subStatus.description
            }
        }

        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            binding.apply {
                clientImageError.apply {
                    isVisible = true
                    text = _client.displayName[0].toString().uppercase()
                    context.random.let {
                        colorMapping[absoluteAdapterPosition] = it
                        drawable(it)
                    }
                }
                clientImage.visibility = INVISIBLE
                bindToViews()
            }
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean = binding.run {
            bindToViews()
            return false
        }
    }

    companion object {
        private const val TAG = "ClientAdapter"
    }
}