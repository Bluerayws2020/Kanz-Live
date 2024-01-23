package com.blueray.Kanz.adapters

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueray.Kanz.api.OnMeetingStart
import com.blueray.Kanz.databinding.ListItemLiveEventBinding
import com.blueray.Kanz.databinding.NotfiItemBinding
import com.blueray.Kanz.model.MeetingItem
import com.blueray.Kanz.model.SessionData
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Tracks
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.EventLogger


class MeetingsAdapter(private var items: List<SessionData>, val onClickMeting: OnMeetingStart) : RecyclerView.Adapter<MeetingsAdapter.ViewHolder>() {
    private val AUTH_TOKEN: String ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcGlrZXkiOiJhYWZhYTcxMS04MjQxLTQwM2YtYjg2OC1kODQ4NDRhODI4NDIiLCJwZXJtaXNzaW9ucyI6WyJhbGxvd19qb2luIl0sImlhdCI6MTcwNTA4MTczMCwiZXhwIjoxODYyODY5NzMwfQ.9EfhMnAcKLHnfzxoCo120JxA77-1ooiyoLs8Av4vj-w"
    private var dataSourceFactory: DefaultHttpDataSource.Factory? = null

    private lateinit var player: ExoPlayer

    inner class ViewHolder(val binding : ListItemLiveEventBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ListItemLiveEventBinding.inflate(LayoutInflater.from(parent.context),parent,false)


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        // Bind your item data to your views

        player = ExoPlayer.Builder(holder.binding.playerView.context).build()
        holder.binding.playerView.player = player



//        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()
//            .setUserAgent("Android")
//            .setDefaultRequestProperties(hashMapOf("Authorization" to AUTH_TOKEN))
//
//        val mediaSource: MediaSource =
//            ProgressiveMediaSource.Factory(dataSourceFactory)
//                .createMediaSource(
//                    MediaItem.fromUri(item.downstreamUrl)
//                )
//
//        player.setMediaSource(mediaSource)
//        player.prepare()
//        player.playWhenReady = true
//        holder.binding.tvTitle.text = item.roomId.toString()



        dataSourceFactory = DefaultHttpDataSource.Factory()
        val mediaSource = HlsMediaSource.Factory(dataSourceFactory!!).createMediaSource(
            MediaItem.fromUri(Uri.parse(item.downstreamUrl))
        )
        val playerBuilder = ExoPlayer.Builder(holder.binding.playerView.context)
        player = playerBuilder.build()
        player!!.addAnalyticsListener(EventLogger())
        player!!.setAudioAttributes(AudioAttributes.DEFAULT,  /* handleAudioFocus= */true)
        player!!.setMediaSource(mediaSource)
      holder.binding.playerView!!.player = player
player.play()


holder.binding.tvTitle.text = item.roomId.toString()


        holder.itemView.setOnClickListener {
            onClickMeting.onMeetingGo(position)
        }
        holder.binding.playerView.setOnClickListener {
            onClickMeting.onMeetingGo(position)
        }
    }

    override fun getItemCount() = items.size

    fun updateData(newItems: List<SessionData>) {
        items = newItems
        Log.d("wertyui",newItems.toString())
        notifyDataSetChanged()
    }
}
