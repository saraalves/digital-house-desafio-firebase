package com.saraalves.listagames.listagames

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saraalves.listagames.R
import com.saraalves.listagames.listagames.view.ListaGamesAdapter
import com.saraalves.listagames.listagames.viewmodel.ListaGamesViewModel

class ListaGamesActivity : AppCompatActivity() {

    private lateinit var _listaGamesAdapter: ListaGamesAdapter
    private lateinit var _listaGamesViewModel: ListaGamesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_games)


        val recyclerViewGames = findViewById<RecyclerView>(R.id.recyclerGames)
        val viewGridManager = GridLayoutManager(this, 2)


        recyclerViewGames.apply {
            setHasFixedSize(true)
            layoutManager = viewGridManager
            adapter = _listaGamesAdapter

        }

    }
}
