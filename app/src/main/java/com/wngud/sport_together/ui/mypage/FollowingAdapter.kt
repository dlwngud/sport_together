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
import com.wngud.sport_together.domain.model.User

class FollowingAdapter(
    private val context: Context
): ListAdapter<User, FollowingAdapter.FollowingViewHolder>(diffUtil) {

    inner class FollowingViewHolder(private val binding: ItemFollowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
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
        val diffUtil = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.follower == newItem.follower
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }
}