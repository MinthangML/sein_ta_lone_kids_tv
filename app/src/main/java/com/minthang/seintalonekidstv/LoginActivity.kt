package com.minthang.seintalonekidstv

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.minthang.seintalonekidstv.R.string.facebook_application_id
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    companion object{//google
        private val PERMISSION_CODE = 9999;
    }

    lateinit var mGoogleApiClient: GoogleApiClient      //google
    lateinit var alertDialog: SpotsDialog             //google

    var firebaseAuth: FirebaseAuth?=null
    var callbackManager: CallbackManager?=null
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    lateinit var firebaseUser: FirebaseUser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        FacebookSdk.sdkInitialize(this);
//        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login)

        configureGoogleClient()

        firebaseAuth = FirebaseAuth.getInstance()       //google & facebook
        callbackManager = CallbackManager.Factory.create()      //facebook
        alertDialog = SpotsDialog.Builder().setContext(this).setMessage("Please wait")
            .setCancelable(false)
            .build() as SpotsDialog


        btn_login.setOnClickListener(){
            val email:TextView = username_login
            val password: TextView = password_login
            default_login(email, password)
        }

        facebook_login.setReadPermissions("email")
        facebook_login.setOnClickListener(){
            fb_login()
        }

        btn_googleLogin.setOnClickListener(){
            google_login()
        }

//        printKeyHash()
    }

    private fun default_login(email: TextView, password: TextView) {  //default login
        if (email.text.toString().isEmpty()){
            email.error = "Please enter email address"
            email.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            email.error = "Please enter valid address"
            email.requestFocus()
            return
        }

        if (password.text.toString().isEmpty()){
            password.error = "Please enter password"
            password.requestFocus()
            return
        }


        firebaseAuth!!.signInWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnSuccessListener {authResult ->
            Toast.makeText(this@LoginActivity, "Login Successful "+authResult.user, Toast.LENGTH_LONG).show()
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))//MainActivity::class.java))
            finish()
        }.addOnFailureListener{e->
            Toast.makeText(this@LoginActivity, "Login Failed "+e.message, Toast.LENGTH_SHORT).show()
        }
    }               ////default login

    private fun google_login() {
        val intent: Intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(intent, PERMISSION_CODE)
    }

    private fun configureGoogleClient() {
        val options: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, options)
            .build()
        mGoogleApiClient.connect() //Dont forget this
    }

    private fun fb_login() {
        facebook_login.registerCallback(callbackManager, object:FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult?) {
                handleFacebookAccessToken(result!!.accessToken)
                //
            }

            override fun onCancel() {
                Toast.makeText(applicationContext, "Cancel", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException?) {
                Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun handleFacebookAccessToken(accessToken: AccessToken?) {
        //Get Credentails
        val credentials : AuthCredential = FacebookAuthProvider.getCredential(accessToken!!.token)
        firebaseAuth!!.signInWithCredential(credentials).addOnFailureListener(){
            e->
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }.addOnSuccessListener {result->
            val auth: FirebaseAuth = FirebaseAuth.getInstance()
            firebaseUser = auth.currentUser!!

            val email:String = result.user!!.email.toString()
            val name:String = result.user!!.displayName!!
            val uid:String = result.user!!.uid

            if (result.additionalUserInfo!!.isNewUser){
                val hashMap: HashMap<Any, String> = HashMap()
                hashMap.put("email", email)
                hashMap.put("uid", uid)
                hashMap.put("name", name)
                hashMap.put("image", "")

                firebaseDatabase = FirebaseDatabase.getInstance()
                databaseReference = firebaseDatabase.getReference("Users")
                databaseReference.child(uid).setValue(hashMap)
            }


            val logged_activity = Intent(applicationContext, MainActivity::class.java)
            logged_activity.putExtra("email", email)
            Toast.makeText(this, ""+email, Toast.LENGTH_LONG).show()
            startActivity(logged_activity)
            finish()
            Toast.makeText(this, "You logged with email : "+email, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PERMISSION_CODE){                //google
            val result:GoogleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data) as GoogleSignInResult
            Toast.makeText(this, data.toString(), Toast.LENGTH_LONG).show()
            if (result.isSuccess){
                val account: GoogleSignInAccount? = result.signInAccount
                val idToken: String? = account!!.idToken

                val credential = GoogleAuthProvider.getCredential(idToken, null)
                firebaseAuthWithGoogle(credential)
            }else{
                Log.d("Minthang_ERROR", "Login Failded")
                Toast.makeText(this, "Login Unsuccessful "+result.status, Toast.LENGTH_SHORT).show()
            }
        }
        callbackManager!!.onActivityResult(requestCode, resultCode, data) //facebook,
    }

    private fun firebaseAuthWithGoogle(credential: AuthCredential) {
        firebaseAuth!!.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                val auth: FirebaseAuth = FirebaseAuth.getInstance()
                firebaseUser = auth.currentUser!!

                val email:String = authResult.user!!.email.toString()
                val name:String = authResult.user!!.displayName!!
                val uid:String = authResult.user!!.uid

                if (authResult.additionalUserInfo!!.isNewUser){
                    val hashMap: HashMap<Any, String> = HashMap()
                    hashMap.put("email", email)
                    hashMap.put("uid", uid)
                    hashMap.put("name", name)
                    hashMap.put("image", "")

                    firebaseDatabase = FirebaseDatabase.getInstance()
                    databaseReference = firebaseDatabase.getReference("Users")
                    databaseReference.child(uid).setValue(hashMap)
                }


                val logged_activity = Intent(applicationContext, MainActivity::class.java)
                logged_activity.putExtra("email", email)
                Toast.makeText(this, ""+email, Toast.LENGTH_LONG).show()
                startActivity(logged_activity)
                finish()
            }.addOnFailureListener{
                e->
                Toast.makeText(this, ""+e.message, Toast.LENGTH_SHORT).show()
            }
    }

    fun signUp(view: View) {
        startActivity(Intent(this, SignupActivity::class.java))
        finish()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Toast.makeText(this, ""+p0.errorMessage, Toast.LENGTH_SHORT).show()
    }

//    fun printKeyHash(){
//        try {
//            val info: PackageInfo?? = packageManager.getPackageInfo("com.minthang.seintalonekidstv", PackageManager.GET_SIGNING_CERTIFICATES)
//            for (signature : Signature?? in info!!.signatures){
//                val md: MessageDigest?? = MessageDigest.getInstance("SHA");
//                md!!.update(signature!!.toByteArray())
//                Log.e("KEYHASH", Base64.encodeToString(md!!.digest(), Base64.DEFAULT))
//            }
//        }catch (e:PackageManager.NameNotFoundException){
//            Log.e("KEYHASH", "NameNotFoundException")
//        }
//        catch (e:NoSuchAlgorithmException){
//            Log.e("KEYHASH", "NoSuchAlgorithmException")
//        }
//    }
}