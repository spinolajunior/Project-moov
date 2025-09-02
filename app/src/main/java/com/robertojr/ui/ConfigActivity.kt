package com.robertojr.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.robertojr.moov.R
import com.robertojr.moov.databinding.ActivityOptionsBinding
import com.robertojr.moov.model.Customer
import com.robertojr.moov.model.Driver
import com.robertojr.moov.model.Login
import com.robertojr.moov.model.RetrofitClient
import com.robertojr.util.credentialData
import com.robertojr.util.userSection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConfigActivity : AppCompatActivity() {
    lateinit var binding: ActivityOptionsBinding

    private var isNomeValid = false
    private var isEmailValid = false
    private var isPhoneValid = false
    private var isSenhaAtualValid = false
    private var isNovaSenhaValid = false
    private var isPlacaValid = false
    private var isCarroValid = false

    private val regexNome = Regex("^[A-Za-zÀ-ÖØ-öø-ÿ]{2,}(\\s[A-Za-zÀ-ÖØ-öø-ÿ]{2,})*\$")
    private val regexEmail = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
    private val regexPhone = Regex("^\\(?\\d{2}\\)?[\\s-]?9\\d{4}-?\\d{4}\$")
    private val regexNovaSenha = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^\\da-zA-Z\\s])(?=.{8,}).*\$")
    private val regexPlaca = Regex("^([A-Z]{3}-?\\d{4}|[A-Z]{3}\\d[A-Z]\\d{2})$")
    private val regexNovoModeloCarro = Regex("^(?=.*\\d.*\\d)(?!.*--)(?!.*-$)[a-zA-Z0-9\\s-]*$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setHint()
        configurarTextWatchers()
        configurarBotoes()
    }

    private fun configurarTextWatchers() {
        binding.edtEditarNome.addTextChangedListener {
            isNomeValid = it.toString().isNotEmpty() && it.toString().matches(regexNome)
            binding.edtEditarNome.error = if (!isNomeValid) getString(R.string.error_nome_invalido) else null
            ativarBtn()
        }

        binding.edtEditarEmail.addTextChangedListener {
            isEmailValid = it.toString().isNotEmpty() && it.toString().matches(regexEmail)
            binding.edtEditarEmail.error = if (!isEmailValid) getString(R.string.error_email_invalido) else null
            ativarBtn()
        }

        binding.edtEditarPhone.addTextChangedListener {
            isPhoneValid = it.toString().isNotEmpty() && it.toString().matches(regexPhone)
            binding.edtEditarPhone.error = if (!isPhoneValid) getString(R.string.error_phone_invalido) else null
            ativarBtn()
        }

        binding.edtSenhaAtual.addTextChangedListener {
            isSenhaAtualValid = it.toString().isNotEmpty() && it.toString() == credentialData?.password
            binding.edtSenhaAtual.error = if (!isSenhaAtualValid) getString(R.string.error_senha_atual) else null
            ativarBtn()
        }

        binding.edtNovaSenha.addTextChangedListener {
            isNovaSenhaValid = it.toString().isNotEmpty() && it.toString().matches(regexNovaSenha) &&
                    it.toString() != credentialData?.password
            binding.edtNovaSenha.error = if (!isNovaSenhaValid) getString(R.string.error_senha_invalida) else null
            ativarBtn()
        }

        if (userSection.type == "DRIVER") {
            binding.edtEditarPlaca.addTextChangedListener {
                isPlacaValid = it.toString().isNotEmpty() && it.toString().matches(regexPlaca)
                binding.edtEditarPlaca.error = if (!isPlacaValid) getString(R.string.error_placa_invalida) else null
                ativarBtn()
            }

            binding.edtEditarVeiculo.addTextChangedListener {
                isCarroValid = it.toString().isNotEmpty() && it.toString().matches(regexNovoModeloCarro)
                binding.edtEditarVeiculo.error = if (!isCarroValid) getString(R.string.error_nome_carro_invalido) else null
                ativarBtn()
            }
        } else {
            binding.llEditarPlaca.visibility = View.GONE
            binding.llEditarVeiculo.visibility = View.GONE
        }
    }

    private fun configurarBotoes() {
        binding.btnVoltar.setOnClickListener { finish() }
        binding.btnSalvar.setOnClickListener { salvarDados() }
    }

    private fun ativarBtn() {
        val validarCampo = if (userSection.type == "DRIVER") {
            isNomeValid || isEmailValid || isPhoneValid || isSenhaAtualValid || isNovaSenhaValid || isPlacaValid || isCarroValid
        } else {
            isNomeValid || isEmailValid || isPhoneValid || isSenhaAtualValid || isNovaSenhaValid
        }

        binding.btnSalvar.isEnabled = validarCampo
        binding.btnSalvar.backgroundTintList =
            ContextCompat.getColorStateList(this,
                if (validarCampo) R.color.amarelo_principal else R.color.btn_desativado)
    }

    private fun setHint() {
        binding.edtEditarNome.hint = userSection.name
        binding.edtEditarEmail.hint = credentialData?.email
        binding.edtEditarPhone.hint = userSection.phone
        if (userSection.type == "DRIVER") {
            binding.edtEditarPlaca.hint = userSection.plateNumber
            binding.edtEditarVeiculo.hint = userSection.carModel
        }
    }

    private fun salvarDados() {
        val email = binding.edtEditarEmail.text.toString().trim()
        val senha = binding.edtNovaSenha.text.toString().trim()
        val nome = binding.edtEditarNome.text.toString().trim()
        val phone = binding.edtEditarPhone.text.toString().trim()

        if (userSection.type == "DRIVER") {
            val placa = binding.edtEditarPlaca.text.toString().trim()
            val nomeVeiculo = binding.edtEditarVeiculo.text.toString().trim()

            // Atualiza credenciais
            if ((email.isNotEmpty() && email.matches(regexEmail)) ||
                (senha.isNotEmpty() && senha.matches(regexNovaSenha) && senha != credentialData?.password)) {

                val updateLogin = Login().apply {
                    if (email.isNotEmpty() && email.matches(regexEmail)) this.email = email
                    if (senha.isNotEmpty() && senha.matches(regexNovaSenha) && senha != credentialData?.password) this.password = senha
                }

                lifecycleScope.launch {
                    val requisicao = RetrofitClient.loginRetrofit.updateById(updateLogin, userSection.id)
                    if (requisicao.isSuccessful) {
                        Log.d("Credential", "Credenciais atualizadas com sucesso")
                        reload()
                    }
                }
            }

            // Atualiza Driver
            val updateDriver = Driver().apply {
                if (nome.isNotEmpty() && nome.matches(regexNome)) this.name = nome
                if (phone.isNotEmpty() && phone.matches(regexPhone)) this.phone = phone
                if (placa.isNotEmpty() && placa.matches(regexPlaca)) this.plateNumber = placa
                if (nomeVeiculo.isNotEmpty() && nomeVeiculo.matches(regexNovoModeloCarro)) this.carModel = nomeVeiculo
            }

            lifecycleScope.launch {
                if (updateDriver.name != null || updateDriver.phone != null ||
                    updateDriver.plateNumber != null || updateDriver.carModel != null) {

                    val requisicao = RetrofitClient.driverRetrofit.updateById(updateDriver, userSection.id)
                    if (requisicao.isSuccessful) {
                        Log.d("Driver", "Usuário atualizado com sucesso")
                        reload()
                    }
                }
            }

        } else { // CUSTOMER
            val updateCustomer = Customer().apply {
                if (nome.isNotEmpty() && nome.matches(regexNome)) this.name = nome
                if (phone.isNotEmpty() && phone.matches(regexPhone)) this.phone = phone
            }

            lifecycleScope.launch {
                if (updateCustomer.name != null || updateCustomer.phone != null) {
                    val requisicao = RetrofitClient.customerRetrofit.updateById(updateCustomer, userSection.id)
                    if (requisicao.isSuccessful) {
                        Log.d("Customer", "Usuário atualizado com sucesso")
                        reload()
                    }
                }
            }

            // Atualiza credenciais
            if ((email.isNotEmpty() && email.matches(regexEmail)) ||
                (senha.isNotEmpty() && senha.matches(regexNovaSenha) && senha != credentialData?.password)) {

                val updateLogin = Login().apply {
                    if (email.isNotEmpty() && email.matches(regexEmail)) this.email = email
                    if (senha.isNotEmpty() && senha.matches(regexNovaSenha) && senha != credentialData?.password) this.password = senha
                }

                lifecycleScope.launch {
                    val requisicao = RetrofitClient.loginRetrofit.updateById(updateLogin, userSection.id)
                    if (requisicao.isSuccessful) {
                        Log.d("Credential", "Credenciais atualizadas com sucesso")
                        reload()
                    }
                }
            }
        }
    }

    private fun reload(target: Class<*> = SplashScreenActivity::class.java) {
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                val intent = Intent(this@ConfigActivity, target)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }
}
