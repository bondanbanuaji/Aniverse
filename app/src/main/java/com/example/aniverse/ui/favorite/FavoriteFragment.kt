package com.example.aniverse.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.aniverse.adapter.AnimeAdapter
import com.example.aniverse.databinding.FragmentFavoriteBinding
import com.example.aniverse.ui.detail.DetailActivity

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AnimeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AnimeAdapter { anime ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("ANIME_ID", anime.malId)
            startActivity(intent)
        }

        binding.recyclerFavorite.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerFavorite.adapter = adapter

        // Nanti dihubungkan ke Room Database (anggota 2)
        binding.tvEmpty.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}