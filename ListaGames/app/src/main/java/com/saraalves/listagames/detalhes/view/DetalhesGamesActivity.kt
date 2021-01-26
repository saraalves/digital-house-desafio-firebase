package com.saraalves.listagames.detalhes.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.saraalves.listagames.R
import org.w3c.dom.Text

class DetalhesGamesActivity : AppCompatActivity() {

    private lateinit var btnGoBack: MaterialToolbar
    private lateinit var btnEditGame: FloatingActionButton
    private lateinit var titleNameGameDetalhes: TextView
    private lateinit var txtNameGameDetalhes: TextView
    private lateinit var txtAnoLancamento: TextView
    private lateinit var txtDescription: TextView
    private lateinit var imgGameDetail: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_games)
    }
}