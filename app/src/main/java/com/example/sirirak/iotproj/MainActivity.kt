package com.example.sirirak.iotproj

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.Auth
import android.content.Intent
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.example.sirirak.iotproj.activity.AccountActivity
import com.google.firebase.auth.GoogleAuthProvider


class MainActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null
    private var email: String? = null
    private var password: String? = null

    private val RC_SIGN_IN = 1
    private var mGoogleApiClient:GoogleApiClient?=null

    private val TagGoogleSign:String = "result google sign-in"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                // User is signed in
                val intent = Intent(this, AccountActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // User is signed out
            }
            // ...
        }
        btn_google_sign.setOnClickListener {
            signIn()
        }

        // Configure Google Sign In
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleApiClient = GoogleApiClient.Builder(applicationContext)
                .enableAutoManage(this,
                        GoogleApiClient.OnConnectionFailedListener {

                        }
                ).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build()
    }
    // Firebase Auth with Email
    fun onClickCreate(view:View){
        email = edt_email.text.toString()
        password = edt_password.text.toString()

        mAuth!!.createUserWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener(this) { task ->
                    if (!task.isSuccessful) {
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                    // ...
                }
    }
    fun onClickSignIn(view: View){
        email = edt_email.text.toString()
        password = edt_password.text.toString()

        mAuth!!.signInWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener(this) { task ->
                    if (!task.isSuccessful) {

                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                    // ...
                }

    }
    override fun onStart() {
        super.onStart()
        mAuth!!.addAuthStateListener(mAuthListener!!)
    }

    override fun onStop() {
        super.onStop()
        if (mAuthListener != null) {
            mAuth!!.removeAuthStateListener(mAuthListener!!);
        }
    }
    //Firebase Auth with Google Account
    fun signIn() {
        Log.d("Google sign in","onclick")
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                // Google Sign In was successful, authenticate with Firebase
                val account = result.signInAccount
                firebaseAuthWithGoogle(account)
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        Log.d(TagGoogleSign, "firebaseAuthWithGoogle:" + acct!!.getId())

        val credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null)
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TagGoogleSign, "signInWithCredential:success")
                        val user = mAuth!!.getCurrentUser()
                        //updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("result google sign-in", "signInWithCredential:failure", task.exception)
                        Toast.makeText(this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        //updateUI(null)
                    }

                    // ...
                }
    }

}
