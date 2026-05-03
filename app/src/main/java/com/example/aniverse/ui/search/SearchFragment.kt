package com.example.aniverse.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aniverse.R
import com.example.aniverse.adapter.AnimeAdapter
import com.example.aniverse.databinding.FragmentSearchBinding
import com.example.aniverse.ui.detail.DetailActivity
import com.example.aniverse.viewmodel.SearchViewModel
import com.example.aniverse.viewmodel.ViewModelFactory
import com.example.aniverse.data.remote.ApiClient
import com.example.aniverse.data.local.AnimeDatabase
import com.example.aniverse.data.repository.AnimeRepository
import com.example.aniverse.util.Resource

import com.example.aniverse.data.model.GenreDetail
import com.google.android.material.chip.Chip

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AnimeAdapter
    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupRecyclerView()

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.etSearch.text.toString().trim()
                if (query.isNotEmpty()) {
                    viewModel.searchAnime(query)
                }
                true
            } else false
        }

        observeSearchResult()
        observeGenres()
    }

    private fun setupViewModel() {
        val apiService = ApiClient.instance
        val animeDao = AnimeDatabase.getDatabase(requireContext()).animeDao()
        val repository = AnimeRepository(apiService, animeDao)
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[SearchViewModel::class.java]
    }

    private fun setupRecyclerView() {
        adapter = AnimeAdapter { anime ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("ANIME_ID", anime.malId)
            startActivity(intent)
        }

        binding.recyclerSearch.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerSearch.adapter = adapter
    }

    private fun observeSearchResult() {
        viewModel.searchResult.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Show loading if needed
                }
                is Resource.Success -> {
                    val data = resource.data ?: emptyList()
                    adapter.submitList(data)
                    if (data.isEmpty() && binding.etSearch.text?.isNotEmpty() == true) {
                        Toast.makeText(requireContext(), getString(R.string.msg_empty_search), Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observeGenres() {
        viewModel.genres.observe(viewLifecycleOwner) { resource ->
            if (resource is Resource.Success) {
                populateGenreChips(resource.data ?: emptyList())
            }
        }
    }

    private fun populateGenreChips(genres: List<GenreDetail>) {
        binding.chipGroupGenres.removeAllViews()
        genres.take(15).forEach { genre ->
            val chip = Chip(requireContext()).apply {
                text = genre.name
                isCheckable = true
                setTextColor(resources.getColorStateList(R.color.white, null))
                setChipBackgroundColorResource(R.color.surface)
                setChipStrokeColorResource(R.color.accent)
                setChipStrokeWidth(2f)
                setOnClickListener {
                    viewModel.searchAnimeByGenre(genre.malId.toString())
                }
            }
            binding.chipGroupGenres.addView(chip)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}