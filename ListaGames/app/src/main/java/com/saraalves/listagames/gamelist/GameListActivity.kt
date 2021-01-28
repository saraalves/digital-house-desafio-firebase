package com.saraalves.listagames.gamelist

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.saraalves.listagames.R
import com.saraalves.listagames.detail.DetailGameActivity
import com.saraalves.listagames.gamelist.model.GameModel
import com.saraalves.listagames.gamelist.repository.GameRepository
import com.saraalves.listagames.gamelist.viewmodel.GameViewModel
import com.saraalves.listagames.savegame.SaveGameActivity

class GameListActivity : AppCompatActivity() {

    private val searchView: SearchView by lazy { findViewById<SearchView>(R.id.searchView) }
    private val swipeRefreshLayout: SwipeRefreshLayout by lazy { findViewById<SwipeRefreshLayout>(R.id.swipeToRefresh) }

    private val recyclerView: RecyclerView by lazy { findViewById<RecyclerView>(R.id.recyclerGames) }
    private val btnNewGame: FloatingActionButton by lazy { findViewById<FloatingActionButton>(R.id.btnNewGame) }

    private lateinit var _gameListAdapter: GameListAdapter
    private lateinit var _viewModel: GameViewModel

    private lateinit var ref: DatabaseReference
    private var databse = FirebaseDatabase.getInstance()
    private lateinit var auth: FirebaseAuth

    private val _gameList = mutableListOf<GameModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_list)

        auth = Firebase.auth
        ref = databse.getReference(auth.currentUser!!.uid)

        val manager = GridLayoutManager(this, 2)

        viewModelProvider()
        load()
        setUpNavigation()
        setUpRecyclerView(recyclerView, manager)
        getGames(ref, this, _gameList)

        btnNewGame.setOnClickListener {
            val intent = Intent(this,   SaveGameActivity::class.java)
            startActivity(intent)
        }
    }

    private fun load() {
        swipeRefreshLayout.setOnRefreshListener {
            SwipeRefreshLayout.OnRefreshListener {
                _gameList.clear()
            }
            getGames(ref, this@GameListActivity, _gameList)
            swipeRefreshLayout.isRefreshing = false
            _gameListAdapter.notifyDataSetChanged()
        }
    }

    private fun getGames(ref: DatabaseReference, context: Context, list: List<GameModel>) {
        _viewModel.getGames(ref, context, _gameList).observe(this) {
            list.let {_gameList.addAll(it)}
            _gameListAdapter.notifyDataSetChanged()
        }
    }

    private fun setUpRecyclerView(
        recyclerView: RecyclerView,
        viewLayoutManager: GridLayoutManager
    ) {
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewLayoutManager
            adapter = _gameListAdapter
        }
    }

    private fun setUpNavigation() {
        _gameListAdapter = GameListAdapter(_gameList) {
            val intent = Intent(this@GameListActivity, DetailGameActivity::class.java)
            intent.putExtra("NAME", it.nome)
            intent.putExtra("LANCAMENTO", it.dataLancamento)
            intent.putExtra("DESCRICAO", it.descricao)
            intent.putExtra("IMG_URL", it.imgUrl)
            startActivity(intent)
        }
    }

    private fun viewModelProvider() {
        _viewModel =
            ViewModelProvider(this, GameViewModel.GameViewModelFactory(GameRepository())).get(
                GameViewModel::class.java
            )
    }

    private fun searchByName(){

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                _viewModel.searchByName(query!!, this@GameListActivity, ref).observe(this@GameListActivity) {
                    _gameList.clear()
                    getGames(ref, this@GameListActivity, it)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText.isNullOrEmpty()){
                    _gameList.clear()
                    getGames(ref, this@GameListActivity, _viewModel.initialList())
                }
                return false
            }
        })
    }
}