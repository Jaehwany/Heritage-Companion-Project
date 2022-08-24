package com.ssafy.heritage.adpter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.heritage.data.dto.Stamp
import com.ssafy.heritage.databinding.ItemFoundDetailBinding

private const val TAG = "StampDetailListAdapter___"

class StampDetailListAdapter() :
    ListAdapter<Stamp, StampDetailListAdapter.ViewHolder>(DiffCallback()) {

//    lateinit var categoryListClickListener: CategoryListClickListener

    inner class ViewHolder(private val binding: ItemFoundDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Stamp) = with(binding) {
            stamp = data
            Log.d(TAG, "bind: $data")

//            val resName = "@drawable/category_"+data.categorySeq.toString()
//            val packName = context.packageName
//            val resId = context.resources.getIdentifier(resName, "drawable", packName)
//            Glide.with(ivHeritage.context)
//                .load(resId)
//                .into(ivHeritage)
//            itemView.setOnClickListener {
//                categoryListClickListener.onClick(bindingAdapterPosition, data)
//            }

            // 내가 찾은 것들은 흐릿하게
//            if (data.found == 'Y') {
//                itemView.background = ContextCompat.getDrawable(itemView.context, R.color.black)
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemFoundDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Stamp>() {
        override fun areItemsTheSame(oldItem: Stamp, newItem: Stamp): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: Stamp, newItem: Stamp): Boolean {
            return oldItem == newItem
        }
    }
}