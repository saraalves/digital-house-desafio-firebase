package com.saraalves.listagames.savegames.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
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
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    private lateinit var user:  FirebaseUser
    private lateinit var userRef: StorageReference
    private lateinit var databaseRef: DatabaseReference
    private lateinit var imageReference: String
    private var imageURI: Uri? = null


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

        getImage()
        salvarGame()

    }

    private fun salvarGame() {
        btnSave.setOnClickListener() {
            if (checarCamposVazios()) {
                enviarArquivo(userRef)
                enviarGame(
                    databaseRef,
                    etNameGame.text.toString(),
                    etDataGame.text.toString(),
                    etDescriptionGame.text.toString(),
                    imageReference
                )
                val intent = Intent(this, ListaGamesActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Error validar inputs", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getImage() {
        imgSaveGame.setOnClickListener() {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, CONTENT_REQUEST_CODE)
        }
    }

    private fun checarCamposVazios(): Boolean {

        if (etNameGame.text?.trim()!!.isEmpty()) {
            findViewById<EditText>(R.id.etNameGame).error = RegisterActivity.ERRO_VAZIO
            return false
        } else if (etDataGame.text?.trim()!!.isEmpty()) {
            findViewById<EditText>(R.id.etDataGame).error = RegisterActivity.ERRO_VAZIO
            return false
        } else if (etDescriptionGame.text?.trim()!!.isEmpty()) {
            findViewById<EditText>(R.id.etDescriptionGame).error = RegisterActivity.ERRO_VAZIO
            return false
        } else if (imageReference == null) {
            findViewById<EditText>(R.id.imgSaveGame).error = RegisterActivity.ERRO_VAZIO
            return false
        }
        return true
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

    fun enviarGame(
        databaseRef: DatabaseReference,
        name: String,
        date: String,
        description: String,
        image: String
    ) {
        val newGame = GamesModel(name, date, description, image)
        databaseRef.child(user.uid).child(name).setValue(newGame)

    }


//    // Write a message to the database
//    FirebaseDatabase database = FirebaseDatabase.getInstance();
//    DatabaseReference myRef = database.getReference("message");
//
//    myRef.setValue("Hello, World!");

//    // Read from the database
//    myRef.addValueEventListener(new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//            // This method is called once with the initial value and again
//            // whenever data at this location is updated.
//            String value = dataSnapshot.getValue(String.class);
//            Log.d(TAG, "Value is: " + value);
//        }
//
//        @Override
//        public void onCancelled(DatabaseError error) {
//            // Failed to read value
//            Log.w(TAG, "Failed to read value.", error.toException());
//        }
//    });

    companion object {
        const val CONTENT_REQUEST_CODE = 1
    }
}