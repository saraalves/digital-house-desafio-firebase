package com.saraalves.listagames.savegames.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
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
        imgSaveGame.setOnClickListener() {
            procurarArquivo()
        }




    }

//    private fun checarCamposVazios(
//        nameGame: String,
//        ano: String,
//        descricao: String,
//
//    ): Boolean {
//
//        if (nome.trim().isEmpty()) {
//            findViewById<EditText>(R.id.etNameRegister).error = RegisterActivity.ERRO_VAZIO
//            return false
//        } else if (email.trim().isEmpty()) {
//            findViewById<EditText>(R.id.etEmailRegister).error = RegisterActivity.ERRO_VAZIO
//            return false
//        } else if (senha.trim().isEmpty()) {
//            findViewById<EditText>(R.id.etSenhaRegister).error = RegisterActivity.ERRO_VAZIO
//            return false
//        } else if (senhaRepeat.trim().isEmpty()) {
//            findViewById<EditText>(R.id.etSenhaRepeateRegister).error = RegisterActivity.ERRO_VAZIO
//            return false
//        }
//        return true
//    }

    fun procurarArquivo() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, CONTENT_REQUEST_CODE)
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