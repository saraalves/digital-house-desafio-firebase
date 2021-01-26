package com.saraalves.listagames.login.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.saraalves.listagames.R
import com.saraalves.listagames.listagames.ListaGamesActivity
import com.saraalves.listagames.login.util.AppUtil
import com.saraalves.listagames.register.RegisterActivity
import com.saraalves.listagames.splash.SplashActivity


var LOGIN_TYPE = ""

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var saveUser: AppUtil

    private lateinit var checkBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        checkBox = findViewById(R.id.checkboxRemember)

        val prefs = getSharedPreferences(SplashActivity.APP_NAME, MODE_PRIVATE)
        checkBox.setOnCheckedChangeListener{ _, isChecked -> Unit
            prefs.edit().putBoolean(SplashActivity.NOTIFICATION_PREFS, isChecked).apply()
        }


        validaCampos()

        val textIrProCadastro = findViewById<TextView>(R.id.btnCreateAccoutn)
        textIrProCadastro.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            if (currentUser.isEmailVerified) {
                val intent = Intent(this, ListaGamesActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Valide seu email", Toast.LENGTH_LONG).show()
                auth.signOut()
            }
        }
    }


    // configuração login com email e senha

    private fun validaCampos() {
        val buttonLogin = findViewById<TextView>(R.id.btnLogin)
        buttonLogin.setOnClickListener {

            val email = findViewById<EditText>(R.id.etEmailLogin).text.toString()
            val senha = findViewById<EditText>(R.id.etSenhaLogin).text.toString()

            if (checarCamposVazios(email, senha)) {
                firebaseLoginSenha(email, senha)
            }
        }
    }

    private fun checarCamposVazios(email: String, senha: String): Boolean {

        if (email.trim().isEmpty()) {
            findViewById<EditText>(R.id.etEmailLogin).error = RegisterActivity.ERRO_VAZIO
            return false
        } else if (senha.trim().isEmpty()) {
            findViewById<EditText>(R.id.etSenhaLogin).error = RegisterActivity.ERRO_VAZIO
            return false
        }
        return true
    }

    private fun firebaseLoginSenha(email: String, senha: String) {
        auth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user!!.isEmailVerified) {
                        val intent = Intent(this@LoginActivity, ListaGamesActivity::class.java)
                        startActivity(intent)
                        finish()
                        LOGIN_TYPE = "SENHA"
                    } else Toast.makeText(baseContext, "Falha de autenticação", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(baseContext, "Falha de autenticação", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun esqueciSenha(email: String, alertDialog: AlertDialog?) {

        if (email.isNotEmpty()) {
            Firebase.auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Verifique seu email", Toast.LENGTH_SHORT)
                            .show()
                        alertDialog?.dismiss()
                    } else {
                        Toast.makeText(this, "Erro de envio", Toast.LENGTH_SHORT).show()
                        alertDialog?.dismiss()
                    }
                }
        } else {
            Toast.makeText(this, "Campo vazio", Toast.LENGTH_SHORT).show()
            alertDialog?.dismiss()
        }
    }

    companion object {
        private const val RC_SIGN_IN = 1
    }


}