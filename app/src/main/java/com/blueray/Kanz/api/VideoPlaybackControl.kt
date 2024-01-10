package com.blueray.Kanz.api

interface VideoPlaybackControl {
    fun pauseAllVideos()
    fun playVideoAtPosition(position: Int)
}
