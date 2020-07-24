package com.minthang.seintalonekidstv.activities

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.minthang.seintalonekidstv.R
import kotlinx.android.synthetic.main.activity_full_screen_video.*

class FullScreenVideoActivity : AppCompatActivity() {

    lateinit var simpleExoPlayer: SimpleExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_full_screen_video)

        val videoLink: String = intent.getStringExtra("videoLink")!!
        val videoTitle: String = intent.getStringExtra("videoTitle")!!

        full_screen_video_title.text = videoTitle


        try {
            val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter.Builder(applicationContext).build()
//            val trackSelector: TrackSelector =
//                DefaultTrackSelector(AdaptiveTrackSelection.Factory(bandwidthMeter))
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(applicationContext)
            val videoUri = Uri.parse(videoLink)
            val dataSourceFactory = DefaultHttpDataSourceFactory("video")
            val extractorFactory: ExtractorsFactory = DefaultExtractorsFactory()
            val mediaSource: MediaSource = ExtractorMediaSource(videoUri, dataSourceFactory, extractorFactory, null, null)
            full_screen_exo_player.setPlayer(simpleExoPlayer)
            simpleExoPlayer.prepare(mediaSource)
            simpleExoPlayer.setPlayWhenReady(true)
        } catch (e: Exception) {
            Log.e("ViewHolder Error", "ERROR " + e.message.toString())
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        simpleExoPlayer.stop()
        simpleExoPlayer.release()

        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}