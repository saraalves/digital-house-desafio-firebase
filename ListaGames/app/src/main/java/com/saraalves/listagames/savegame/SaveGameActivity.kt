package com.saraalves.listagames.savegame

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.saraalves.listagames.R
import com.saraalves.listagames.gamelist.repository.GameRepository
import com.saraalves.listagames.gamelist.viewmodel.GameViewModel
import com.saraalves.listagames.utils.Constantes
import de.hdodenhof.circleimageview.CircleImageView

class SaveGameActivity : AppCompatActivity() {

    private val btnSave: MaterialButton by lazy { findViewById<MaterialButton>(R.id.btnSave) }
    private val etNameGame: TextInputEditText by lazy { findViewById<TextInputEditText>(R.id.etNameGame) }
    private val etDataGame: TextInputEditText by lazy { findViewById<TextInputEditText>(R.id.etDataGame) }
    private val etDescriptionGame: TextInputEditText by lazy { findViewById<TextInputEditText>(R.id.etDescriptionGame) }
    private val img: CircleImageView by lazy { findViewById<CircleImageView>(R.id.imgGame) }

    private val database = Firebase.database
    private lateinit var auth: FirebaseAuth
    private lateinit var ref: DatabaseReference
    private val storage = Firebase.storage

    private lateinit var _viewModel: GameViewModel

    private lateinit var imgUri: Uri
    private var imgURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_game)

        auth = Firebase.auth

        viewModelProvider()
        addUser(auth.currentUser!!.uid, database)

        img.setOnClickListener {
            getImage()
        }

        btnSave.setOnClickListener {
            val name = etNameGame.text.toString()
            val data = etDataGame.text.toString()
            val description = etDescriptionGame.text.toString()

            if(camposVazios(name, data, description)) {
                noImage(imgURL)
                addGame(ref, name, data, description, imgURL)
            }
        }
    }

    private fun addUser(userId: String, database: FirebaseDatabase) {
        _viewModel.addUser(userId, database).observe(this, {
            ref = it
        })
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
    ) {
        _viewModel.addGame(nome, data, description, imgURL, ref).observe(this) {
            Toast.makeText(
                this@SaveGameActivity,
                "Game salvo com sucesso",
                Toast.LENGTH_SHORT
            ).show()

            goHome()
        }
    }

    private fun viewModelProvider() {
        _viewModel =
            ViewModelProvider(this, GameViewModel.GameViewModelFactory(GameRepository())).get(
                GameViewModel::class.java
            )
    }

    private fun goHome() {
        finish()
    }

    private fun getImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, Constantes.CAMERA_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constantes.CAMERA_REQUEST && resultCode == RESULT_OK) {
            imgUri = data?.data!!
            img.setImageURI(imgUri)
            sendImg(auth.currentUser!!.uid)
        } else {
            Toast.makeText(this, "Erro ao salvar imagem", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendImg(userId: String) {
        imgUri.run {

            val extension = MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(contentResolver.getType(imgUri))

            val storageRef = storage.getReference("${userId}/imgGames")
            val fileRef = storageRef.child("${System.currentTimeMillis()}.${extension}")

            fileRef.putFile(imgUri)
                .addOnSuccessListener {
                    fileRef.downloadUrl.addOnSuccessListener {
                        imgURL = it.toString()
                    }
                        .addOnFailureListener {
                            imgURL =
                                "https://www.solidbackgrounds.com/images/1024x600/1024x600-black-solid-color-background.jpg"
                            Toast.makeText(
                                this@SaveGameActivity,
                                "Erro ao salvar imagem",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this@SaveGameActivity,
                        "Erro ao salvar imagem",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun camposVazios(
        nome: String,
        data: String,
        description: String
    ): Boolean {
        return when {
            nome.isEmpty() -> {
                etNameGame.error = Constantes.ERRO_VAZIO
                false
            }
            data.isEmpty() -> {
                etDataGame.error = Constantes.ERRO_VAZIO
                false
            }
            description.isEmpty() -> {
                etDescriptionGame.error = Constantes.ERRO_VAZIO
                false
            }
            else -> {
                true
            }
        }
    }

    private fun noImage(imgPath: String){
        if(imgPath.isEmpty()){
            imgURL = "https://www.solidbackgrounds.com/images/1024x600/1024x600-black-solid-color-background.jpg"
        }
    }

}