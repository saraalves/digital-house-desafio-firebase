package com.saraalves.listagames.listagames.view

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.saraalves.listagames.R
import com.saraalves.listagames.listagames.model.GamesModel
import com.squareup.picasso.Picasso

class ListaGamesViewHolder(private val view: View): RecyclerView.ViewHolder(view) {

    private val imagem: ImageView = view.findViewById(R.id.cardGame)
    private val nomeGame: TextView = view.findViewById(R.id.txtNameGame)
    private val anoGame: TextView = view.findViewById(R.id.dataAno)

    fun bind(game: GamesModel) {
        nomeGame.text = game.nome
        anoGame.text = game.ano

        Picasso.get()
            .load(game.imagem)
            .into(imagem)
    }
}
