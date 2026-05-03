package com.example.aniverse.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aniverse.R
import com.example.aniverse.data.model.CharacterData
import com.example.aniverse.databinding.ItemCasterBinding

class CasterAdapter : RecyclerView.Adapter<CasterAdapter.CasterViewHolder>() {

    private val characters = mutableListOf<CharacterData>()

    fun setData(newList: List<CharacterData>) {
        characters.clear()
        characters.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CasterViewHolder {
        val binding = ItemCasterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CasterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CasterViewHolder, position: Int) {
        holder.bind(characters[position])
    }

    override fun getItemCount(): Int = characters.size

    class CasterViewHolder(private val binding: ItemCasterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CharacterData) {
            // Get Japanese Voice Actor if available
            val voiceActor = data.voiceActors?.find { it.language == "Japanese" }
                ?: data.voiceActors?.firstOrNull()

            binding.tvCasterName.text = voiceActor?.person?.name ?: "Unknown"
            binding.tvCasterRole.text = data.character.name
            
            val imageUrl = voiceActor?.person?.images?.jpg?.imageUrl 
                ?: data.character.images?.jpg?.imageUrl
            
            Glide.with(binding.root.context)
                .load(imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .circleCrop()
                .into(binding.imgCaster)
        }
    }
}