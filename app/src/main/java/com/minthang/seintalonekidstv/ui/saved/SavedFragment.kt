package com.minthang.seintalonekidstv.ui.saved

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.minthang.seintalonekidstv.R
import com.minthang.seintalonekidstv.adapters.VideosRecyclerViewAdapter
import com.minthang.seintalonekidstv.datamodels.videoListData

class SavedFragment : Fragment() {

    private lateinit var savedViewModel: SavedViewModel
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        savedViewModel = ViewModelProviders.of(this).get(SavedViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_saved, container, false)
//        val textView: TextView = root.findViewById(R.id.text_gallery)
//        savedViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })


        //val exampleList = generateDummyList(100)
        val list = ArrayList<videoListData>()
        val adapter =
            VideosRecyclerViewAdapter(list)
        val myrecyclerview: RecyclerView = root.findViewById(R.id.recycler_view_video)
        myrecyclerview.adapter = adapter
        myrecyclerview.layoutManager = LinearLayoutManager(context)
        myrecyclerview.setHasFixedSize(true)

        database = FirebaseDatabase.getInstance()
        databaseReference = database.reference.child("Videos")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI

                Toast.makeText(this@SavedFragment.context, "Some thing has changed", Toast.LENGTH_SHORT).show()
                list.clear()
                for(ds in dataSnapshot.children){
                    val item =
                        videoListData(
                            ds.child("title").value.toString(),
                            ds.child("thumbnail").value.toString(),
                            ds.child("link").value.toString(),
                        ds.child("summary").value.toString())
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

        return root
    }

    override fun onStart() {
        super.onStart()

        //var firebaseRe
        Toast.makeText(this.context, "Called onStart() function", Toast.LENGTH_SHORT).show()
    }
}
