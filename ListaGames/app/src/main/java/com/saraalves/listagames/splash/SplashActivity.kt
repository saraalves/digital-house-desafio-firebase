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

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        val prefs = getSharedPreferences(APP_NAME, MODE_PRIVATE)
        val prefsChecked = prefs.getBoolean(NOTIFICATION_PREFS, false)

        Handler(Looper.getMainLooper()).postDelayed({
            if (prefsChecked && user != null) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, ListaGamesActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 2000)
    }

    companion object{
        const val APP_NAME = "ListaGames"
        const val NOTIFICATION_PREFS = "NOTIFICATION_PREFS"
    }
}