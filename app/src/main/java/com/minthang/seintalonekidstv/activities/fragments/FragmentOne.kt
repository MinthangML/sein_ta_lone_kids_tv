package com.minthang.seintalonekidstv.activities.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.minthang.seintalonekidstv.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentOne.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentOne : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var exoplayerViewAll: PlayerView
    private var videoLink: String? = null
    //private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            videoLink = it.getString(VIDEOLINK)
            //param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root: View = inflater.inflate(R.layout.fragment_one, container, false)



        exoplayerViewAll = root.findViewById(R.id.videoViewAll)
        try {
            val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter.Builder(root.context).build()
            val trackSelector: TrackSelector =
                DefaultTrackSelector(AdaptiveTrackSelection.Factory(bandwidthMeter))
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(root.context)
            val videoUri = Uri.parse(videoLink)
            val dataSourceFactory = DefaultHttpDataSourceFactory("video")
            val extractorFactory: ExtractorsFactory = DefaultExtractorsFactory()
            val mediaSource: MediaSource = ExtractorMediaSource(videoUri, dataSourceFactory, extractorFactory, null, null)
            exoplayerViewAll.player = simpleExoPlayer
            simpleExoPlayer.prepare(mediaSource)
            simpleExoPlayer.playWhenReady = false
        } catch (e: Exception) {
            Log.e("ViewHolder Error", "ERROR " + e.message.toString())
        }

        // Inflate the layout for this fragment
        return root
    }

//    override fun onStart() {
//        super.onStart()
//    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentOne.
         */
        // TODO: Rename and change types and number of parameters

        private val TAG = FragmentOne::class.qualifiedName
        private val VIDEOLINK:String = "Link"

        @JvmStatic
        fun newInstance(link: String) =
            FragmentOne().apply {
                arguments = Bundle().apply {
                    putString(VIDEOLINK, link)
                    //putString(ARG_PARAM2, param2)
                }
            }
    }
}