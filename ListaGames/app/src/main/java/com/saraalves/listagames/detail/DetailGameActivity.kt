package com.saraalves.listagames.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.saraalves.listagames.R
import com.saraalves.listagames.editgame.EditGameActivity
import com.squareup.picasso.Picasso

class DetailGameActivity : AppCompatActivity() {

    private val btnEditGame: FloatingActionButton by lazy { findViewById<FloatingActionButton>(R.id.btnEditGame) }
    private val btnBack: ImageView by lazy { findViewById<ImageView>(R.id.btnBack) }
    private val imgGameDetail: ImageView by lazy { findViewById<ImageView>(R.id.imgGameDetail) }
    private val tvNomeDetail: TextView by lazy { findViewById<TextView>(R.id.tvNameGameDetalhes) }
    private val tvSecondNameDetail: TextView by lazy { findViewById<TextView>(R.id.tvSecondNameGameDetalhes) }
    private val tvAnoDetalhe: TextView by lazy { findViewById<TextView>(R.id.tvAnoDetalhe) }
    private val tvDescricaoDetalhe: TextView by lazy { findViewById<TextView>(R.id.tvDescription) }

    private lateinit var imgUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_game)

        val nome = intent.getStringExtra("NAME")
        val descricao = intent.getStringExtra("DESCRICAO")
        val data = intent.getStringExtra("LANCAMENTO")
        imgUrl = intent.getStringExtra("IMG_URL")!!

        btnBack.setOnClickListener {
            finish()
        }

        btnEditGame.setOnClickListener {
            editGame(nome, data, descricao)
        }

        tvNomeDetail.text = nome
        tvSecondNameDetail.text = nome
        tvAnoDetalhe.text = data
        tvDescricaoDetalhe.text = descricao
        Picasso.get().load(imgUrl).into(imgGameDetail)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun editGame(nome: String?, data: String?, descricao: String?){
        val intent = Intent(this, EditGameActivity::class.java)
        intent.putExtra("NAMEA", nome)
        intent.putExtra("LANCAMENTOA", data)
        intent.putExtra("DESCRICAOA", descricao)
        intent.putExtra("URL", imgUrl)
        startActivity(intent)
        finish()
    }
}