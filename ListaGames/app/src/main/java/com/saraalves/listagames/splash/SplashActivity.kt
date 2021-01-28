package com.saraalves.listagames.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.saraalves.listagames.R
import com.saraalves.listagames.gamelist.GameListActivity
import com.saraalves.listagames.login.LoginActivity
import com.saraalves.listagames.utils.Constantes


class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth = Firebase.auth
        val user = auth.currentUser

        val prefs = getSharedPreferences(Constantes.APP_NAME, MODE_PRIVATE)
        val prefsChecked = prefs.getBoolean(Constantes.NOTIFICATION_PREFS, false)

        Handler(Looper.getMainLooper()).postDelayed({
            if (prefsChecked && user != null) {
                val intent = Intent(this, GameListActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        },3000)
    }

}