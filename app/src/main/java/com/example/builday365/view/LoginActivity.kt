package com.example.builday365.view

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.builday365.R
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    private var btn_login: SignInButton? = null
    private var iv_splash: ImageView? = null
    private var auth: FirebaseAuth? = null
    private var googleApiClient: GoogleApiClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        iv_splash = findViewById(R.id.splash_img)
        Glide.with(this).load(R.drawable.login_main_pic).into(iv_splash)
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build()
        auth = FirebaseAuth.getInstance()
        btn_login = findViewById(R.id.login_btn_login)
        btn_login.setOnClickListener(View.OnClickListener {
            val intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient!!)
            startActivityForResult(intent, REQ_SIGN_GOOGLE)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_SIGN_GOOGLE) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data!!)
            if (result!!.isSuccess) {
                val account = result.signInAccount
                resultLogin(account)
            }
        }
    }

    private fun resultLogin(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        auth!!.signInWithCredential(credential)
            .addOnCompleteListener(this, object : OnCompleteListener<AuthResult?> {
                override fun onComplete(task: Task<AuthResult>) {
                    if (task.isSuccessful) {
                        Toast.makeText(this@LoginActivity, "구글 로그인 성공!", Toast.LENGTH_SHORT)
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        intent.putExtra("google_name", account.displayName)
                        intent.putExtra("google_photo", account.photoUrl.toString())
                        Log.e("DEBUG", "google_name" + account.displayName)
                        Log.e("DEBUG", "google_photo" + account.photoUrl.toString())
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@LoginActivity, "구글 로그인 실패!", Toast.LENGTH_SHORT)
                    }
                }
            })
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {}

    companion object {
        private const val REQ_SIGN_GOOGLE = 100
    }
}