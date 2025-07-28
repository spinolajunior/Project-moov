package com.robertojr.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.robertojr.moov.R
import com.robertojr.moov.databinding.ActivityCriarConta2Binding
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

//                try {
//
//                    var userSenha = Login(email,null,senha,null,usuario)
//                    var valida = RetrofitClient.loginRetrofit.validateLogin(userSenha)
//
//                    if (valida.isSuccessful){
//
//                    }else{
//
//                    }
//
//                }catch (e: Exception){
//                    e.printStackTrace()
//                }




                intent.putExtra("nome", nome)
                intent.putExtra("sobrenome", sobrenome)
                intent.putExtra("phone", phone)
                intent.putExtra("dataNascimento", dataNascimento)
                intent.putExtra("usuario", usuario)
                intent.putExtra("senha", senha)
                intent.putExtra("email", email)


                startActivity(intent)


            }


        }

    }
}