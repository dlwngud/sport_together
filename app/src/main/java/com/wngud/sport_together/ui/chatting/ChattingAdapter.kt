package com.wngud.sport_together.ui.chatting

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wngud.sport_together.App
import com.wngud.sport_together.R
import com.wngud.sport_together.databinding.ItemCounterChattingBinding
import com.wngud.sport_together.databinding.ItemMyChattingBinding
import com.wngud.sport_together.domain.model.Chatting
import com.wngud.sport_together.domain.repository.UserRepository
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChattingAdapter @Inject constructor(
    @ActivityContext private val context: Context,
    private val userRepository: UserRepository
) : ListAdapter<Chatting, RecyclerView.ViewHolder>(diffUtil) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    inner class MyChattingViewHolder(private val binding: ItemMyChattingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chatting: Chatting) {
            binding.run {
                tvContentMyChatting.text = chatting.content
            }
        }
    }

    inner class OtherChattingViewHolder(private val binding: ItemCounterChattingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chatting: Chatting) {
            binding.run {
                coroutineScope.launch(Dispatchers.Main) {
                    val counterUser = userRepository.getUserInfo(chatting.senderId)
                    App.storage.reference.child(counterUser.profileImage).downloadUrl.addOnSuccessListener {
                        Glide.with(context)
                            .load(it)
                            .placeholder(R.drawable.app_icon)
                            .error(R.drawable.app_icon)
                            .centerCrop()
                            .into(binding.ivProfileCounterChatting)
                    }

                    tvContentCounterChatting.text = chatting.content
                    tvNicknameCounterChatting.text = counterUser.nickname
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == MY_CHAT) {
            MyChattingViewHolder(
                ItemMyChattingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            OtherChattingViewHolder(
                ItemCounterChattingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MY_CHAT) {
            Log.i("viewHolder1", getItem(position).content)
            (holder as MyChattingViewHolder).bind(getItem(position))
        } else {
            Log.i("viewHolder2", getItem(position).content)
            (holder as OtherChattingViewHolder).bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (App.auth.currentUser!!.uid == getItem(position).senderId) MY_CHAT
        else OTHER_CHAT
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Chatting>() {
            override fun areItemsTheSame(oldItem: Chatting, newItem: Chatting): Boolean {
                return oldItem.content == newItem.content
            }

            override fun areContentsTheSame(oldItem: Chatting, newItem: Chatting): Boolean {
                return oldItem == newItem
            }
        }
        private const val MY_CHAT = 1
        private const val OTHER_CHAT = 2
    }
}