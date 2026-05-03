package com.example.aniverse.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aniverse.R
import com.example.aniverse.databinding.ItemAnimeBinding

// Data class sementara (nanti diganti model dari anggota 2)
data class AnimeItem(
    val malId: Int,
    val title: String,
    val score: Double,
    val imageUrl: String
)

class AnimeAdapter(
    private val onClick: (AnimeItem) -> Unit
) : RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder>() {

    private val items = mutableListOf<AnimeItem>()

    fun submitList(newItems: List<AnimeItem>) {
        android.util.Log.d("AnimeAdapter", "Submitting list with ${newItems.size} items")
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val binding = ItemAnimeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AnimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class AnimeViewHolder(private val binding: ItemAnimeBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AnimeItem) {
            binding.tvTitle.text = item.title
            binding.tvScore.text = item.score.toString()

            Glide.with(binding.root.context)
                .load(item.imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .centerCrop()
                .into(binding.imgPoster)

            binding.root.setOnClickListener { onClick(item) }
        }
    }
}