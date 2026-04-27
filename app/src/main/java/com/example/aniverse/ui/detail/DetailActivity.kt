package com.example.aniverse.ui.detail

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aniverse.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tombol back di ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detail Anime"

        val animeId = intent.getIntExtra("ANIME_ID", -1)
        // Nanti panggil ViewModel dari anggota 2 berdasarkan animeId

        // Sementara: placeholder UI
        binding.tvTitleDetail.text = "Loading..."
        binding.tvScoreDetail.text = "⭐ -"

        binding.btnFavorite.setOnClickListener {
            // Nanti dihubungkan ke Room DAO (anggota 2)
            Toast.makeText(this, "Added to Favorite!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}