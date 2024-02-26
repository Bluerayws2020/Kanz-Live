package com.blueray.Kanz.ui.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.blueray.Kanz.R
import com.blueray.Kanz.databinding.ActivitySharedVideoPlayerBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player

class SharedVideoPlayer : AppCompatActivity() {
    private lateinit var binding: ActivitySharedVideoPlayerBinding
    var player: ExoPlayer? = null
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySharedVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        uri = intent.data
        if (uri != null) {

            if (player == null) {
                player = ExoPlayer.Builder(binding.root.context).build().also {
                    it.repeatMode = Player.REPEAT_MODE_ONE // Set repeat mode for looping
                    it.addListener(object : Player.Listener {
                        override fun onPlaybackStateChanged(playbackState: Int) {
                            // Update UI based on playback state
                            if (playbackState == Player.STATE_BUFFERING) {
                                // binding.progressBar.show()
                                // binding.placeHolderImg.show()
                            } else {
                                // binding.progressBar.hide()
                                // binding.placeHolderImg.show()
                                // binding.placeHolderBackground.hide()
                            }
                        }
                    })
                }
                val mediaItem = MediaItem.fromUri(Uri.parse(uri.toString()))
                player?.setMediaItem(mediaItem)
                player?.prepare()
                player?.play()

            }
            binding.videoView.setOnClickListener {
                player?.let { exoPlayer ->
                    if (exoPlayer.isPlaying) {
                        exoPlayer.pause()
                    } else {
                        exoPlayer.play()
                    }
                }
            }

            binding.videoView.player = player
//            player?.playWhenReady = true
        }


    }

    override fun onPause() {
        super.onPause()
        player?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
    }
}