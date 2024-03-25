package com.qingteng.soundeffect.player

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Handler
import android.os.Looper
import com.example.soundeffect.R
import com.liningkang.base.BaseApplication
import com.qingteng.soundeffect.SoundEffectId


/**
 * 音效播放器
 *
 * @author wuxinrong
 */
class SoundEffectPlayer @SuppressLint("NewApi") private constructor() {
    private var mSoundPool: SoundPool? = null
    private var mCurrentSoundId: Int? = 0
    private var mCurrentStreamId: Int? = 0

    /**
     * 解决快速连续播放多个音效时，之前的音效无法播放的问题
     *
     * @param resId       播放的音效ID
     * @param delayMillis 非常重要，如果音效没有播放，则适当设置该参数，例如100
     */
    fun play(resId: Int, delayMillis: Long) {
        // if (currentVolume?:0 > 0 ) {

        val id = mSoundEffectResMap[resId]
        handler?.postDelayed(
            { mCurrentSoundId = mSoundPool?.load(mContext, id!!, 1) },
            delayMillis
        )
        // }
    }

    init {
        mSoundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val aa = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
            SoundPool.Builder()
                .setMaxStreams(MAX_STREAMS)
                .setAudioAttributes(aa)
                .build()
        } else {
            SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0)
        }
        mSoundPool?.setOnLoadCompleteListener { _: SoundPool?, _: Int, _: Int ->
            mCurrentStreamId = mSoundPool?.play(mCurrentSoundId!!, 1f, 1f, 0, 0, 1f)
        }
    }

    fun stop() {

        // 关闭音效
        mSoundPool?.stop(mCurrentStreamId!!)
        mSoundPool?.unload(mCurrentSoundId!!)
    }

    fun pause(){
        mSoundPool?.pause(mCurrentSoundId!!)
    }
    fun resume(){
        mSoundPool?.resume(mCurrentSoundId!!)
    }

    companion object {
        private const val MAX_STREAMS = 16
        @Volatile
        private var sInstance: SoundEffectPlayer? = null
        private var mContext: Context? = null
        private var currentVolume: Int? = 1
        private var handler: Handler? = null

        @Volatile
        private var hasInitialized = false
        private val mSoundEffectResMap = HashMap<Int, Int>()


        fun init() {
            if (!hasInitialized) {
                mContext = BaseApplication.context


                val audioManager =
                    mContext?.getSystemService(Context.AUDIO_SERVICE) as AudioManager?
                currentVolume = audioManager?.getStreamVolume(AudioManager.STREAM_MUSIC)
                handler = Handler(Looper.getMainLooper())
                hasInitialized = true
            }
        }


        val instance: SoundEffectPlayer?
            get() {
                if (!hasInitialized) {
                    if (!hasInitialized) {
                        throw RuntimeException("SoundEffectPlayer::Init::Invoke init(context, resIds) first!")
                    }
                }
                if (sInstance == null) {
                    synchronized(SoundEffectPlayer::class.java) {
                        if (sInstance == null) {
                            sInstance = SoundEffectPlayer()
                        }
                    }
                }
                return sInstance
            }
    }
}