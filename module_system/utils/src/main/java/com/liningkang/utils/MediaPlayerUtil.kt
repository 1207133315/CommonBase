package com.liningkang.utils

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener

class MediaPlayerUtil {
    private var mediaPlayer: MediaPlayer? = null
    val MUSIC_UNSTART = 0// 未开始
    val MUSIC_START = 1// 正在计时
    val MUSIC_PAUSE = 3// 暂停
    var mediaState = MUSIC_UNSTART
    var resId=0
    fun playRawAudio(context: Context, audioResId: Int) {
        stopPlayback()
        if (mediaState == MUSIC_UNSTART) {
            mediaPlayer = MediaPlayer.create(context, audioResId)
            mediaPlayer?.start();
            resId=audioResId
            mediaState = MUSIC_START
        }


    }

    fun getDuration():Int{
        return mediaPlayer?.duration?:0
    }
    fun getCurrentPosition():Int{
        return mediaPlayer?.currentPosition?:0
    }


    fun setOnCompletionListener(listener:OnCompletionListener){
        mediaPlayer?.setOnCompletionListener(listener)

    }

    fun next(context: Context, audioResId: Int) {
        stopPlayback()
        playRawAudio(context, audioResId)
    }

    fun previous(context: Context, audioResId: Int) {
        stopPlayback()
        playRawAudio(context, audioResId)
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying == true
    }

    fun pausePlayback() {
        if (mediaPlayer?.isPlaying == true) {
            mediaState = MUSIC_PAUSE
            mediaPlayer?.pause()
        }

    }

    fun resumePlayback() {
        if (mediaPlayer?.isPlaying == false) {
            mediaState = MUSIC_START
            mediaPlayer!!.start()
        }
    }

    fun stopPlayback() {
        if (mediaPlayer != null) {
            mediaState = MUSIC_UNSTART
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }
}