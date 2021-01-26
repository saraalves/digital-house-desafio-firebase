package com.saraalves.listagames.listagames.repository

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.saraalves.listagames.listagames.model.GamesModel
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ListaGamesRepository {

    suspend fun addGame(ref: DatabaseReference, gameModel: GamesModel) {
        val newGame = ref.child(gameModel.nome)
        newGame.setValue(gameModel)
    }

    suspend fun getGames(ref: DatabaseReference, context: Context, list: MutableList<String>) {

    }

    suspend fun updateGame() {

    }

    suspend fun addUser(userId: String, databse: FirebaseDatabase): DatabaseReference {
        val ref = databse.getReference(userId)
        return ref
    }

    suspend fun showImg(imgURI: Uri, circleImageView: CircleImageView){
        circleImageView.setImageURI(imgURI)
    }

    suspend fun sendImg(userId: String, nameGame: String, imgURI: Uri, firebaseStorage: FirebaseStorage, contentResolver: ContentResolver, circleImageView: CircleImageView): Uri {
        imgURI.run {
            lateinit var imgUrl:Uri
            val extension =  MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(contentResolver.getType(imgURI))

            val storageRef = firebaseStorage.getReference("${userId}/imgGames")
            val fileRef = storageRef.child("${nameGame}.${extension}")

            fileRef.putFile(imgURI).addOnSuccessListener {
                imgUrl = getImage(userId, extension, nameGame, firebaseStorage, circleImageView)
            }
            return imgUrl
        }
    }

    fun getImage(userId: String, extension: String?, nameGame: String,firebaseStorage: FirebaseStorage, circleImageView: CircleImageView): Uri {
        lateinit var imgUrl: Uri
        val storageReference = firebaseStorage.getReference("${userId}/imgGames")
        storageReference.child("${nameGame}.${extension}").downloadUrl.addOnSuccessListener {
            Picasso.get().load(it).into(circleImageView)
            imgUrl = it
        }
        return imgUrl
    }
}