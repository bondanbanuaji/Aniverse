package com.example.aniverse.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.aniverse.adapter.AnimeAdapter
import com.example.aniverse.databinding.FragmentAnimeListBinding
import com.example.aniverse.ui.detail.DetailActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

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

        // Ambil data dari Jikan API
        fetchData(type)
    }

    private fun fetchData(type: String) {
        binding.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Tentukan endpoint berdasarkan tipe tab
                val urlString = if (type == "top") {
                    "https://api.jikan.moe/v4/top/anime"
                } else {
                    "https://api.jikan.moe/v4/seasons/now"
                }

                // Ambil data JSON sebagai string
                val response = URL(urlString).readText()
                val jsonObject = JSONObject(response)
                val dataArray = jsonObject.getJSONArray("data")
                val animeList = mutableListOf<com.example.aniverse.adapter.AnimeItem>()

                // Looping data
                for (i in 0 until dataArray.length()) {
                    val item = dataArray.getJSONObject(i)
                    val id = item.getInt("mal_id")
                    val title = item.getString("title")
                    val score = item.optDouble("score", 0.0)
                    
                    val images = item.getJSONObject("images")
                    val jpg = images.getJSONObject("jpg")
                    val imageUrl = jpg.getString("image_url")
                    
                    animeList.add(com.example.aniverse.adapter.AnimeItem(id, title, score, imageUrl))
                }

                // Update UI di Main thread
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                    adapter.submitList(animeList)
                }

            } catch (e: Exception) {
                Log.e("AnimeListFragment", "Error fetching data", e)
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
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