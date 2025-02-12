package com.pubscale.basicvideoplayer.presentation.activities

import android.app.PictureInPictureParams
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Rational
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.pubscale.basicvideoplayer.databinding.ActivityMainBinding
import com.pubscale.basicvideoplayer.presentation.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {
    private var player: ExoPlayer? = null
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private var video: String = ""

    //Checking for pip support
    private val isPipSupported by lazy {
        packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //observing the url form viewmodel
        mainViewModel.videoUrl.observe(this) { videoUrl ->
            if (videoUrl != null) {
                video = videoUrl
                setupExoPLayer(videoUrl)
            } else {
                //hiding player if error on url
                binding.playerView.visibility = View.GONE
                binding.errorLayout.visibility = View.VISIBLE
            }
        }

//handling onBackPress to show pip automatically
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                setupPip()
            }
        }
        this.onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun setupExoPLayer(url: String) {
        try {
            player = ExoPlayer.Builder(this).build()
            binding.playerView.player = player

            val videoUri = Uri.parse(url)

            val mediaItem = MediaItem.fromUri(videoUri)
            player?.setMediaItem(mediaItem)
            player?.prepare()
            player?.playWhenReady = true
        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
        }

    }

    override fun onRestart() {
        super.onRestart()
        player?.play()
    }

    override fun onStop() {
        super.onStop()
        player?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
    }

    //Setting up PIP
    private fun setupPip() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (isPipSupported && video.isNotEmpty()) {
                val aspectRatio = Rational(binding.playerView.width, binding.playerView.height)
                val pipParams = PictureInPictureParams.Builder().setAspectRatio(aspectRatio).build()
                enterPictureInPictureMode(pipParams)
            } else {
               finish()
            }

        }else{
            finish()
        }

    }

    //When user exist the app pip will be continued
    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        setupPip()
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean, newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        // Hide UI controls when in PiP
        binding.playerView.useController = !isInPictureInPictureMode
    }


}