package com.example.aniverse.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.aniverse.R
import com.example.aniverse.adapter.AnimeAdapter
import com.example.aniverse.data.local.AnimeDatabase
import com.example.aniverse.data.remote.ApiClient
import com.example.aniverse.data.repository.AnimeRepository
import com.example.aniverse.databinding.FragmentAnimeListBinding
import com.example.aniverse.ui.detail.DetailActivity
import com.example.aniverse.util.Resource
import com.example.aniverse.viewmodel.HomeViewModel
import com.example.aniverse.viewmodel.ViewModelFactory

class AnimeListFragment : Fragment() {

    private var _binding: FragmentAnimeListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel
    private lateinit var animeAdapter: AnimeAdapter
    private var type = "top"

    companion object {
        fun newInstance(type: String) = AnimeListFragment().apply {
            arguments = Bundle().apply {
                putString("type", type)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getString("type") ?: "top"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        android.util.Log.d("AnimeListFragment", "onViewCreated for type: $type")

        setupRecyclerView()
        setupViewModel()
        observeAnimeData()

        // Ambil data dari API
        viewModel.fetchAnime(type)
    }

    private fun setupViewModel() {
        val apiService = ApiClient.instance
        val animeDao = AnimeDatabase.getDatabase(requireContext()).animeDao()
        val repository = AnimeRepository(apiService, animeDao)
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
    }

    private fun setupRecyclerView() {
        animeAdapter = AnimeAdapter { anime ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("ANIME_ID", anime.malId)
            startActivity(intent)
        }

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = animeAdapter
            setHasFixedSize(true)
        }
    }

    private fun observeAnimeData() {
        viewModel.animeList.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                    binding.tvError.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    android.util.Log.d("AnimeListFragment", "Data received for $type: ${resource.data?.size} items")
                    if (resource.data.isNullOrEmpty()) {
                        binding.tvError.visibility = View.VISIBLE
                        binding.tvError.text = getString(R.string.msg_empty_search)
                        binding.recyclerView.visibility = View.GONE
                    } else {
                        binding.tvError.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
                        animeAdapter.submitList(resource.data)
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerView.visibility = View.GONE
                    binding.tvError.visibility = View.VISIBLE
                    binding.tvError.text = "Error: ${resource.message}"
                    android.util.Log.e("AnimeListFragment", "API Error: ${resource.message}")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}