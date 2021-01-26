package com.saraalves.listagames.listagames.viewmodel

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.saraalves.listagames.listagames.model.GamesModel
import com.saraalves.listagames.listagames.repository.ListaGamesRepository
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.Dispatchers

class ListaGamesViewModel(private val _repository: ListaGamesRepository): ViewModel() {

    fun addUser(userId: String, databse: FirebaseDatabase) = liveData(Dispatchers.IO){
        _repository.addUser(userId, databse)
        emit(_repository.addUser(userId, databse))
    }


    fun addGame(nome: String, data: String, description: String, imgURL: String, ref: DatabaseReference) = liveData(Dispatchers.IO) {
        _repository.addGame(ref, GamesModel(nome, data, description, imgURL))
        emit(true)
    }

    fun addImg(userId: String, nameGame: String, imgURI: Uri, firebaseStorage: FirebaseStorage, contentResolver: ContentResolver, circleImageView: CircleImageView) = liveData(Dispatchers.IO) {
        val imgUrl = _repository.sendImg(userId, nameGame, imgURI, firebaseStorage, contentResolver, circleImageView)
        emit(imgUrl)
    }

    fun showImg(imgURI: Uri, circleImageView: CircleImageView) = liveData(Dispatchers.IO){
        _repository.showImg(imgURI, circleImageView)
        emit(true)
    }

    class GameViewModelFactory(private val repository: ListaGamesRepository): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ListaGamesViewModel(repository) as T
        }
    }
}