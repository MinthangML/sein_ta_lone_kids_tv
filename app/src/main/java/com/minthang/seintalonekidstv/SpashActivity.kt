package com.minthang.seintalonekidstv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SpashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT:Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_spash)

        var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        var firebaseUser: FirebaseUser?? = firebaseAuth.currentUser

        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity
            if (firebaseUser != null){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }, SPLASH_TIME_OUT)
    }
}