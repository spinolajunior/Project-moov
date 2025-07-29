package com.robertojr.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.robertojr.moov.R
import com.robertojr.moov.databinding.ActivityCriarConta2Binding
import com.robertojr.moov.model.Login
import com.robertojr.moov.model.RetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CriarContaActivity2 : AppCompatActivity() {
    lateinit var binding: ActivityCriarConta2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityCriarConta2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val nome = intent.getStringExtra("nome")
        val sobrenome = intent.getStringExtra("sobrenome")
        val phone = intent.getStringExtra("phone")
        val dataNascimento = intent.getStringExtra("dataNascimento")

        val watcher = {
            var usuario = binding.edtUsuarioCadastro.text.toString()
            var senha = binding.edtSenhaCadastro.text.toString()
            var senhaConfirma = binding.edtSenhaCadastroConfirmar.text.toString()
            var email = binding.edtEmail.text.toString()

            val regexUsuario = usuario.isNotEmpty()
            val regexSenha = senha.isNotEmpty()
            val regexSenhaConfirma = senhaConfirma.isNotEmpty()
            val regexEmail = email.isNotEmpty()

            val validate = regexUsuario and regexSenha and regexSenhaConfirma and regexEmail
            binding.btnCriarContaAvancar.isEnabled = validate
            binding.btnCriarContaAvancar.backgroundTintList =
                ContextCompat.getColorStateList(
                    this@CriarContaActivity2,
                    if (validate) R.color.amarelo_principal else R.color.btn_desativado
                )

            if (!regexUsuario) binding.edtUsuarioCadastro.error =
                getString(R.string.campoIncompleto)
            if (!regexSenha) binding.edtSenhaCadastro.error = getString(R.string.campoIncompleto)
            if (!regexSenhaConfirma) binding.edtSenhaCadastroConfirmar.error =
                getString(R.string.campoIncompleto)
            if (!regexEmail) binding.edtEmail.error = getString(R.string.campoIncompleto)

        }

        binding.edtUsuarioCadastro.addTextChangedListener { watcher() }
        binding.edtSenhaCadastro.addTextChangedListener { watcher() }
        binding.edtSenhaCadastroConfirmar.addTextChangedListener { watcher() }
        binding.edtEmail.addTextChangedListener { watcher() }



        binding.btnCriarContaAvancar.setOnClickListener {
            lifecycleScope.launch {
                val intent = Intent(this@CriarContaActivity2, CriarContaActivity3::class.java)

                var usuario = binding.edtUsuarioCadastro.text.toString()
                var senha = binding.edtSenhaCadastro.text.toString()
                var email = binding.edtEmail.text.toString()

                try {
                    var login = Login(email, null, senha, null, usuario)
                    var verificar = RetrofitClient.loginRetrofit.validateNewLogin(login)


                    val respostaBruta = verificar.errorBody()?.string() ?: verificar.body().toString()
                    Log.d("API_RESPONSE_RAW", respostaBruta)

                    if (verificar.isSuccessful) {

                        intent.putExtra("nome", nome)
                        intent.putExtra("sobrenome", sobrenome)
                        intent.putExtra("phone", phone)
                        intent.putExtra("dataNascimento", dataNascimento)
                        intent.putExtra("usuario", usuario)
                        intent.putExtra("senha", senha)
                        intent.putExtra("email", email)

                        delay(100)
                        startActivity(intent)

                    } else {

                        val erroBodyJson = verificar.errorBody()?.string()
                        val erroLogin = Gson().fromJson(erroBodyJson, Login::class.java)
                        Log.d("ERRO_LOGIN", "id = ${erroLogin?.id}")
                        Log.d("ERRO_LOGIN", "username = ${erroLogin?.userName}")
                        Log.d("ERRO_LOGIN", "password = ${erroLogin?.password}")
                        Log.d("ERRO_LOGIN", "email = ${erroLogin?.email}")
                        Log.d("ERRO_LOGIN", "userId = ${erroLogin?.userId}")

                        if (erroLogin?.email == null) {
                            binding.edtEmail.error = "Este email já esta vinculado a uma conta!"
                        }
                        if (erroLogin?.password == null) {
                            binding.edtSenhaCadastro.error =
                                "Esta senha já esta vinculada a uma conta!"
                        }
                        if (erroLogin?.userName == null) {
                            binding.edtUsuarioCadastro.error =
                                "Este usuario já esta vinculado a uma conta!"
                        }
                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }


        }

    }
}