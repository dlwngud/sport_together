package com.wngud.sport_together.ui.chatting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wngud.sport_together.databinding.ItemChatBinding

class ChattingAdapter : RecyclerView.Adapter<ChattingAdapter.ChattingViewHolder>() {

    inner class ChattingViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChattingViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChattingViewHolder(binding)
    }

    override fun getItemCount(): Int = 10

    override fun onBindViewHolder(holder: ChattingViewHolder, position: Int) {
        holder.bind()
    }
}