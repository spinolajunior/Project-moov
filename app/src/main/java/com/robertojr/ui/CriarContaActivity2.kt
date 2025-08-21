package com.robertojr.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.robertojr.moov.R
import com.robertojr.moov.databinding.ActivityCriarConta2Binding
import com.robertojr.moov.model.Login
import com.robertojr.moov.model.RetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CriarContaActivity2 : AppCompatActivity() {
    lateinit var binding: ActivityCriarConta2Binding

    // Variáveis para rastrear o estado dos campos
    private var isUsuarioValid = false
    private var isEmailValid = false
    private var isSenhaValid = false
    private var isConfirmaSenhaValid = false

    // Regexes em constantes para reutilização
    private val regexUsuario = Regex("^[a-zA-Z0-9._-]{3,20}$")
    private val regexEmail = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
    private val regexSenha = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=!])(?=.{8,15}).*$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityCriarConta2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val nome = intent.getStringExtra("nome")
        val sobrenome = intent.getStringExtra("sobrenome")
        val phone = intent.getStringExtra("phone")
        val dataNascimento = intent.getStringExtra("dataNascimento")

        // Configuração dos TextWatchers
        binding.edtUsuarioCadastro.addTextChangedListener {
            isUsuarioValid = it.toString().matches(regexUsuario)
            if (!isUsuarioValid) {
                binding.edtUsuarioCadastro.error = getString(R.string.error_usuario_invalido)
            } else {
                binding.edtUsuarioCadastro.error = null
            }
            ativarBtn()
        }

        binding.edtEmail.addTextChangedListener {
            isEmailValid = it.toString().matches(regexEmail)
            if (!isEmailValid) {
                binding.edtEmail.error = getString(R.string.error_email_invalido)
            } else {
                binding.edtEmail.error = null
            }
            ativarBtn()
        }

        binding.edtSenhaCadastro.addTextChangedListener {
            isSenhaValid = it.toString().matches(regexSenha)
            isConfirmaSenhaValid = it.toString() == binding.edtSenhaCadastroConfirmar.text.toString()
            if (!isSenhaValid) {
                binding.edtSenhaCadastro.error = getString(R.string.error_senha_invalida_cadastro)
            } else {
                binding.edtSenhaCadastro.error = null
            }
            ativarBtn()
        }

        binding.edtSenhaCadastroConfirmar.addTextChangedListener {
            isConfirmaSenhaValid = it.toString() == binding.edtSenhaCadastro.text.toString()
            if (!isConfirmaSenhaValid) {
                binding.edtSenhaCadastroConfirmar.error = getString(R.string.error_senhas_nao_conferem)
            } else {
                binding.edtSenhaCadastroConfirmar.error = null
            }
            ativarBtn()
        }

        binding.btnCriarContaAvancar.setOnClickListener {
            // A lógica de envio de dados permanece a mesma
            lifecycleScope.launch {
                val intent = Intent(this@CriarContaActivity2, CriarContaActivity3::class.java)

                val usuario = binding.edtUsuarioCadastro.text.toString()
                val senha = binding.edtSenhaCadastro.text.toString()
                val email = binding.edtEmail.text.toString()

                try {
                    val login = Login(email, null, senha, null, usuario)
                    val verificar = RetrofitClient.loginRetrofit.validateNewLogin(login)

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
                        binding.txtFailureNewCredenciais.visibility = View.VISIBLE
                        delay(500)
                        binding.txtFailureNewCredenciais.visibility = View.INVISIBLE
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        binding.btnVoltarLogin.setOnClickListener {
            val intent = Intent(this@CriarContaActivity2, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    private fun ativarBtn() {
        val validarCampos = isUsuarioValid && isEmailValid && isSenhaValid && isConfirmaSenhaValid
        binding.btnCriarContaAvancar.isEnabled = validarCampos
        binding.btnCriarContaAvancar.backgroundTintList =
            ContextCompat.getColorStateList(
                this,
                if (validarCampos) R.color.amarelo_principal else R.color.btn_desativado
            )
    }
}