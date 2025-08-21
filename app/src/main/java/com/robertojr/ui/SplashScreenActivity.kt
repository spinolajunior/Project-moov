// SplashScreenActivity.kt
package com.robertojr.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.robertojr.moov.databinding.ActivitySplashScreenBinding
import com.robertojr.moov.model.Login
import com.robertojr.moov.model.RetrofitClient
import com.robertojr.util.credentialData
import com.robertojr.util.userSection
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.hide()
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            checkLogin()
        }
    }


    private suspend fun checkLogin() {
        val file = File(filesDir, "login.json")

        if (file.exists()) {
            try {
                val content = file.readText()
                val json = JSONObject(content)
                val usuario = json.getString("usuario")
                val senha = json.getString("senha")

                val login = Login(null, null, senha, null, usuario)
                val tryLogin = RetrofitClient.loginRetrofit.validateLogin(login)

                if (tryLogin.isSuccessful) {
                    userSection = tryLogin.body()!!
                    val credential = RetrofitClient.loginRetrofit.findById(userSection.credentialId)
                    if (credential.isSuccessful) {
                        credentialData = credential.body()
                    }


                    startActivity(Intent(this, MapsHomeActivity::class.java))
                    finish()
                    return
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
