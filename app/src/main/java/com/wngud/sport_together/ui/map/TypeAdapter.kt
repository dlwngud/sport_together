package com.wngud.sport_together.ui.map

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wngud.sport_together.databinding.ItemTypeMapBinding

class TypeAdapter(private val context: Context, private val typeList: List<ExerciseType>) :
    RecyclerView.Adapter<TypeAdapter.TypeViewHolder>() {

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    private lateinit var itemClickListener: onItemClickListener

    fun setItemClickListener(itemClickListener: onItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    inner class TypeViewHolder(private val binding: ItemTypeMapBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(exerciseType: ExerciseType) {
            binding.tvItemType.text = exerciseType.type
            Glide.with(context)
                .load(exerciseType.image)
                .into(binding.ivItemType)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
        val binding = ItemTypeMapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TypeViewHolder(binding)
    }

    override fun getItemCount(): Int = typeList.size


    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(position)
        }
        holder.bind(typeList[position])
    }
}