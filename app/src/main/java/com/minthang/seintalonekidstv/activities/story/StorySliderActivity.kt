package com.minthang.seintalonekidstv.activities.story

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.google.firebase.database.*
import com.minthang.seintalonekidstv.R
import com.minthang.seintalonekidstv.activities.story.slides.SlideListData
import com.minthang.seintalonekidstv.activities.story.slides.SlideViewPagerAdapter
import kotlinx.android.synthetic.main.activity_story_slider.*
import java.lang.Exception
import kotlin.collections.ArrayList

class StorySliderActivity : AppCompatActivity() {

    lateinit var slideViewAdapter: SlideViewPagerAdapter
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    lateinit var mediaPlayer: MediaPlayer
    var positionpage: Int = 0
    val audioMainUrl: String = "https://firebasestorage.googleapis.com/v0/b/sein-ta-lone-kids-tv.appspot.com/o/audios%2Fsong2.mp3?alt=media&token=44088b33-1333-49c7-86bc-3ada0e5fc2ab"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_story_slider)


//        val storyListData: StoryListData = intent.getParcelableExtra<StoryListData>("slideData")!!
//        story_title.text = storyListData.title
//        story_summary.text = storyListData.summary
//
//        try {
//            Picasso.get().load(storyListData.thumbnail_url).into(story_thumbnail)
//        }catch (e: Exception){
//            Picasso.get().load(R.drawable.ic_add_photo).into(story_thumbnail)
//            Toast.makeText(applicationContext, ""+e.message, Toast.LENGTH_LONG).show()
//        }
        mediaPlayer = MediaPlayer()
        //prepareMediaPlayer(mediaPlayer, audioMainUrl)

        val story_id: String = intent.getStringExtra("story_id")!!

//        Toast.makeText(applicationContext, ""+story_id, Toast.LENGTH_SHORT).show()

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("Stories").child(story_id).child("slides")

        val list = ArrayList<SlideListData>()
        val audioUrl = ArrayList<String>()

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI

                Toast.makeText(applicationContext, "Some thing has changed", Toast.LENGTH_SHORT).show()
                list.clear()
                audioUrl.clear()
                for(ds in dataSnapshot.children){
                    val item = SlideListData(ds.child("imgurl").value.toString(), ds.child("audiourl").value.toString())
                    list.add(item)
                    audioUrl.add(item.audio_url)
                }

                //Toast.makeText(applicationContext, "Audio URL=$audioMainUrl", Toast.LENGTH_SHORT).show()
                slideViewAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("loadPost:onCancelled", databaseError.toException())
                // ...
            }
        }
        databaseReference.addValueEventListener(postListener)

        slideViewAdapter = SlideViewPagerAdapter(this, list)
        viewPager.adapter = slideViewAdapter

        story_tab_slide.setupWithViewPager(viewPager)

        button2.setOnClickListener(){
            positionpage = viewPager.currentItem

            if (positionpage == list.size){
                positionpage = 0
                viewPager.currentItem = positionpage
            }
            
            if (positionpage < list.size){
                positionpage++
                viewPager.currentItem = positionpage
            }
        }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
                //To implement
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//                   Not Implemented yet
            }

            override fun onPageSelected(position: Int) {
                Toast.makeText(applicationContext, "Position: $position, "+audioUrl[position], Toast.LENGTH_SHORT).show()
//                val mediaPlayer2 = MediaPlayer.create(applicationContext, Uri.parse(audioMainUrl))
//                mediaPlayer2.prepare()
//                mediaPlayer2.start()
//                mediaPlayer2.release()
                if (mediaPlayer.isPlaying){
                    mediaPlayer.stop()
                    mediaPlayer.release()
                }

                mediaPlayer = MediaPlayer()
                prepareMediaPlayer(mediaPlayer, list[position].audio_url)
                mediaPlayer.start()

            }
        })
    } //end of onCreate fun

    //Preparing MediaPlayer for to play audio
    private fun prepareMediaPlayer(player: MediaPlayer, url: String){
        try{
            player.apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
            }
            player.setDataSource(url)
            player.prepare()
        }catch (e: Exception){
            Toast.makeText(applicationContext, "Music Error: "+e.message+" url=$String", Toast.LENGTH_SHORT).show()
        }
    }

//    override fun onStart() {
//        super.onStart()
//        mediaPlayer.start()
//    }
//
    override fun onPause() {
        super.onPause()
        mediaPlayer.stop()
        mediaPlayer.release()
    }
//
//    override fun onResume() {
//        super.onResume()
//        mediaPlayer.start()
//    }
}

