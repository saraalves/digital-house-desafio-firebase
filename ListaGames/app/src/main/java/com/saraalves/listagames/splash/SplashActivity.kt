package com.saraalves.listagames.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth
import com.saraalves.listagames.R
import com.saraalves.listagames.listagames.ListaGamesActivity
import com.saraalves.listagames.login.view.LoginActivity

class SplashActivity : AppCompatActivity() {

//    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//        auth = FirebaseAuth.getInstance()

        Handler(Looper.getMainLooper()).postDelayed({
//            if (auth.currentUser == null) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
//            } else {
//                val intent = Intent(this, ListaGamesActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
        }, 2000)
    }
}