package com.example.playlistmaker

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }

        val track = if (SDK_INT >= 33) {
            intent.getParcelableExtra("track", Track::class.java)!!
        } else {
            intent.getParcelableExtra<Track>("track")!!
        }

        val trackImage:ImageView = findViewById(R.id.player_track_image)
        val playerTrackName: TextView = findViewById(R.id.player_track_name)
        val playerArtistName: TextView = findViewById(R.id.player_track_author)
        val playerTrackTime: TextView = findViewById(R.id.track_time_value)
        val collectionName: TextView = findViewById(R.id.track_album_value)
        val releaseDate: TextView = findViewById(R.id.track_year_value)
        val primaryGenreName: TextView = findViewById(R.id.track_style_value)
        val country: TextView = findViewById(R.id.track_country_value)

        playerTrackName.text = track.trackName
        playerArtistName.text = track.artistName
        playerTrackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)
        collectionName.text = if (track.collectionName!!.isNotEmpty()) track.collectionName else ""
        releaseDate.text = if (track.releaseDate!!.isNotEmpty()) track.releaseDate?.substring(
            0,
            4
        ) else "Not found"
        primaryGenreName.text = track.primaryGenreName
        country.text = track.country

        val artworkUrl512 = track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")
        Glide
            .with(trackImage)
            .load(artworkUrl512)
            .fitCenter()
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(trackImage.resources.getDimensionPixelSize(R.dimen.track_image_corner_radius)))
            .into(trackImage)
    }
}