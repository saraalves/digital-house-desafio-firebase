package com.saraalves.listagames.editgame

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
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.saraalves.listagames.R
import com.saraalves.listagames.gamelist.GameListActivity
import com.saraalves.listagames.gamelist.repository.GameRepository
import com.saraalves.listagames.gamelist.viewmodel.GameViewModel
import com.saraalves.listagames.register.RegisterActivity
import com.saraalves.listagames.utils.Constantes
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class EditGameActivity : AppCompatActivity() {

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


        val nomeAtual = intent.getStringExtra("NAMEA")
        val descricaoAtual = intent.getStringExtra("DESCRICAOA")
        val dataAtual = intent.getStringExtra("LANCAMENTOA")
        val imgAtual = intent.getStringExtra("URL")

        etNameGame.setText(nomeAtual)
        etDataGame.setText(dataAtual)
        etDescriptionGame.setText(descricaoAtual)
        Picasso.get().load(imgAtual).into(img)


        viewModelProvider()

        auth = Firebase.auth
        val path = nomeAtual!!.toLowerCase() + "-" + dataAtual
        ref = database.getReference(auth.currentUser!!.uid).child(path)
        val refId = database.getReference(auth.currentUser!!.uid)

        img.setOnClickListener {
            getImage()
        }

        btnSave.setOnClickListener {
            val name = etNameGame.text.toString()
            val data = etDataGame.text.toString()
            val description = etDescriptionGame.text.toString()

            if(camposVazios(name, data, description)) {
                if(imgURL.isEmpty()){imgURL = imgAtual!!}
                editaGame(ref, name, data, description, imgURL, refId)
            }
        }
    }

    private fun camposVazios(
        nome: String,
        data: String,
        description: String
    ): Boolean {
        if (nome.isEmpty()) {
            etNameGame.error = Constantes.ERRO_VAZIO
            return false
        } else if (data.isEmpty()) {
            etDataGame.error = Constantes.ERRO_VAZIO
            return false
        } else if (description.isEmpty()) {
            etDescriptionGame.error = Constantes.ERRO_VAZIO
            return false
        } else {
            return true
        }
    }

    private fun noImage(imgPath: String){
        if(imgPath.isEmpty()){
            imgURL = "https://www.solidbackgrounds.com/images/1024x600/1024x600-black-solid-color-background.jpg"
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun editaGame(
        ref: DatabaseReference,
        nome: String,
        data: String,
        description: String,
        imgURL: String, refId: DatabaseReference
    ) {
        _viewModel.editGame(nome, data, description, imgURL, ref, refId).observe(this) {
            Toast.makeText(
                this@EditGameActivity,
                "Game alterado com sucesso",
                Toast.LENGTH_SHORT
            ).show()

            goList()
        }
    }

    private fun viewModelProvider() {
        _viewModel =
            ViewModelProvider(this, GameViewModel.GameViewModelFactory(GameRepository())).get(
                GameViewModel::class.java
            )
    }

    private fun goList() {
        val intent = Intent(this, GameListActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun getImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, Constantes.CAMERA_REQUEST_EDIT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constantes.CAMERA_REQUEST_EDIT && resultCode == RESULT_OK) {
            imgUri = data?.data!!
            img.setImageURI(imgUri)
            enviarArquivo(auth.currentUser!!.uid)
        } else {
            Toast.makeText(this, "Erro ao salvar imagem", Toast.LENGTH_SHORT).show()
        }
    }

    private fun enviarArquivo(userId: String) {
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
                                this@EditGameActivity,
                                "Erro ao salvar imagem",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this@EditGameActivity,
                        "Erro ao salvar imagem",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}