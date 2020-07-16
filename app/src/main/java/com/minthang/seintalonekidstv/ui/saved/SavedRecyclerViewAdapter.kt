package com.minthang.seintalonekidstv.ui.saved

import android.app.Application
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
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

class SavedRecyclerViewAdapter(private val videoList: List<videoListData>):
    Adapter<SavedRecyclerViewAdapter.VideoListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.videolist, parent, false)
        return VideoListViewHolder(itemView)
    }

    override fun getItemCount() = videoList.size

    override fun onBindViewHolder(holder: VideoListViewHolder, position: Int) {
        val currentItem  = videoList[position]

        holder.itemView.setOnClickListener(){
            Toast.makeText(holder.itemView.context, currentItem.url, Toast.LENGTH_SHORT).show()
        }

        holder.setVideo(currentItem.ctx, currentItem.title, currentItem.url)
//        holder.textView1.text = currentItem.text1
//        holder.textView2.text = currentItem.text2
    }

//    class VideoListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
//        val textView: TextView = itemView.text
//        val textView1: TextView = itemView.text1
//        val textView2: TextView = itemView.text2
//    }
class VideoListViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var exoplyerview: PlayerView

    fun setVideo(ctx: Application?, title: String?, url: String?) {
        val textView = view.findViewById<TextView>(R.id.videoTitle)
        exoplyerview = view.findViewById(R.id.exo_player_view)
        textView.text = title
        try {
            val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter.Builder(ctx).build()
            val trackSelector: TrackSelector =
                DefaultTrackSelector(AdaptiveTrackSelection.Factory(bandwidthMeter))
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(ctx)
            val videoUri = Uri.parse(url)
            val dataSourceFactory = DefaultHttpDataSourceFactory("video")
            val extractorFactory: ExtractorsFactory = DefaultExtractorsFactory()
            val mediaSource: MediaSource = ExtractorMediaSource(videoUri, dataSourceFactory, extractorFactory, null, null)
            exoplyerview.setPlayer(simpleExoPlayer)
            simpleExoPlayer.prepare(mediaSource)
            simpleExoPlayer.setPlayWhenReady(false)
        } catch (e: Exception) {
            Log.e("ViewHolder Error", "ERROR " + e.message.toString())
        }
    }

}
}