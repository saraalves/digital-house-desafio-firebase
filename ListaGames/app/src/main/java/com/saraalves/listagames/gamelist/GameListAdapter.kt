package com.saraalves.listagames.gamelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saraalves.listagames.R
import com.saraalves.listagames.gamelist.model.GameModel

class GameListAdapter(
    private var _gameList: MutableList<GameModel>,
    var listener: (GameModel) -> Unit
) : RecyclerView.Adapter<HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        return HomeViewHolder(view)
    }

    override fun getItemCount() = _gameList.size

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = _gameList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { listener(item) }
    }
}