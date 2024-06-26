package com.wngud.sport_together.ui.review

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wngud.sport_together.App
import com.wngud.sport_together.databinding.ItemReviewBinding
import com.wngud.sport_together.domain.model.Review
import com.wngud.sport_together.domain.repository.UserRepository
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReviewAdapter @Inject constructor(
    @ActivityContext private val context: Context,
    private val userRepository: UserRepository
) : ListAdapter<Review, ReviewAdapter.ReviewViewHolder>(DIFF_CALLBACK) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    private lateinit var itemClickListener: onItemClickListener

    fun setItemClickListener(itemClickListener: onItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    inner class ReviewViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {
            coroutineScope.launch(Dispatchers.Main) {
                val isFollowing = userRepository.getFollowingStatus(review.uid)
                binding.btnFollowItemReview.visibility = if(review.uid == App.auth.currentUser!!.uid) View.INVISIBLE
                else {
                    if(isFollowing) {
                        binding.btnFollowItemReview.text = "팔로잉 ✔"
                    } else {
                        binding.btnFollowItemReview.text = "팔로워"
                    }
                    View.VISIBLE
                }
            }

            App.storage.reference.child(review.profileImage).downloadUrl.addOnSuccessListener {
                Glide.with(context)
                    .load(it)
                    .centerCrop()
                    .into(binding.ivProfileItemReview)
            }
            binding.tvNicknameItemReview.text = review.nickname
            binding.tvDateItemReview.text = review.time
            binding.tvContentItemReview.text = review.content
            binding.rvImagesItemReview.run {
                val imageAdapter = ImageAdapter(review, context)
                adapter = imageAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
            binding.btnFollowItemReview.setOnClickListener {
                itemClickListener.onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding =
            ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Review>() {
            override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
                return oldItem.createTime == newItem.createTime
            }

            override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
                return oldItem == newItem
            }
        }
    }
}