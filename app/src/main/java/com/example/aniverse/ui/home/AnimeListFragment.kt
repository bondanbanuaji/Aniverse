package com.example.aniverse.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.aniverse.adapter.AnimeAdapter
import com.example.aniverse.databinding.FragmentAnimeListBinding
import com.example.aniverse.ui.detail.DetailActivity

class AnimeListFragment : Fragment() {

    private var _binding: FragmentAnimeListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AnimeAdapter
    private var type = "top"

    companion object {
        fun newInstance(type: String) = AnimeListFragment().apply {
            arguments = Bundle().apply { putString("type", type) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = arguments?.getString("type") ?: "top"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        // Nanti diisi ViewModel oleh anggota 2
        // Untuk sekarang tampilkan loading state saja
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        adapter = AnimeAdapter { anime ->
            // Navigate ke DetailActivity
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("ANIME_ID", anime.malId)
            startActivity(intent)
        }
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}