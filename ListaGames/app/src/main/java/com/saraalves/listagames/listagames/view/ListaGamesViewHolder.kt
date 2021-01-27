package com.saraalves.listagames.listagames.view

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.saraalves.listagames.R
import com.saraalves.listagames.listagames.model.GamesModel
import com.squareup.picasso.Picasso

class ListaGamesViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val imagem: ImageView = view.findViewById(R.id.cardGame)
    private val nomeGame: TextView = view.findViewById(R.id.txtNameGame)
    private val anoGame: TextView = view.findViewById(R.id.dataAno)

    fun bind(game: GamesModel) {
        nomeGame.text = game.nome
        anoGame.text = game.ano
        carregarImagem(game.imgUrl, imagem)
    }

    private fun carregarImagem(img: String, imagem: ImageView?) {
        val storage = FirebaseStorage.getInstance().getReference("uploads")

        storage.child(img).downloadUrl.addOnSuccessListener {
            Picasso.get()
                .load(it)
                .into(imagem)
        }
    }
}

