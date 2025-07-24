package com.robertojr.ui
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.robertojr.moov.databinding.ActivityLoginBinding
import com.robertojr.moov.model.RetrofitClient
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        lifecycleScope.launch {
            try {
                val logins = RetrofitClient.loginRetrofit.findAll()
                for (login in logins){
                    Log.d("API","Login { id: ${login.id}, username: ${login.username}, " +
                            "password: ${login.password}, Email: ${login.email}, UserId: ${login.userId}")
                }
            }catch (e: Exception){
                Log.d("Error","Exception")
                e.printStackTrace()
            }
        }


    }
}