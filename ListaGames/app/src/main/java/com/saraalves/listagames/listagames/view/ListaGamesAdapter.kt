package com.saraalves.listagames.listagames.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.MultiAutoCompleteTextView
import androidx.recyclerview.widget.RecyclerView
import com.saraalves.listagames.R
import com.saraalves.listagames.listagames.model.GamesModel

class ListaGamesAdapter(
    private val _listaGames: MutableList<GamesModel>,
    private val _listener: (GamesModel) -> Unit
): RecyclerView.Adapter<ListaGamesViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaGamesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_games, parent, false)
        return ListaGamesViewHolder(view)
    }

    override fun getItemCount() = _listaGames.size


    override fun onBindViewHolder(holder: ListaGamesViewHolder, position: Int) {
       val item = _listaGames[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { _listener(item) }
    }
}


