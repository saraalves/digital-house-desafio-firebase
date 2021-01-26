package com.saraalves.listagames.listagames

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.saraalves.listagames.R
import com.saraalves.listagames.R.id.btnAddGame
import com.saraalves.listagames.detalhes.view.DetalhesGamesActivity
import com.saraalves.listagames.listagames.model.GamesModel
import com.saraalves.listagames.listagames.repository.ListaGamesRepository
import com.saraalves.listagames.listagames.view.ListaGamesAdapter
import com.saraalves.listagames.listagames.viewmodel.ListaGamesViewModel
import com.saraalves.listagames.savegames.view.SaveGameActivity

class ListaGamesActivity : AppCompatActivity() {


    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAddGame: FloatingActionButton


    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var storage: FirebaseStorage

    private lateinit var _viewModel: ListaGamesViewModel

    private lateinit var _listaGamesAdapter: ListaGamesAdapter

    private val _listGames = mutableListOf<GamesModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_games)

        val manager = GridLayoutManager(this, 2)

        searchView = findViewById(R.id.searchView)
        recyclerView = findViewById(R.id.recyclerGames)
        btnAddGame = findViewById(R.id.btnAddGame)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        storage = FirebaseStorage.getInstance()

        viewModelProvider()
        setUpNavigation()
        setUpRecyclerView(recyclerView, manager)
        addGame()

    }

    private fun addGame() {
        btnAddGame.setOnClickListener {
            val intent = Intent(this, SaveGameActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView, viewLayoutManager: GridLayoutManager){
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewLayoutManager
            adapter = _listaGamesAdapter
        }
    }

    private fun setUpNavigation(){
        _listaGamesAdapter = ListaGamesAdapter(_listGames){
            val intent = Intent(this@ListaGamesActivity, DetalhesGamesActivity::class.java)
            intent.putExtra("NAME", it.nome)
            intent.putExtra("LANCAMENTO", it.ano)
            intent.putExtra("DESCRICAO", it.description)
            intent.putExtra("IMG_URL", it.imgUrl)
            startActivity(intent)
        }
    }


    private fun viewModelProvider() {
        _viewModel =
            ViewModelProvider(this, ListaGamesViewModel.GameViewModelFactory(ListaGamesRepository())).get(
                ListaGamesViewModel::class.java
            )
    }
}
