package com.chicken.minerunner.ui.sound

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.chicken.minerunner.R

class SoundManager(private val context: Context) {

    private var menuPlayer: MediaPlayer? = null
    private var gamePlayer: MediaPlayer? = null

    fun playMenuMusic(enabled: Boolean) {
        if (!enabled) {
            stopMenuMusic()
            return
        }
        if (menuPlayer == null) {
            menuPlayer = MediaPlayer.create(context, R.raw.music_menu).apply {
                isLooping = true
            }
        }
        if (menuPlayer?.isPlaying != true) {
            menuPlayer?.start()
        }
        stopGameMusic()
    }

    fun playGameMusic(enabled: Boolean) {
        if (!enabled) {
            stopGameMusic()
            return
        }
        if (gamePlayer == null) {
            gamePlayer = MediaPlayer.create(context, R.raw.music_game).apply {
                isLooping = true
            }
        }
        if (gamePlayer?.isPlaying != true) {
            gamePlayer?.start()
        }
        stopMenuMusic()
    }

    fun stopAll() {
        stopMenuMusic()
        stopGameMusic()
    }

    fun playSfx(@RawRes resId: Int, enabled: Boolean) {
        if (!enabled) return
        val player = MediaPlayer.create(context, resId)
        player.setOnCompletionListener { it.release() }
        player.start()
    }

    private fun stopMenuMusic() {
        menuPlayer?.let {
            if (it.isPlaying) it.stop()
            it.release()
        }
        menuPlayer = null
    }

    private fun stopGameMusic() {
        gamePlayer?.let {
            if (it.isPlaying) it.stop()
            it.release()
        }
        gamePlayer = null
    }
}

@Composable
fun rememberSoundManager(): SoundManager {
    val context = LocalContext.current
    return remember { SoundManager(context) }
}
