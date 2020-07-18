package com.minthang.seintalonekidstv.activities.story

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.minthang.seintalonekidstv.R
import com.squareup.picasso.Picasso
import java.lang.Exception

class StoryRecyclerViewAdapter(private val StoryList: List<StoryListData>): Adapter<StoryRecyclerViewAdapter.VideoListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.story_list_data_view, parent, false)
        return VideoListViewHolder(itemView)
    }

    override fun getItemCount() = StoryList.size

    override fun onBindViewHolder(holder: VideoListViewHolder, position: Int) {
        val currentItem  = StoryList[position]

        holder.itemView.setOnClickListener(){
            Toast.makeText(holder.itemView.context, currentItem.title, Toast.LENGTH_SHORT).show()
            val sliderintent = Intent(holder.itemView.context, StorySliderActivity::class.java)
            //val storyListData = StoryListData(currentItem.thumbnail_url, currentItem.title, currentItem.summary)
            //storyListData.title = currentItem.title
            //sliderintent.putExtra("slideData", storyListData)
            sliderintent.putExtra("story_id", currentItem.story_id)
            holder.itemView.context.startActivity(sliderintent)
        }

        holder.setVideo(currentItem.thumbnail_url, currentItem.title, currentItem.summary)
    }

    class VideoListViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
    fun setVideo(thumnail_url: String, title: String?, summary: String?) {
        val titletextView = view.findViewById<TextView>(R.id.story_list_title)
        val thumnailImageView: ImageView = view.findViewById(R.id.story_list_thumbnail)
        val summaryTextView: TextView = view.findViewById(R.id.story_list_summary)

        titletextView.text = title
        summaryTextView.text = summary
        try {
            if ((thumnail_url.isEmpty()) or (thumnail_url == "")){
                Picasso.get().load(R.drawable.ic_add_photo).into(thumnailImageView)
            }else{
                Picasso.get().load(thumnail_url).into(thumnailImageView)
            }
        }catch (e: Exception){
            Picasso.get().load(R.drawable.ic_add_photo).into(thumnailImageView)
        }
    }
    }
}