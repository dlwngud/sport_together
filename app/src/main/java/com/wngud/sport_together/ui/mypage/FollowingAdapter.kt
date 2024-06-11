package com.wngud.sport_together.ui.mypage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wngud.sport_together.App
import com.wngud.sport_together.R
import com.wngud.sport_together.databinding.ItemFollowBinding
import com.wngud.sport_together.domain.repository.UserRepository
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class FollowingAdapter @Inject constructor(
    @ActivityContext private val context: Context,
    private val userRepository: UserRepository
): ListAdapter<String, FollowingAdapter.FollowingViewHolder>(diffUtil) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    private lateinit var itemClickListener: onItemClickListener

    fun setItemClickListener(itemClickListener: onItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    inner class FollowingViewHolder(private val binding: ItemFollowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(uid: String) {
            coroutineScope.launch(Dispatchers.Main) {
                val user = userRepository.getUserInfo(uid)
                val isFollowing = userRepository.getFollowingStatus(uid)
                App.storage.reference.child(user.profileImage).downloadUrl.addOnSuccessListener {
                    Glide.with(context)
                        .load(it)
                        .placeholder(R.drawable.app_icon)
                        .error(R.drawable.app_icon)
                        .centerCrop()
                        .into(binding.ivFollow)
                }
                binding.run {
                    tvFollow.text = user.nickname
                    btnFollow.text = if(isFollowing) "✔ 팔로잉" else "팔로우"
                    btnFollow.setOnClickListener {
                        itemClickListener.onItemClick(position)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingViewHolder {
        val binding = ItemFollowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }
}