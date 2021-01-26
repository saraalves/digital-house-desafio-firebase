package com.saraalves.listagames.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.saraalves.listagames.R
import com.saraalves.listagames.login.view.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth

        validaCampos()
    }

    private fun validaCampos() {

        val btnCadastro = findViewById<MaterialButton>(R.id.btnCreate)

        btnCadastro.setOnClickListener {
            val nome = findViewById<EditText>(R.id.etNameRegister).text.toString()
            val email = findViewById<EditText>(R.id.etEmailRegister).text.toString()
            val senha = findViewById<EditText>(R.id.etSenhaRegister).text.toString()
            val senhaConferir = findViewById<EditText>(R.id.etSenhaRepeateRegister).text.toString()

            if (checarCamposVazios(nome, email, senha, senhaConferir)) {
                if (checarQtdDigitosSenha(8, senha)) {
                    if (senhasIguais(senha, senhaConferir)) {
                        criarCadastroFirebase(nome, email, senha)
                    }
                }
            }
        }

    }

    private fun checarCamposVazios(
        nome: String,
        email: String,
        senha: String,
        senhaRepeat: String
    ): Boolean {

        if (nome.trim().isEmpty()) {
            findViewById<EditText>(R.id.etNameRegister).error = ERRO_VAZIO
            return false
        } else if (email.trim().isEmpty()) {
            findViewById<EditText>(R.id.etEmailRegister).error = ERRO_VAZIO
            return false
        } else if (senha.trim().isEmpty()) {
            findViewById<EditText>(R.id.etSenhaRegister).error = ERRO_VAZIO
            return false
        } else if (senhaRepeat.trim().isEmpty()) {
            findViewById<EditText>(R.id.etSenhaRepeateRegister).error = ERRO_VAZIO
            return false
        }
        return true
    }

    private fun checarQtdDigitosSenha(qtdDigitos:Int, senha: String): Boolean {
        if(senha.length >= qtdDigitos){
            return true
        } else {
            findViewById<EditText>(R.id.etSenhaRegister).text.clear()
            findViewById<EditText>(R.id.etSenhaRepeateRegister).text.clear()
            findViewById<EditText>(R.id.etSenhaRegister).error = ERRO_DIGITOS
            return false
        }
    }

    private fun senhasIguais(senha: String, senhaRepeat: String): Boolean {
        if(senha == senhaRepeat){
            return true
        } else {
            Toast.makeText(this@RegisterActivity, "As senhas devem ser iguais", Toast.LENGTH_SHORT)
                .show()
            findViewById<EditText>(R.id.etSenhaRegister).text.clear()
            findViewById<EditText>(R.id.etSenhaRepeateRegister).text.clear()
            return false
        }
    }

    private fun criarCadastroFirebase(nome: String, email: String, senha: String){

        auth.createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    val profileUpdates = userProfileChangeRequest {
                        displayName = nome
                    }

                    user!!.updateProfile(profileUpdates).addOnCompleteListener {
                        Toast.makeText(baseContext, "Usuário criado com sucesso", Toast.LENGTH_SHORT).show()
                        sendEmail()
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    Toast.makeText(baseContext, "Erro ao criar usuário", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun sendEmail(){
        val user = Firebase.auth.currentUser

        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                }
            }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        const val ERRO_VAZIO = "Campo Vazio"
        const val ERRO_DIGITOS = "O campo deve ter no mínimo 8 dígitos"
    }
}