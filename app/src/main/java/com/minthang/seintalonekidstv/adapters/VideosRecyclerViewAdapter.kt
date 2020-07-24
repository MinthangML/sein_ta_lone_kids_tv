package com.minthang.seintalonekidstv.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.minthang.seintalonekidstv.R
import com.minthang.seintalonekidstv.activities.FullScreenVideoActivity
import com.minthang.seintalonekidstv.activities.story.StoryActivity
import com.minthang.seintalonekidstv.datamodels.videoListData
import com.squareup.picasso.Picasso

class VideosRecyclerViewAdapter(private val videoList: List<videoListData>):
    Adapter<VideosRecyclerViewAdapter.VideoListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.videolist, parent, false)
        return VideoListViewHolder(
            itemView
        )
    }

    override fun getItemCount() = videoList.size

    override fun onBindViewHolder(holder: VideoListViewHolder, position: Int) {
        val currentItem  = videoList[position]

        holder.itemView.setOnClickListener(){
            Toast.makeText(it.context, currentItem.url, Toast.LENGTH_SHORT).show()
        }

        holder.thumbnailImgView.setOnClickListener(){
            val intent: Intent = Intent(it.context, FullScreenVideoActivity::class.java)
            intent.putExtra("videoLink", currentItem.url)
            intent.putExtra("videoTitle", currentItem.title)
            it.context.startActivity(intent)
        }

        holder.setVideo(currentItem.title, currentItem.thumbnail,currentItem.url, currentItem.summary)
//        holder.textView1.text = currentItem.text1
//        holder.textView2.text = currentItem.text2
    }

//    class VideoListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
//        val textView: TextView = itemView.text
//        val textView1: TextView = itemView.text1
//        val textView2: TextView = itemView.text2
//    }
class VideoListViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
//    private lateinit var simpleExoPlayer: SimpleExoPlayer
//    private lateinit var exoplyerview: PlayerView
    private val textView: TextView = view.findViewById<TextView>(R.id.videoTitle)
    private val videoSummary: TextView = view.findViewById(R.id.video_summary)
    val thumbnailImgView: ImageView = view.findViewById(R.id.exo_player_view)

    fun setVideo(title: String?, thumbnail: String?,url: String?, summary: String?) {
        //exoplyerview = view.findViewById(R.id.exo_player_view)
        textView.text = title
        videoSummary.text = summary

        try {
            Picasso.get().load(thumbnail).into(thumbnailImgView)
        }catch (e : Exception){
            Log.e("IMGERROR: ", e.message.toString())
        }

//        try {
//            val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter.Builder(ctx).build()
//            val trackSelector: TrackSelector =
//                DefaultTrackSelector(AdaptiveTrackSelection.Factory(bandwidthMeter))
//            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(ctx)
//            val videoUri = Uri.parse(url)
//            val dataSourceFactory = DefaultHttpDataSourceFactory("video")
//            val extractorFactory: ExtractorsFactory = DefaultExtractorsFactory()
//            val mediaSource: MediaSource = ExtractorMediaSource(videoUri, dataSourceFactory, extractorFactory, null, null)
//            exoplyerview.setPlayer(simpleExoPlayer)
//            simpleExoPlayer.prepare(mediaSource)
//            simpleExoPlayer.setPlayWhenReady(false)
//        } catch (e: Exception) {
//            Log.e("ViewHolder Error", "ERROR " + e.message.toString())
//        }
    }

}
}

