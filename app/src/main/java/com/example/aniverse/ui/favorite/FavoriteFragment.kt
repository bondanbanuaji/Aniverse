package com.example.aniverse.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.aniverse.adapter.AnimeAdapter
import com.example.aniverse.data.local.AnimeDatabase
import com.example.aniverse.data.remote.ApiClient
import com.example.aniverse.data.repository.AnimeRepository
import com.example.aniverse.databinding.FragmentFavoriteBinding
import com.example.aniverse.ui.detail.DetailActivity
import com.example.aniverse.viewmodel.FavoriteViewModel
import com.example.aniverse.viewmodel.ViewModelFactory

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AnimeAdapter
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupRecyclerView()
        observeFavorites()
    }

    private fun setupViewModel() {
        val apiService = ApiClient.instance
        val animeDao = AnimeDatabase.getDatabase(requireContext()).animeDao()
        val repository = AnimeRepository(apiService, animeDao)
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]
    }

    private fun setupRecyclerView() {
        adapter = AnimeAdapter { anime ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("ANIME_ID", anime.malId)
            startActivity(intent)
        }

        binding.recyclerFavorite.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerFavorite.adapter = adapter
    }

    private fun observeFavorites() {
        viewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            if (favorites.isNullOrEmpty()) {
                binding.tvEmpty.visibility = View.VISIBLE
                binding.recyclerFavorite.visibility = View.GONE
                adapter.submitList(emptyList())
            } else {
                binding.tvEmpty.visibility = View.GONE
                binding.recyclerFavorite.visibility = View.VISIBLE
                adapter.submitList(viewModel.getFavoriteItems(favorites))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}