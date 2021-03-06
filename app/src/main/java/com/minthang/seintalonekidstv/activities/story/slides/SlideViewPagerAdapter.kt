package com.minthang.seintalonekidstv.activities.story.slides

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager.widget.PagerAdapter
import com.minthang.seintalonekidstv.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.slide_list_image.view.*

class SlideViewPagerAdapter(private val mContext: Context, private val dataList: List<SlideListData>): PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
       return view === `object`
    }

    override fun getCount(): Int {
        return dataList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        //return super.instantiateItem(container, position)
        val inflater = LayoutInflater.from(mContext)
        val layout = inflater.inflate(R.layout.slide_list_image, container, false) as ViewGroup

        Toast.makeText(layout.context, "Item Initiated", Toast.LENGTH_SHORT).show()

        //layout.slideImageView
        try {
            Toast.makeText(mContext, ""+dataList.get(position).slide_url, Toast.LENGTH_SHORT).show()
            Picasso.get().load(dataList.get(position).slide_url).into(layout.slideImageView)
        }catch (e: Exception){
            Toast.makeText(mContext, ""+e.message, Toast.LENGTH_LONG).show()
        }

        container.addView(layout)
        return layout
    }


    override fun destroyItem(container: ViewGroup, position: Int, view: Any)
    {
        Toast.makeText(container.context, "Item Destroyed", Toast.LENGTH_SHORT).show()
        container.removeView(view as View)
    }


}