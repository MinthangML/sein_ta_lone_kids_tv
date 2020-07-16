package com.minthang.seintalonekidstv

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup.*
import java.lang.ref.Reference
import java.util.*
import kotlin.collections.HashMap

class SignupActivity : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        firebaseAuth = FirebaseAuth.getInstance()
        btn_signup.setOnClickListener(){
            signup()
        }
    }

    private fun signup() {

        if (username_regis.text.toString().isEmpty()){
            username_regis.error = "Please enter username"
            username_regis.requestFocus()
            return
        }

        if (user_email_regis.text.toString().isEmpty()){
            user_email_regis.error = "Please enter email"
            user_email_regis.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(user_email_regis.text.toString()).matches()) {
            user_email_regis.error = "Please enter valid address"
            user_email_regis.requestFocus()
            return
        }

        if (password_regis.text.toString().isEmpty()){
            password_regis.error = "Please enter password"
            password_regis.requestFocus()
            return
        }

        if (password_regis.text.toString().length < 6){
            password_regis.error = "Password must have 6 minimum length"
            password_regis.requestFocus()
            return
        }

        if (confrm_password.text.toString().isEmpty()){
            confrm_password.error = "Please enter confirm password"
            confrm_password.requestFocus()
            return
        }

        if (!password_regis.text.toString().equals(confrm_password.text.toString())){
            confrm_password.error = "Password doesn't match!"
            confrm_password.requestFocus()
            return
        }

        firebaseAuth.createUserWithEmailAndPassword(user_email_regis.text.toString(), password_regis.text.toString()).addOnCompleteListener(this){
            task->
            if (task.isSuccessful){
                val user: FirebaseUser = firebaseAuth.currentUser!!
                val email = user.email!!
                val uid = user.uid
                val name: String = username_regis.text.toString()

                val hashMap: HashMap<Any, String> = HashMap()
                hashMap.put("email", email)
                hashMap.put("uid", uid)
                hashMap.put("name", name)
                hashMap.put("image", "")

                firebaseDatabase = FirebaseDatabase.getInstance()
                databaseReference = firebaseDatabase.getReference("Users")
                databaseReference.child(uid).setValue(hashMap)

                Toast.makeText(this@SignupActivity, "You have created an account", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }else{
                Toast.makeText(this@SignupActivity, "Authentication failed, error->"+task.result, Toast.LENGTH_SHORT).show()
                //updateUI(null)
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {

    }

    fun logIn(view: View) {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}