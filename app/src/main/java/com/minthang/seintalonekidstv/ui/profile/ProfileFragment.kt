package com.minthang.seintalonekidstv.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.minthang.seintalonekidstv.LoginActivity
import com.minthang.seintalonekidstv.R
import com.minthang.seintalonekidstv.ui.home.HomeViewModel
import com.squareup.picasso.Picasso
import www.sanju.motiontoast.MotionToast
import java.lang.Exception

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var btnLogout: Button
    private lateinit var imageAvator: ImageView

    private lateinit var textName: TextView
    private lateinit var textEmail: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        firebaseAuth = FirebaseAuth.getInstance()
        user = firebaseAuth.currentUser!!
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("Users")

        //Toast.makeText(this.context, ""+user.email, Toast.LENGTH_LONG).show()
        MotionToast.darkToast(this.requireActivity(),""+user.email,
            MotionToast.TOAST_SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.SHORT_DURATION,
            ResourcesCompat.getFont(this.requireContext(),R.font.helvetica_regular))

        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            //textView.text = this.intent
//        })

        imageAvator = root.findViewById(R.id.avator)
        textName = root.findViewById(R.id.textName)
        textEmail = root.findViewById(R.id.textEmail)
        btnLogout = root.findViewById(R.id.btnLogout)

        var query: Query = databaseReference.orderByChild("email").equalTo(user.email)
        query.addValueEventListener(object: ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(root.context, "Cancel: "+error.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.getChildren()) run {
                    val email: String = "" + ds.child("email").value
                    val image: String = "" + ds.child("image").value
                    val name: String = "" + ds.child("name").value

                    textEmail.text = email
                    textName.text = name

                    try {
                        if ((image.isEmpty()) or (image == "")){
                            Picasso.get().load(R.drawable.ic_add_photo).into(imageAvator)
                        }else{
                            Picasso.get().load(image).into(imageAvator)
                        }
                    }catch (e: Exception){
                        Picasso.get().load(R.drawable.ic_add_photo).into(imageAvator)
                    }
                }
            }
        })

        btnLogout.setOnClickListener(){
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this.context, "You logged out successfully", Toast.LENGTH_LONG).show()
            val intent: Intent = Intent(this.context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            requireActivity().finish()
        }

        return root
    }
}