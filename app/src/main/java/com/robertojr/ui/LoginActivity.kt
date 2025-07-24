package com.robertojr.ui
import android.os.Bundle
import android.util.Log
import android.util.Log.e
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.robertojr.moov.databinding.ActivityLoginBinding
import com.robertojr.moov.model.Login
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
                val logins = RetrofitClient.loginRetrofit.validateLogin(Login(null,null,
                    "120777Ce'",null,"spinola147"))

                    Log.d(null,String.format("id:%d%n" +
                            "name:%s%n" +
                            "lastName:%s%n",logins.id,logins.name,logins.lastName))

            }catch (e: Exception){
                Log.d("Error","Exception")
                e.printStackTrace()
            }
        }


    }
}