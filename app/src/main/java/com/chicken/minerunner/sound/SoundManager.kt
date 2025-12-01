package com.chicken.minerunner.sound

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.chicken.minerunner.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoundManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private enum class Channel { MENU, GAME }

    private var currentChannel: Channel? = null

    // --- новый двухконтурный контроль ---
    private var systemPaused = false
    private var gamePaused = false

    private val musicPlayers = mutableMapOf<Channel, MediaPlayer>()

    private val soundPool = SoundPool.Builder()
        .setMaxStreams(6)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        .build()

    private val loadedSfx = mutableMapOf<Int, Int>()
    private val readySfx = mutableSetOf<Int>()
    private val pendingPlay = mutableSetOf<Int>()

    init {
        soundPool.setOnLoadCompleteListener { pool, sampleId, status ->
            if (status == 0) {
                readySfx += sampleId
                if (pendingPlay.remove(sampleId)) {
                    pool.play(sampleId, 1f, 1f, 1, 0, 1f)
                }
            } else {
                pendingPlay.remove(sampleId)
            }
        }
    }


    // ============================================================
    //                 MUSIC CHANNEL BASICS
    // ============================================================

    fun playMenuMusic(enabled: Boolean) {
        if (!enabled) return stopMusic()
        playMusic(Channel.MENU, R.raw.music_menu)
    }

    fun playGameMusic(enabled: Boolean) {
        if (!enabled) return stopMusic()
        playMusic(Channel.GAME, R.raw.music_game)
    }

    private fun playMusic(channel: Channel, @RawRes res: Int) {
        if (currentChannel == channel && musicPlayers[channel]?.isPlaying == true) return

        currentChannel?.let { old ->
            musicPlayers[old]?.pause()
        }

        val mp = ensurePlayer(channel, res)
        currentChannel = if (mp != null) channel else null

        mp?.let {
            it.isLooping = true
            try {
                it.start()
            } catch (_: IllegalStateException) {
                musicPlayers.remove(channel)
                currentChannel = null
            }
        }

        updatePlayback()
    }

    private fun ensurePlayer(channel: Channel, @RawRes res: Int): MediaPlayer? {
        musicPlayers[channel]?.let { return it }

        return runCatching { MediaPlayer.create(context, res) }
            .getOrNull()
            ?.also { mp ->
                mp.isLooping = true
                musicPlayers[channel] = mp
            }
    }

    fun stopMusic() {
        currentChannel?.let { ch ->
            musicPlayers[ch]?.pause()
            musicPlayers[ch]?.seekTo(0)
        }
        currentChannel = null
    }


    // ============================================================
    //                SYSTEM & GAME PAUSE LOGIC
    // ============================================================

    /** Вызов из MainActivity.onPause() */
    fun pauseForLifecycle() {
        systemPaused = true
        updatePlayback()
    }

    /** Вызов из MainActivity.onResume() */
    fun resumeAfterLifecycle() {
        systemPaused = false
        updatePlayback()
    }

    /** Пауза через меню игры */
    fun pauseForGame() {
        gamePaused = true
        updatePlayback()
    }

    /** Продолжение игры */
    fun resumeForGame() {
        gamePaused = false
        updatePlayback()
    }

    /**
     * Централизованная логика:
     * Музыка играет ТОЛЬКО если:
     * !systemPaused && !gamePaused && currentChannel != null
     */
    private fun updatePlayback() {
        val ch = currentChannel ?: return
        val mp = musicPlayers[ch] ?: return

        if (systemPaused || gamePaused) {
            if (mp.isPlaying) mp.pause()
        } else {
            if (!mp.isPlaying) {
                try {
                    mp.start()
                } catch (_: IllegalStateException) {
                    musicPlayers.remove(ch)
                    currentChannel = null
                }
            }
        }
    }


    // ============================================================
    //                           SFX
    // ============================================================

    fun playSfx(@RawRes resId: Int, enabled: Boolean) {
        if (!enabled) return

        val sample = loadedSfx[resId]
        if (sample != null) {
            if (readySfx.contains(sample)) {
                soundPool.play(sample, 1f, 1f, 1, 0, 1f)
            } else {
                pendingPlay += sample
            }
            return
        }

        val loadId = soundPool.load(context, resId, 1)
        if (loadId != 0) {
            loadedSfx[resId] = loadId
            pendingPlay += loadId
        }
    }

    fun release() {
        stopMusic()
        musicPlayers.values.forEach { it.release() }
        musicPlayers.clear()
        soundPool.release()
    }
}
