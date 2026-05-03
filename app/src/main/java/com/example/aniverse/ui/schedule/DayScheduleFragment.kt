package com.example.aniverse.ui.schedule

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.aniverse.adapter.AnimeAdapter
import com.example.aniverse.data.local.AnimeDatabase
import com.example.aniverse.data.remote.ApiClient
import com.example.aniverse.data.repository.AnimeRepository
import com.example.aniverse.databinding.FragmentDayScheduleBinding
import com.example.aniverse.ui.detail.DetailActivity
import com.example.aniverse.util.Resource
import com.example.aniverse.viewmodel.ScheduleViewModel
import com.example.aniverse.viewmodel.ViewModelFactory

class DayScheduleFragment : Fragment() {

    private var _binding: FragmentDayScheduleBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ScheduleViewModel
    private lateinit var adapter: AnimeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDayScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val day = arguments?.getString(ARG_DAY) ?: "monday"

        setupViewModel()
        setupRecyclerView()
        observeSchedule()

        viewModel.fetchSchedules(day)
    }

    private fun setupViewModel() {
        val apiService = ApiClient.instance
        val animeDao = AnimeDatabase.getDatabase(requireContext()).animeDao()
        val repository = AnimeRepository(apiService, animeDao)
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ScheduleViewModel::class.java]
    }

    private fun setupRecyclerView() {
        adapter = AnimeAdapter { anime ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("ANIME_ID", anime.malId)
            startActivity(intent)
        }
        binding.rvSchedule.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvSchedule.adapter = adapter
    }

    private fun observeSchedule() {
        viewModel.scheduleResult.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.submitList(resource.data ?: emptyList())
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_DAY = "day"
        fun newInstance(day: String) = DayScheduleFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_DAY, day)
            }
        }
    }
}