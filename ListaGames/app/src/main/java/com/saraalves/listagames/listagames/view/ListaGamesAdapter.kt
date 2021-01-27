package com.saraalves.listagames.listagames.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.MultiAutoCompleteTextView
import androidx.recyclerview.widget.RecyclerView
import com.saraalves.listagames.R
import com.saraalves.listagames.listagames.model.GamesModel

class ListaGamesAdapter(
): RecyclerView.Adapter<ListaGamesViewHolder>(){

    private val _listaGames: MutableList<GamesModel> = mutableListOf()

    fun addGame(game: GamesModel) {
        _listaGames.add(game)
        notifyDataSetChanged()
    }

    fun adicionarGames(game: List<GamesModel>) {
        _listaGames.addAll(game)
        notifyDataSetChanged()
    }

    fun updateGame(games: GamesModel) {
        for (game in _listaGames  ) {
            if (game.nome == games.nome) {
                game.description = games.description
                break
            }
        }

        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaGamesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_games, parent, false)
        return ListaGamesViewHolder(view)
    }

    override fun getItemCount() = _listaGames.size


    override fun onBindViewHolder(holder: ListaGamesViewHolder, position: Int) {
       val item = _listaGames[position]
        holder.bind(item)
    }
}


