package com.minthang.seintalonekidstv.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.database.*
import com.minthang.seintalonekidstv.R
import com.minthang.seintalonekidstv.activities.VideoViewerActivity
import com.minthang.seintalonekidstv.activities.story.StoryActivity
import com.minthang.seintalonekidstv.activities.story.slides.SlideListData
import com.minthang.seintalonekidstv.activities.story.slides.SlideViewPagerAdapter
import kotlinx.android.synthetic.main.activity_story_slider.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import www.sanju.motiontoast.MotionToast
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    lateinit var homeslideViewAdapter: HomeSlideViewPagerAdapter
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    lateinit var runnable: Runnable
    var position: Int = 0
    val handler = Handler()
    val swipeTimer = Timer()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        MotionToast.darkToast(this.requireActivity(),"Welcome home",
            MotionToast.TOAST_SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.SHORT_DURATION,
            ResourcesCompat.getFont(this.requireContext(),R.font.helvetica_regular))

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            //textView.text = this.intent
//        })

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("Slides")

        val list = ArrayList<HomeSlideListData>()

        val homeSlidesListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI

                //Toast.makeText(root.context, "Some thing has changed", Toast.LENGTH_SHORT).show()
                list.clear()
                for(ds in dataSnapshot.children){
                    val item = HomeSlideListData(ds.value.toString())
                    list.add(item)
                }

                //Toast.makeText(root.context, "Some thing has changed list+$list", Toast.LENGTH_SHORT).show()

                homeslideViewAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("loadPost:onCancelled", databaseError.toException())
                // ...
            }
        }

        databaseReference.addValueEventListener(homeSlidesListener)

        homeslideViewAdapter = HomeSlideViewPagerAdapter(this.requireContext(), list)

        root.all_slider_viewpager.adapter = homeslideViewAdapter

        root.all_tab_slide.setupWithViewPager(root.all_slider_viewpager)


        //Timer for home fragment image slider


        runnable = Runnable {
            if (position == list.size) {
                position = 0
            }
            root.all_slider_viewpager.setCurrentItem(position++, true)
        }

        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(runnable)
            }
        }, 5000, 5000)




        ////CardViews

        val storyCardView: CardView = root.findViewById(R.id.kids_story)
        storyCardView.setOnClickListener(){
            val intent: Intent = Intent(root.context, StoryActivity::class.java)
            intent.putExtra("cardId", "Stories")
            startActivity(intent)
        }

        val engCardView: CardView = root.findViewById(R.id.learn_english)
        engCardView.setOnClickListener(){
            val intent: Intent = Intent(root.context, VideoViewerActivity::class.java)
            intent.putExtra("cardId", "English")
            startActivity(intent)
        }

        val toysCardView: CardView = root.findViewById(R.id.learn_toys)
        toysCardView.setOnClickListener(){
            val intent: Intent = Intent(root.context, VideoViewerActivity::class.java)
            intent.putExtra("cardId", "Toys")
            startActivity(intent)
        }

        val nightingaleCardView: CardView = root.findViewById(R.id.nightingale)
        nightingaleCardView.setOnClickListener(){
            val intent: Intent = Intent(root.context, VideoViewerActivity::class.java)
            intent.putExtra("cardId", "Nightingale")
            startActivity(intent)
        }

        val mindCardView: CardView = root.findViewById(R.id.mind_lecture)
        mindCardView.setOnClickListener(){
            val intent: Intent = Intent(root.context, VideoViewerActivity::class.java)
            intent.putExtra("cardId", "MindLecture")
            startActivity(intent)
        }

        val dancingCardView: CardView = root.findViewById(R.id.dancing_kids)
            dancingCardView.setOnClickListener(){
            val intent: Intent = Intent(root.context, VideoViewerActivity::class.java)
            intent.putExtra("cardId", "Dancing")
            startActivity(intent)
        }

        val bornCardView: CardView = root.findViewById(R.id.born_again)
            bornCardView.setOnClickListener(){
            val intent: Intent = Intent(root.context, VideoViewerActivity::class.java)
            intent.putExtra("cardId", "Sunday")
            startActivity(intent)
        }

        return root
    }

//    override fun onPause() {
//        super.onPause()
//        swipeTimer.cancel()
//    }

//    override fun onResume() {
//        super.onResume()
//        swipeTimer.schedule(object : TimerTask() {
//            override fun run() {
//                handler.post(runnable)
//            }
//        }, 5000, 5000)
//    }
}