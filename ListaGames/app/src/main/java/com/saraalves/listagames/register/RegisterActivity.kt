package com.saraalves.listagames.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.saraalves.listagames.R
import com.saraalves.listagames.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private val btnCreateAccount: MaterialButton by lazy { findViewById(R.id.btnCreate) }
    private val etEmailRegister: TextInputEditText by lazy { findViewById(R.id.etEmailRegister) }
    private val etSenhaRegister: TextInputEditText by lazy { findViewById(R.id.etSenhaRegister) }
    private val etRepeateSenha: TextInputEditText by lazy { findViewById(R.id.etSenhaRepeateRegister) }
    private val etNomeRegister: TextInputEditText by lazy { findViewById(R.id.etNameRegister) }

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth

        btnCreateAccount.setOnClickListener {

            val nome = etNomeRegister.text.toString()
            val email = etEmailRegister.text.toString()
            val senha = etSenhaRegister.text.toString()
            val senhaRepeate = etRepeateSenha.text.toString()

            if (checarCampos(nome, email, senha, senhaRepeate)) {
                createAccount(nome, email, senha)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun camposVazios(
        nome: String,
        email: String,
        senha: String,
        senhaRepeate: String
    ): Boolean {
        if (nome.isEmpty()) {
            etNomeRegister.error = ERRO_VAZIO
            return false
        } else if (email.isEmpty()) {
            etEmailRegister.error = ERRO_VAZIO
            return false
        } else if (senha.isEmpty()) {
            etSenhaRegister.error = ERRO_VAZIO
            return false
        } else if (senhaRepeate.isEmpty()) {
            etRepeateSenha.error = ERRO_VAZIO
            return false
        } else {
            return true
        }
    }

    private fun senhasIguais(senha: String, senhaRepeate: String): Boolean {
        return if (senha == senhaRepeate) {
            true
        } else {
            Toast.makeText(this@RegisterActivity, ERRO_DIFERENTES, Toast.LENGTH_SHORT).show()
            etSenhaRegister.text!!.clear()
            etRepeateSenha.text!!.clear()
            false
        }
    }

    private fun checarCampos(
        nome: String,
        email: String,
        senha: String,
        senhaRepeate: String
    ): Boolean {
        if (camposVazios(nome, email, senha, senhaRepeate)) {
            if (senhasIguais(senha, senhaRepeate)) {
                return true
            }
        }
        return false
    }

    private fun createAccount(nome: String, email: String, senha: String) {
        auth.createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(baseContext, "Usuário criado com sucesso! Cheque seu email para autenticação", Toast.LENGTH_SHORT).show()
                    sendEmail(user!!)
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(baseContext, "Erro ao criar usuário", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun sendEmail(user: FirebaseUser) {
        user.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        baseContext,
                        "Usuário criado com sucesso! Cheque seu email para verificação",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    companion object {
        const val ERRO_VAZIO = "Campo vazio"
        const val ERRO_DIFERENTES = "As senhas precisam ser iguais"
    }
}