package com.minthang.seintalonekidstv.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.minthang.seintalonekidstv.R
import com.minthang.seintalonekidstv.adapters.VideosRecyclerViewAdapter
import com.minthang.seintalonekidstv.datamodels.videoListData

class VideoViewerActivity : AppCompatActivity() {
    //lateinit var slideViewAdapter: SlideViewPagerAdapter
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Request Fullscreen codes
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_video_viewer)


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

        val parent: String = intent.getStringExtra("cardId")!!


        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child(parent)

        val list = ArrayList<videoListData>()
        val adapter = VideosRecyclerViewAdapter(list)
        val myrecyclerview: RecyclerView = findViewById(R.id.recycler_view_video)


        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI

                Toast.makeText(applicationContext, "Some thing has changed!!", Toast.LENGTH_SHORT).show()
                list.clear()
                for(ds in dataSnapshot.children){
                    val item =
                        videoListData(
                            ds.child("title").value.toString(),
                            ds.child("thumbnail").value.toString(),
                            ds.child("link").value.toString(),
                            ds.child("summary").value.toString()
                        )
                    list.add(item)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("loadPost:onCancelled", databaseError.toException())
                // ...
            }
        }
        databaseReference.addValueEventListener(postListener)


        myrecyclerview.adapter = adapter
        myrecyclerview.layoutManager = LinearLayoutManager(applicationContext)
        myrecyclerview.setHasFixedSize(true)

    }
}