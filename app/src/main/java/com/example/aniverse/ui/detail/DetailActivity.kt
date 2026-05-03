package com.example.aniverse.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.example.aniverse.R
import com.example.aniverse.adapter.CasterAdapter
import com.example.aniverse.data.local.AnimeDatabase
import com.example.aniverse.data.model.AnimeDetail
import com.example.aniverse.data.model.AnimeFavorite
import com.example.aniverse.data.remote.ApiClient
import com.example.aniverse.data.repository.AnimeRepository
import com.example.aniverse.databinding.ActivityDetailBinding
import com.example.aniverse.util.Resource
import com.example.aniverse.viewmodel.DetailViewModel
import com.example.aniverse.viewmodel.ViewModelFactory
import com.google.android.material.chip.Chip

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var casterAdapter: CasterAdapter
    private var currentAnime: AnimeDetail? = null
    private var isFavoriteAnime = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationOnClickListener { finish() }

        val animeId = intent.getIntExtra("ANIME_ID", -1)

        setupViewModel()
        setupRecyclerView()

        if (animeId != -1) {
            viewModel.getAnimeDetail(animeId)
            viewModel.checkFavoriteStatus(animeId)
        }

        observeDetailData()
        setupFavoriteButton()
    }

    private fun setupViewModel() {
        val apiService = ApiClient.instance
        val animeDao = AnimeDatabase.getDatabase(this).animeDao()
        val repository = AnimeRepository(apiService, animeDao)
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]
    }

    private fun setupRecyclerView() {
        casterAdapter = CasterAdapter()
        binding.rvCasters.adapter = casterAdapter
    }

    private fun observeDetailData() {
        viewModel.animeDetail.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let { anime ->
                        currentAnime = anime
                        populateData(anime)
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(this, resource.message ?: "Error", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }

        viewModel.characters.observe(this) { resource ->
            if (resource is Resource.Success) {
                resource.data?.let { casterAdapter.setData(it) }
            }
        }

        viewModel.isFavorite.observe(this) { isFav ->
            isFavoriteAnime = isFav
            updateFavoriteIcon(isFav)
        }
    }

    private fun populateData(anime: AnimeDetail) {
        binding.apply {
            tvTitleDetail.text = anime.title
            tvScoreDetail.text = getString(R.string.ratings, anime.score?.toString() ?: "0.0")
            tvFavorites.text = getString(R.string.favorites_count, anime.favorites?.toString() ?: "0")
            tvRank.text = getString(R.string.rank_label, anime.rank?.toString() ?: "-")
            tvPopularity.text = getString(R.string.popularity_label, anime.popularity?.toString() ?: "-")
            tvSynopsis.text = anime.synopsis ?: getString(R.string.no_synopsis)

            // In-App YouTube Trailer
            val youtubeId = anime.trailer?.youtubeId ?: extractYoutubeId(anime.trailer?.embedUrl)
            if (!youtubeId.isNullOrEmpty()) {
                youtubePlayerView.visibility = View.VISIBLE
                cardTrailer.visibility = View.GONE
                tvTrailerLabel.visibility = View.VISIBLE

                lifecycle.addObserver(youtubePlayerView)
                youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.cueVideo(youtubeId, 0f)
                    }

                    override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {
                        if (error == PlayerConstants.PlayerError.VIDEO_NOT_FOUND || error == PlayerConstants.PlayerError.UNKNOWN) {
                            youtubePlayerView.visibility = View.GONE
                            cardTrailer.visibility = View.VISIBLE
                            
                            // Load thumbnail as fallback
                            val thumbnailUrl = "https://img.youtube.com/vi/$youtubeId/0.jpg"
                            Glide.with(this@DetailActivity)
                                .load(thumbnailUrl)
                                .into(binding.imgTrailerThumbnail)

                            binding.cardTrailer.setOnClickListener {
                                val intent = Intent(Intent.ACTION_VIEW, 
                                    Uri.parse("https://www.youtube.com/watch?v=$youtubeId"))
                                startActivity(intent)
                            }
                        }
                    }
                })
            } else {
                youtubePlayerView.visibility = View.GONE
                cardTrailer.visibility = View.GONE
                tvTrailerLabel.visibility = View.GONE
            }

            // Info Chips with localized strings
            chipGroupInfo.removeAllViews()
            addInfoChip(chipGroupInfo, getString(R.string.status_format, anime.status ?: "-"))
            addInfoChip(chipGroupInfo, getString(R.string.episodes_format, anime.episodes?.toString() ?: "-"))
            addInfoChip(chipGroupInfo, getString(R.string.duration_format, anime.duration ?: "-"))
            addInfoChip(chipGroupInfo, getString(R.string.rating_format, anime.rating ?: "-"))
            addInfoChip(chipGroupInfo, getString(R.string.season_format, anime.season ?: "-", anime.year?.toString() ?: ""))
            addInfoChip(chipGroupInfo, getString(R.string.source_format, anime.source ?: "-"))

            chipGroupGenres.removeAllViews()
            anime.genres?.forEach { addInfoChip(chipGroupGenres, it.name) }

            chipGroupStudios.removeAllViews()
            anime.studios?.forEach { addInfoChip(chipGroupStudios, it.name) }
            anime.producers?.forEach { addInfoChip(chipGroupStudios, it.name) }

            Glide.with(this@DetailActivity).load(anime.images.jpg.imageUrl).into(imgPosterDetail)
        }
    }

    private fun addInfoChip(chipGroup: com.google.android.material.chip.ChipGroup, text: String) {
        val chip = Chip(this)
        chip.text = text
        chip.setTextColor(getColor(R.color.white))
        chip.setChipBackgroundColorResource(R.color.primary)
        chipGroup.addView(chip)
    }

    private fun setupFavoriteButton() {
        binding.btnFavorite.setOnClickListener {
            currentAnime?.let { anime ->
                val favAnime = AnimeFavorite(
                    malId = anime.malId,
                    title = anime.title,
                    score = anime.score ?: 0.0,
                    imageUrl = anime.images.jpg.imageUrl
                )
                if (isFavoriteAnime) {
                    viewModel.removeFromFavorite(favAnime)
                    Toast.makeText(this, getString(R.string.msg_removed_favorite), Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.addToFavorite(favAnime)
                    Toast.makeText(this, getString(R.string.msg_added_favorite), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateFavoriteIcon(isFav: Boolean) {
        val icon = if (isFav) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off
        binding.btnFavorite.setIconResource(icon)
        binding.btnFavorite.text = if (isFav) getString(R.string.in_watchlist) else getString(R.string.add_to_watchlist)
        
        // UI Fix: Tambah stroke/border agar tombol terlihat jelas
        binding.btnFavorite.strokeWidth = 4
        binding.btnFavorite.setStrokeColorResource(if (isFav) R.color.accent else R.color.white)
        binding.btnFavorite.setTextColor(if (isFav) getColor(R.color.accent) else getColor(R.color.white))
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.youtubePlayerView.release()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun extractYoutubeId(url: String?): String? {
        if (url == null) return null
        val pattern = "(?<=embed/|v/|vi/|watch\\?v=|&v=)[^#&?]*"
        val matcher = java.util.regex.Pattern.compile(pattern).matcher(url)
        return if (matcher.find()) matcher.group() else null
    }
}