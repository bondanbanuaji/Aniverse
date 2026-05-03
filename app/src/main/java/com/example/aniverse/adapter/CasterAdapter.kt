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
            binding.tvCasterName.text = data.character.name
            binding.tvCasterRole.text = data.role
            
            val imageUrl = data.character.images?.jpg?.imageUrl
            
            Glide.with(binding.root.context)
                .load(imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(binding.imgCaster)
        }
    }
}