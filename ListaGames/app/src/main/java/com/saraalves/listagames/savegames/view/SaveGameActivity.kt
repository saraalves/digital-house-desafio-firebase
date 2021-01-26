package com.saraalves.listagames.savegames.view

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.saraalves.listagames.R
import com.saraalves.listagames.listagames.ListaGamesActivity
import com.saraalves.listagames.listagames.model.GamesModel
import com.saraalves.listagames.listagames.repository.ListaGamesRepository
import com.saraalves.listagames.listagames.viewmodel.ListaGamesViewModel
import com.saraalves.listagames.register.RegisterActivity
import de.hdodenhof.circleimageview.CircleImageView

class SaveGameActivity : AppCompatActivity() {

    private lateinit var btnSave: MaterialButton
    private lateinit var etNameGame: TextInputEditText
    private lateinit var etDataGame: TextInputEditText
    private lateinit var etDescriptionGame: TextInputEditText
    private lateinit var tilNameGame: TextInputLayout
    private lateinit var tilDataGame: TextInputLayout
    private lateinit var tilDescriptionGame: TextInputLayout
    private lateinit var imgSaveGame: CircleImageView

    private lateinit var imageReference: String
    private var imageURI: Uri? = null

    private lateinit var _viewModel: ListaGamesViewModel
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var storage: FirebaseStorage

    private lateinit var user:  FirebaseUser
    private lateinit var userRef: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_game)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!

        storage = FirebaseStorage.getInstance()
        userRef = storage.getReference("uploads")

        database = FirebaseDatabase.getInstance()
        databaseRef = database.getReference("users")

        btnSave = findViewById(R.id.btnSave)
        etNameGame = findViewById(R.id.etNameGame)
        etDataGame = findViewById(R.id.etDataGame)
        etDescriptionGame = findViewById(R.id.etDescriptionGame)
        tilNameGame = findViewById(R.id.tilNameGame)
        tilDataGame = findViewById(R.id.tilDataGame)
        tilDescriptionGame = findViewById(R.id.tilDescriptionGame)
        imgSaveGame = findViewById(R.id.imgSaveGame)

        viewModelProvider()
        addUser(auth.currentUser!!.uid, database)
        getImage()
        saveGame()


    }

    private fun saveGame() {
        btnSave.setOnClickListener {
                enviarArquivo(userRef)
                val name = etNameGame.text.toString()
                val data = etDataGame.text.toString()
                val description = etDescriptionGame.text.toString()
            addGame(databaseRef, name, data, description, "")
            goHome()
        }
    }

    private fun addUser(userId: String, database: FirebaseDatabase){
        _viewModel.addUser(userId, database).observe(this) {
            databaseRef = it
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        goHome()
    }

    private fun addGame(
        ref: DatabaseReference,
        nome: String,
        data: String,
        description: String,
        imgURL: String
    ){
        _viewModel.addGame(nome, data, description, imgURL, ref).observe(this) {
            if (it) Toast.makeText(
                this@SaveGameActivity,
                "Game salvo com sucesso",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun goHome(){
        val intent = Intent(this, ListaGamesActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun getImage() {
        imgSaveGame.setOnClickListener() {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, CONTENT_REQUEST_CODE)
        }
    }


    fun enviarArquivo(storageReference: StorageReference) {
        if (imageURI != null) {
            imageURI?.run {

                val extension = MimeTypeMap.getSingleton()
                    .getExtensionFromMimeType(contentResolver.getType(imageURI!!))

                val fileReference =
                    storageReference.child(user.uid).child("${System.currentTimeMillis()}.${extension}")

                fileReference.putFile(this)
                    .addOnSuccessListener {
                        imageReference = fileReference.toString()
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            this@SaveGameActivity,
                            "Não foi possível carregar a imagem",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CONTENT_REQUEST_CODE && resultCode == RESULT_OK) {
            imageURI = data?.data
            findViewById<CircleImageView>(R.id.imgSaveGame).setImageURI(imageURI)
        }
    }

    private fun sendAndGetImg(userId: String, nameGame: String, imgURI: Uri, firebaseStorage: FirebaseStorage, contentResolver: ContentResolver, circleImageView: CircleImageView){
        _viewModel.addImg(userId, nameGame, imgURI, firebaseStorage, contentResolver, circleImageView).observe(this) {
            imageURI = it
        }
    }

    private fun viewModelProvider() {
        _viewModel =
            ViewModelProvider(this, ListaGamesViewModel.GameViewModelFactory(ListaGamesRepository())).get(
                ListaGamesViewModel::class.java
            )
    }

    companion object {
        const val CONTENT_REQUEST_CODE = 1
    }
}