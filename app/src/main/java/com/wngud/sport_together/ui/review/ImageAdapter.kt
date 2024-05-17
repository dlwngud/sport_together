package com.wngud.sport_together.ui.review

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wngud.sport_together.App
import com.wngud.sport_together.databinding.ItemReviewImageBinding
import com.wngud.sport_together.domain.model.Review

class ImageAdapter(private val review: Review, private val context: Context) :RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(private val binding: ItemReviewImageBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(str: String) {
            App.storage.reference.child(str).downloadUrl.addOnSuccessListener {
                Glide.with(context)
                    .load(it)
                    .centerCrop()
                    .into(binding.ivItemReviewImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemReviewImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun getItemCount(): Int = review.images.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(review.images[position])
    }
}