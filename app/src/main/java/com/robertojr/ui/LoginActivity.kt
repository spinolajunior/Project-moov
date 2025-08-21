// LoginActivity.kt
package com.robertojr.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.robertojr.moov.databinding.ActivityLoginBinding
import com.robertojr.moov.model.Login
import com.robertojr.moov.model.RetrofitClient
import com.robertojr.util.credentialData
import com.robertojr.util.userSection
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnLogin.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val login = Login(
                        null, null, binding.edtSenha.text.toString(),
                        null, binding.edtUsuario.text.toString()
                    )

                    val tryLogin = RetrofitClient.loginRetrofit.validateLogin(login)

                    if (tryLogin.isSuccessful) {
                        userSection = tryLogin.body()!!
                        val credential = RetrofitClient.loginRetrofit.findById(userSection.credentialId)
                        if (credential.isSuccessful) {
                            credentialData = credential.body()
                        }


                        saveLoginToFile(binding.edtUsuario.text.toString(), binding.edtSenha.text.toString())

                        val intent = Intent(this@LoginActivity, MapsHomeActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        binding.txtError.text = "Usuário ou senha inválido"
                        binding.txtError.visibility = View.VISIBLE
                        launch {
                            delay(3000)
                            binding.txtError.visibility = View.INVISIBLE
                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        binding.btnCriarConta.setOnClickListener {
            lifecycleScope.launch {
                delay(200)
                val intent = Intent(this@LoginActivity, CriarContaActivity1::class.java)
                startActivity(intent)
            }
        }
    }


    private fun saveLoginToFile(usuario: String, senha: String) {
        val json = JSONObject()
        json.put("usuario", usuario)
        json.put("senha", senha)

        val file = File(filesDir, "login.json")
        file.writeText(json.toString())
    }
}
