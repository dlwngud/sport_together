package com.wngud.sport_together.ui.review

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wngud.sport_together.App
import com.wngud.sport_together.databinding.ItemReviewBinding
import com.wngud.sport_together.domain.model.Review
import javax.inject.Inject

class ReviewAdapter @Inject constructor(
    private val context: Context
) : ListAdapter<Review, ReviewAdapter.ReviewViewHolder>(DIFF_CALLBACK) {

    inner class ReviewViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {
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
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
                return oldItem.reviewId == newItem.reviewId
            }
        }
    }
}