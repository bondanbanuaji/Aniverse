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

        // Inisialisasi ViewModel (Backend lo)
        setupViewModel()

        // Inisialisasi Adapter sesuai kontrak Anggota 1
        setupRecyclerView()

        // Trigger search saat user tekan tombol search di keyboard
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.etSearch.text.toString().trim()
                if (query.isNotEmpty()) {
                    // Panggil fungsi search di ViewModel lo
                    viewModel.searchAnime(query)
                }
                true
            } else false
        }

        // Observe hasil pencarian dari API Jikan
        observeSearchResult()
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
            // Navigasi ke DetailActivity dengan ID yang benar
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("ANIME_ID", anime.malId)
            startActivity(intent)
        }

        // Gunakan ID recyclerSearch dari layout xml[cite: 4]
        binding.recyclerSearch.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerSearch.adapter = adapter
    }

    private fun observeSearchResult() {
        viewModel.searchResult.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Bisa tambahkan progressBar visibility jika diperlukan
                }
                is Resource.Success -> {
                    // Masukkan data ke adapter Anggota 1[cite: 1]
                    val data = resource.data ?: emptyList()
                    adapter.submitList(data)
                    if (data.isEmpty()) {
                        Toast.makeText(requireContext(), getString(R.string.msg_empty_search), Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}