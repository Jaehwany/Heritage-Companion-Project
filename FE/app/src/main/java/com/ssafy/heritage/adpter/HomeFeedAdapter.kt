package com.ssafy.heritage.adpter

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.heritage.data.dto.Heritage
import com.ssafy.heritage.data.remote.response.FeedListResponse
import com.ssafy.heritage.databinding.ItemHeritageBinding
import com.ssafy.heritage.databinding.ItemHomeBinding
import com.ssafy.heritage.databinding.ItemHomeFeedBinding
import com.ssafy.heritage.listener.FeedListClickListener
import com.ssafy.heritage.listener.HeritageListClickListener

class HomeFeedAdapter : ListAdapter<FeedListResponse, HomeFeedAdapter.ViewHolder>(DiffCallback()) {

    lateinit var feedListClickListener: FeedListClickListener

    inner class ViewHolder(private val binding: ItemHomeFeedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: FeedListResponse) = with(binding) {
            feed = data
            if(data.hashtag.isNotEmpty()){
                binding.tvHashtag1.text = "# " + data.hashtag[0]
                binding.tvHashtag2.text = "# " + data.hashtag[1]
            }
            ViewCompat.setTransitionName(ivFeed, "feed$bindingAdapterPosition")
            binding.root.setOnClickListener {
                feedListClickListener.onClick(bindingAdapterPosition, data, ivFeed)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHomeFeedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<FeedListResponse>() {
        override fun areItemsTheSame(oldItem: FeedListResponse, newItem: FeedListResponse): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: FeedListResponse, newItem: FeedListResponse): Boolean {
            return oldItem == newItem
        }
    }
}