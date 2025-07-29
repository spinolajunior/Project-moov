package com.robertojr.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.robertojr.moov.databinding.ActivityLoginBinding
import com.robertojr.moov.model.Login
import com.robertojr.moov.model.RetrofitClient
import com.robertojr.util.userSection
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

                    val tryLogin = RetrofitClient.loginRetrofit.validateLogin(
                        login
                    )

                    if (tryLogin.isSuccessful ){
                        Log.d("Resposta API","${tryLogin.body()?.name}")
                            userSection = tryLogin.body()!!
                        launch {
                            val intent = Intent(this@LoginActivity, MapsHomeActivity::class.java)
                            startActivity(intent)
                        }

                    }else{
                        binding.txtError.text = "Usuario ou senha invalido"
                        binding.txtError.visibility = View.VISIBLE
                        launch {
                            delay(3000)
                            binding.txtError.visibility = View.INVISIBLE
                        }

                    }

                } catch (e: Exception) {
                    Log.d("Error", "Exception")
                    e.printStackTrace()
                }
            }


        }

        binding.btnCriarConta.setOnClickListener{
            lifecycleScope.launch {
                delay(200)
                val intent = Intent(this@LoginActivity, CriarContaActivity1::class.java)
                startActivity(intent)
            }
        }


    }
}