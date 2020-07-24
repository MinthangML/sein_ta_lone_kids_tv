package com.minthang.seintalonekidstv.activities.story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.minthang.seintalonekidstv.R
import com.minthang.seintalonekidstv.datamodels.StoryListData
import kotlinx.android.synthetic.main.activity_story.*
import www.sanju.motiontoast.MotionToast

class StoryActivity : AppCompatActivity() {

    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story)

        MotionToast.darkToast(this,"Story Activity",
            MotionToast.TOAST_SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.SHORT_DURATION,
            ResourcesCompat.getFont(this,R.font.helvetica_regular))

        firebaseDatabase = FirebaseDatabase.getInstance()
        val cardId: String = intent.getStringExtra("cardId")!!
        databaseReference = firebaseDatabase.reference.child(cardId)
        toolbarTitle.text = cardId

        val list = ArrayList<StoryListData>()
        val adapter = StoryRecyclerViewAdapter(list)
        val myrecyclerview: RecyclerView = findViewById(R.id.story_list_recyclerview)
        myrecyclerview.adapter = adapter
        myrecyclerview.layoutManager = LinearLayoutManager(applicationContext)
        myrecyclerview.setHasFixedSize(true)

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI

                MotionToast.darkToast(this@StoryActivity,"Something has changed",
                    MotionToast.TOAST_WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(this@StoryActivity,R.font.helvetica_regular))

                list.clear()

                for(ds in dataSnapshot.children){
                    val item =
                        StoryListData(
                            ds.child("id").value.toString(),
                            ds.child("thumbnail").value.toString(),
                            ds.child("title").value.toString(),
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
    }
}