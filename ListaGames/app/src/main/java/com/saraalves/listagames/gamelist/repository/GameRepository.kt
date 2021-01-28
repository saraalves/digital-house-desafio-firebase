package com.saraalves.listagames.gamelist.repository

import android.content.Context
import android.widget.Toast
import com.google.firebase.database.*
import com.saraalves.listagames.gamelist.model.GameModel
import kotlinx.coroutines.delay
import java.util.*

class GameRepository {

    fun addGame(ref: DatabaseReference, gameModel: GameModel) {
        val path = gameModel.nome.toLowerCase(Locale.ROOT) + "_" + gameModel.dataLancamento
        val newGame = ref.child(path)
        newGame.setValue(gameModel)
    }

    fun editGame(ref: DatabaseReference, gameModel: GameModel, refUser: DatabaseReference){
        ref.removeValue()
        addGame(refUser, gameModel)
    }

    suspend fun getGames(
        ref: DatabaseReference,
        context: Context,
        list: MutableList<GameModel>
    ): MutableList<GameModel> {

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                for (dataSnapshot1 in dataSnapshot.children) {
                    val game: GameModel? = dataSnapshot1.getValue(GameModel::class.java)
                    list.add(game!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Erro ao carregar lista", Toast.LENGTH_SHORT).show()
            }
        })
        delay(1500)
        return list
    }

    fun addUser(userId: String, databse: FirebaseDatabase): DatabaseReference {
        return databse.getReference(userId)
    }

    suspend fun searchByName(nome: String, ref: DatabaseReference, list: MutableList<GameModel>, context: Context): MutableList<GameModel> {
        val queryCamp = "nome"

        val query = ref.child(nome.toLowerCase(Locale.ROOT)).orderByChild(queryCamp).equalTo(nome.toLowerCase(Locale.ROOT))

        query.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (dataSnapshot1 in snapshot.children) {
                    val game: GameModel? = dataSnapshot1.getValue(GameModel::class.java)
                    list.add(game!!)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Erro ao carregar lista", Toast.LENGTH_SHORT).show()
            }
        })
        delay(1500)
        return list
    }
}

