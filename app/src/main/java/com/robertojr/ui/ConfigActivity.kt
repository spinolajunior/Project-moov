package com.robertojr.ui

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
import kotlinx.coroutines.launch

class ConfigActivity : AppCompatActivity() {
    lateinit var binding: ActivityOptionsBinding

    // Variáveis para rastrear o estado dos campos, permitindo que o botão Salvar seja ativado
    private var isNomeValid = false
    private var isEmailValid = false
    private var isPhoneValid = false
    private var isSenhaAtualValid = false
    private var isNovaSenhaValid = false
    private var isPlacaValid = false
    private var isCarroValid = false

    // Regexes em constantes para reutilização
    private val regexNome = Regex("^[A-Za-zÀ-ÖØ-öø-ÿ]{2,}(\\s[A-Za-zÀ-ÖØ-öø-ÿ]{2,})*\$")
    private val regexEmail = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
    private val regexPhone = Regex("^\\(?\\d{2}\\)?[\\s-]?9\\d{4}-?\\d{4}\$")
    private val regexNovaSenha =
        Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^\\da-zA-Z\\s])(?=.{8,}).*\$")
    private val regexPlaca = Regex("^([A-Z]{3}-?\\d{4}|[A-Z]{3}\\d[A-Z]\\d{2})$")
    private val regexNovoModeloCarro = Regex("^(?=.*\\d.*\\d)(?!.*--)(?!.*-$)[a-zA-Z0-9\\s-]*$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setHint()

        // Configuração dos TextWatchers de forma individual
        binding.edtEditarNome.addTextChangedListener {
            isNomeValid = it.toString().isNotEmpty() && it.toString().matches(regexNome)
            if (!isNomeValid) {
                binding.edtEditarNome.error = getString(R.string.error_nome_invalido)
            } else {
                binding.edtEditarNome.error = null
            }
            ativarBtn()
        }

        binding.edtEditarEmail.addTextChangedListener {
            isEmailValid = it.toString().isNotEmpty() && it.toString().matches(regexEmail)
            if (!isEmailValid) {
                binding.edtEditarEmail.error = getString(R.string.error_email_invalido)
            } else {
                binding.edtEditarEmail.error = null
            }
            ativarBtn()
        }

        binding.edtEditarPhone.addTextChangedListener {
            isPhoneValid = it.toString().isNotEmpty() && it.toString().matches(regexPhone)
            if (!isPhoneValid) {
                binding.edtEditarPhone.error = getString(R.string.error_phone_invalido)
            } else {
                binding.edtEditarPhone.error = null
            }
            ativarBtn()
        }

        binding.edtSenhaAtual.addTextChangedListener {
            isSenhaAtualValid =
                it.toString().isNotEmpty() && it.toString() == credentialData?.password
            if (!isSenhaAtualValid) {
                binding.edtSenhaAtual.error = getString(R.string.error_senha_atual)
            } else {
                binding.edtSenhaAtual.error = null
            }
            ativarBtn()
        }

        binding.edtNovaSenha.addTextChangedListener {
            isNovaSenhaValid = it.toString().isNotEmpty() && it.toString()
                .matches(regexNovaSenha) && it.toString() != credentialData?.password
            if (!isNovaSenhaValid) {
                binding.edtNovaSenha.error = getString(R.string.error_senha_invalida)
            } else {
                binding.edtNovaSenha.error = null
            }
            ativarBtn()
        }


        if (userSection.type == "DRIVER") {
            binding.edtEditarPlaca.addTextChangedListener {
                isPlacaValid = it.toString().isNotEmpty() && it.toString().matches(regexPlaca)
                if (!isPlacaValid) {
                    binding.edtEditarPlaca.error = getString(R.string.error_placa_invalida)
                } else {
                    binding.edtEditarPlaca.error = null
                }
                ativarBtn()
            }

            binding.edtEditarVeiculo.addTextChangedListener {
                isCarroValid =
                    it.toString().isNotEmpty() && it.toString().matches(regexNovoModeloCarro)
                if (!isCarroValid) {
                    binding.edtEditarVeiculo.error = getString(R.string.error_nome_carro_invalido)
                } else {
                    binding.edtEditarVeiculo.error = null
                }
                ativarBtn()
            }
        } else if (userSection.type == "CUSTOMER") {
            binding.llEditarPlaca.visibility = View.GONE
            binding.llEditarVeiculo.visibility = View.GONE
        }

        binding.btnVoltar.setOnClickListener {
            finish()
        }

        binding.btnSalvar.setOnClickListener {
            salvarDados()
        }
    }

    private fun ativarBtn() {

        val validarCampo =
            when (userSection.type) {
                "DRIVER" -> isNomeValid || isEmailValid || isPhoneValid || isSenhaAtualValid || isNovaSenhaValid || isPlacaValid || isCarroValid
                else -> isNomeValid || isEmailValid || isPhoneValid || isSenhaAtualValid || isNovaSenhaValid
            }

        binding.btnSalvar.isEnabled = validarCampo
        binding.btnSalvar.backgroundTintList =
            ContextCompat.getColorStateList(
                this,
                if (validarCampo) R.color.amarelo_principal else R.color.btn_desativado
            )
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
        if (userSection.type == "DRIVER") {
            val email = binding.edtEditarEmail.text.toString()
            val senha = binding.edtNovaSenha.text.toString()
            val nome = binding.edtEditarNome.text.toString()
            val phone = binding.edtEditarPhone.text.toString()
            val placa = binding.edtEditarPlaca.text.toString()
            val nomeVeiculo = binding.edtEditarVeiculo.text.toString()
            val testCredentialUpdate = email.matches(regexEmail) ||
                    (senha.matches(regexNovaSenha) && senha != credentialData?.password)

            var updateLogin = Login()
            if (testCredentialUpdate) {
                updateLogin.email = email
                updateLogin.password = senha

                lifecycleScope.launch {
                    val requisicao =
                        RetrofitClient.loginRetrofit.updateById(updateLogin, userSection.id)
                    if (requisicao.isSuccessful) {
                        Log.d("Credential -> ", "Sucesso")
                    }
                }
            }

            lifecycleScope.launch {
                when (userSection.type) {
                    "DRIVER" -> {
                        var updateDriver = Driver()
                        updateDriver.name = nome
                        updateDriver.phone = phone
                        updateDriver.plateNumber = placa
                        updateDriver.carModel = nomeVeiculo

                        val requisicao =
                            RetrofitClient.driverRetrofit.updateById(updateDriver, userSection.id)

                        if (requisicao.isSuccessful) {
                            Log.d("Driver", "Sucesso")
                        }

                    }

                    else -> {
                        var updateCustomer = Customer()
                        updateCustomer.name = nome
                        updateCustomer.phone = phone

                        val requisicao = RetrofitClient.customerRetrofit.updateById(
                            updateCustomer,
                            userSection.id
                        )

                        if (requisicao.isSuccessful) {
                            (Log.d("Customer", "Sucesso"))

                        }
                    }
                }


            }
        }
    }
}