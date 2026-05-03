package com.example.aniverse.data.model

import com.example.aniverse.adapter.AnimeItem
import com.google.gson.annotations.SerializedName

data class AnimeResponse(
    val data: List<AnimeDetail>
)

data class SingleAnimeResponse(
    val data: AnimeDetail
)

data class AnimeDetail(
    @SerializedName("mal_id") val malId: Int,
    val title: String,
    @SerializedName("title_english") val titleEnglish: String?,
    @SerializedName("title_japanese") val titleJapanese: String?,
    val score: Double?,
    @SerializedName("scored_by") val scoredBy: Int?,
    val rank: Int?,
    val popularity: Int?,
    val favorites: Int?,
    val images: Images,
    val synopsis: String?,
    val genres: List<Genre>?,
    val status: String?,
    val episodes: Int?,
    val duration: String?,
    val rating: String?,
    val season: String?,
    val year: Int?,
    val source: String?,
    val trailer: Trailer?,
    val broadcast: Broadcast?,
    val producers: List<Producer>?,
    val studios: List<Studio>?,
    val background: String?
)

data class Images(val jpg: Jpg)
data class Jpg(@SerializedName("image_url") val imageUrl: String, @SerializedName("large_image_url") val largeImageUrl: String?)
data class Genre(
    @SerializedName("mal_id") val malId: Int,
    val name: String
)

data class GenreResponse(
    val data: List<GenreDetail>
)

data class GenreDetail(
    @SerializedName("mal_id") val malId: Int,
    val name: String,
    val count: Int? = null
)
data class Producer(val name: String)
data class Studio(val name: String)
data class Trailer(@SerializedName("youtube_id") val youtubeId: String?, val url: String?, @SerializedName("embed_url") val embedUrl: String?)
data class Broadcast(val string: String?)

data class CharacterResponse(
    val data: List<CharacterData>
)

data class CharacterData(
    val character: CharacterDetail,
    val role: String,
    @SerializedName("voice_actors") val voiceActors: List<VoiceActor>?
)

data class CharacterDetail(
    @SerializedName("mal_id") val malId: Int,
    val name: String,
    val images: Images?
)

data class VoiceActor(
    val person: PersonDetail,
    val language: String
)

data class PersonDetail(
    val name: String,
    val images: Images?
)

// Fungsi Extension untuk konversi ke AnimeItem
fun AnimeDetail.toAnimeItem() = AnimeItem(
    malId = this.malId,
    title = this.title,
    score = this.score ?: 0.0,
    imageUrl = this.images.jpg.imageUrl
)