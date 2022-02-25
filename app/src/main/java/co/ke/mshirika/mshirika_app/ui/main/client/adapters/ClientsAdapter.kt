package co.ke.mshirika.mshirika_app.ui.main.client.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import co.ke.mshirika.mshirika_app.data.response.Client
import co.ke.mshirika.mshirika_app.databinding.ItemClientBinding
import co.ke.mshirika.mshirika_app.remote.Urls.BASE_URL
import co.ke.mshirika.mshirika_app.ui.main.client.OnClientItemClickListener
import co.ke.mshirika.mshirika_app.ui.main.utils.MyPagingDataAdapter
import co.ke.mshirika.mshirika_app.utility.ui.ViewUtils.drawable
import co.ke.mshirika.mshirika_app.utility.ui.ViewUtils.random
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class ClientsAdapter(
    private val authKey: String,
    private val listener: OnClientItemClickListener
) :
    MyPagingDataAdapter<Client, ClientsAdapter.ClientViewHolder>(Client) {

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bindToView(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ClientViewHolder(
        ItemClientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    inner class ClientViewHolder(private val binding: ItemClientBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener, RequestListener<Drawable> {

        private lateinit var client: Client

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (absoluteAdapterPosition != NO_POSITION)
                getItem(absoluteAdapterPosition)?.let {
                    listener.onClickClient(
                        it,
                        absoluteAdapterPosition,
                        colorMapping[absoluteAdapterPosition]!!
                    )
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
                    text = client.displayName[0].toString().uppercase()
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

        fun bindToView(client: Client) = client.bind()

        private fun Client.bind() {
            client = this
            binding.apply {

                val headers = LazyHeaders
                    .Builder()
                    .addHeader("Authorization", authKey)
                    .addHeader("Fineract-Platform-TenantId", "default").build()
                val url =
                    GlideUrl(
                        "$BASE_URL/fineract-provider/api/v1/clients/$id/images",
                        headers
                    )

                Glide.with(root)
                    .load(url)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .listener(this@ClientViewHolder)
                    .into(clientImage)
            }
        }

        private fun ItemClientBinding.bindToViews() {
            client.apply {
                clientName.text = displayName
                clientMembershipNumber.text = externalId
                clientStatus.text = subStatus.description
            }
        }
    }

    companion object {
        private const val TAG = "ClientAdapter"
    }
}