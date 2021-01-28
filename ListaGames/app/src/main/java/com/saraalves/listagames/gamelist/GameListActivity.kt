package com.saraalves.listagames.gamelist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.saraalves.listagames.R
import com.saraalves.listagames.savegame.SaveGameActivity

class GameListActivity : AppCompatActivity() {

    private val btnNewGame: FloatingActionButton by lazy { findViewById(R.id.btnNewGame) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_list)

        btnNewGame.setOnClickListener {
            val intent = Intent(this,   SaveGameActivity::class.java)
            startActivity(intent)
        }
    }
}