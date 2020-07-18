package com.minthang.seintalonekidstv.activities.story

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.database.*
import com.minthang.seintalonekidstv.LoginActivity
import com.minthang.seintalonekidstv.MainActivity
import com.minthang.seintalonekidstv.R
import com.minthang.seintalonekidstv.activities.story.slides.SlideListData
import com.minthang.seintalonekidstv.activities.story.slides.SlideViewPagerAdapter
import com.minthang.seintalonekidstv.ui.saved.videoListData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_story_slider.*
import java.lang.System.load
import java.util.*
import kotlin.collections.ArrayList

class StorySliderActivity : AppCompatActivity() {

    lateinit var slideViewAdapter: SlideViewPagerAdapter
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    var position: Int = 0

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

        val story_id: String = intent.getStringExtra("story_id")!!

//        Toast.makeText(applicationContext, ""+story_id, Toast.LENGTH_SHORT).show()

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("Stories").child(story_id).child("slides")

        val list = ArrayList<SlideListData>()

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI

                Toast.makeText(applicationContext, "Some thing has changed", Toast.LENGTH_SHORT).show()
                list.clear()
                for(ds in dataSnapshot.children){
                    val item = SlideListData(ds.value.toString())
                    list.add(item)
                }

                Toast.makeText(applicationContext, "List=$list", Toast.LENGTH_SHORT).show()
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
            position = viewPager.currentItem

            if (position == list.size){
                position = 0
            }
            
            if (position < list.size){
                position++
                viewPager.setCurrentItem(position)
            }
        }

        //The following code is important for timer

//        val handler = Handler()
//        val Update = Runnable {
//            if (position == list.size) {
//                position = 0
//            }
//
//            viewPager.setCurrentItem(position++, true)
//        }
//        val swipeTimer = Timer()
//        swipeTimer.schedule(object : TimerTask() {
//            override fun run() {
//                handler.post(Update)
//            }
//        }, 1000, 1000)

    }
}

