package com.wngud.sport_together.ui.chatting

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wngud.sport_together.databinding.ItemChatBinding
import com.wngud.sport_together.domain.model.ChattingRoom

class ChattingRoomAdapter :
    ListAdapter<ChattingRoom, ChattingRoomAdapter.ChattingRoomViewHolder>(diffUtil) {

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    private lateinit var itemClickListener: onItemClickListener

    fun setItemClickListener(itemClickListener: onItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    inner class ChattingRoomViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chattingRoom: ChattingRoom) {
            binding.run {
                tvCountChat.visibility =
                    if (chattingRoom.unreadCount == 0) View.INVISIBLE else View.VISIBLE
                tvContentChat.text = chattingRoom.lastChat
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChattingRoomViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChattingRoomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChattingRoomViewHolder, position: Int) {
        Log.i("viewHolder", getItem(position).users.toString())
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(position)
        }
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ChattingRoom>() {
            override fun areItemsTheSame(oldItem: ChattingRoom, newItem: ChattingRoom): Boolean {
                return oldItem.roomId == newItem.roomId
            }

            override fun areContentsTheSame(oldItem: ChattingRoom, newItem: ChattingRoom): Boolean {
                return oldItem == newItem
            }
        }
    }
}