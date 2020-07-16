package com.minthang.seintalonekidstv.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.minthang.seintalonekidstv.R
import com.minthang.seintalonekidstv.activities.story.StoryActivity
import www.sanju.motiontoast.MotionToast

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

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

        var storyCardView: CardView = root.findViewById(R.id.kids_story)
        storyCardView.setOnClickListener(){
            startAct(this.requireContext())
        }

        return root
    }

    private fun startAct(ctx: Context){
        startActivity(Intent(ctx, StoryActivity::class.java))
    }
}