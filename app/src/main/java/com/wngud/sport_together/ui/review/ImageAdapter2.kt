package com.wngud.sport_together.ui.review

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wngud.sport_together.databinding.ItemAddReviewImageBinding

class ImageAdapter2(private val uris: List<@JvmSuppressWildcards Uri>, private val context: Context) :RecyclerView.Adapter<ImageAdapter2.ImageViewHolder2>() {

    inner class ImageViewHolder2(private val binding: ItemAddReviewImageBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("CheckResult")
        fun bind(uri: @JvmSuppressWildcards Uri) {
            Glide.with(context)
                .load(uri)
                .centerCrop()
                .into(binding.ivItemAddReviewImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder2 {
        val binding = ItemAddReviewImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder2(binding)
    }

    override fun getItemCount(): Int = uris.size

    override fun onBindViewHolder(holder: ImageViewHolder2, position: Int) {
        holder.bind(uris[position])
    }
}