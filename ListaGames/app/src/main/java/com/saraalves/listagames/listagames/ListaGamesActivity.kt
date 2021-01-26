package com.saraalves.listagames.listagames

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.saraalves.listagames.R
import com.saraalves.listagames.savegames.view.SaveGameActivity

class ListaGamesActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_games)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        storage = FirebaseStorage.getInstance()

        findViewById<FloatingActionButton>(R.id.btnAddGame).setOnClickListener{
            val intent = Intent(this, SaveGameActivity::class.java)
            startActivity(intent)
        }


    }

    // ---------> upload a file


//    Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
//    StorageReference riversRef = storageRef.child("images/rivers.jpg");
//
//    riversRef.putFile(file)
//    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//        @Override
//        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//            // Get a URL to the uploaded content
//            Uri downloadUrl = taskSnapshot.getDownloadUrl();
//        }
//    })
//    .addOnFailureListener(new OnFailureListener() {
//        @Override
//        public void onFailure(@NonNull Exception exception) {
//            // Handle unsuccessful uploads
//            // ...
//        }
//    });

    // -------> download a file

//    File localFile = File.createTempFile("images", "jpg");
//    riversRef.getFile(localFile)
//    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//        @Override
//        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//            // Successfully downloaded data to local file
//            // ...
//        }
//    }).addOnFailureListener(new OnFailureListener() {
//        @Override
//        public void onFailure(@NonNull Exception exception) {
//            // Handle failed download
//            // ...
//        }
//    });


}
