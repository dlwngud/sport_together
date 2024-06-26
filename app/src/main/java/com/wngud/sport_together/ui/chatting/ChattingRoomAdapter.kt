package com.wngud.sport_together.ui.chatting

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wngud.sport_together.App
import com.wngud.sport_together.R
import com.wngud.sport_together.databinding.ItemChatBinding
import com.wngud.sport_together.domain.model.ChattingRoom
import com.wngud.sport_together.domain.repository.UserRepository
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChattingRoomAdapter @Inject constructor(
    @ActivityContext private val context: Context,
    private val userRepository: UserRepository
) : ListAdapter<ChattingRoom, ChattingRoomAdapter.ChattingRoomViewHolder>(diffUtil) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

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
            coroutineScope.launch(Dispatchers.Main) {
                val counterUid = chattingRoom.users.find { it != App.auth.currentUser!!.uid }!!
                val counterUser = userRepository.getUserInfo(counterUid)
                binding.run {
                    App.storage.reference.child(counterUser.profileImage).downloadUrl.addOnSuccessListener {
                        Glide.with(context)
                            .load(it)
                            .placeholder(R.drawable.app_icon)
                            .error(R.drawable.app_icon)
                            .centerCrop()
                            .into(binding.ivProfileChat)
                    }
                    tvCountChat.visibility =
                        if (chattingRoom.unreadCount == 0) View.INVISIBLE else View.VISIBLE
                    tvContentChat.text = chattingRoom.lastChat
                    tvNicknameChat.text = counterUser.nickname
                }
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