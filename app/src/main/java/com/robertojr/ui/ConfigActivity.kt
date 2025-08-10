package com.robertojr.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.robertojr.moov.R
import com.robertojr.moov.databinding.ActivityOptionsBinding
import com.robertojr.util.credentialData
import com.robertojr.util.userSection

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
    private val regexNovaSenha = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^\\da-zA-Z\\s])(?=.{8,}).*\$")
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
            if (!isNomeValid && it.toString().isNotEmpty()) {
                binding.edtEditarNome.error = getString(R.string.error_nome_invalido)
            } else {
                binding.edtEditarNome.error = null
            }
            ativarBtn()
        }

        binding.edtEditarEmail.addTextChangedListener {
            isEmailValid = it.toString().isNotEmpty() && it.toString().matches(regexEmail)
            if (!isEmailValid && it.toString().isNotEmpty()) {
                binding.edtEditarEmail.error = getString(R.string.error_email_invalido)
            } else {
                binding.edtEditarEmail.error = null
            }
            ativarBtn()
        }

        binding.edtEditarPhone.addTextChangedListener {
            isPhoneValid = it.toString().isNotEmpty() && it.toString().matches(regexPhone)
            if (!isPhoneValid && it.toString().isNotEmpty()) {
                binding.edtEditarPhone.error = getString(R.string.error_phone_invalido)
            } else {
                binding.edtEditarPhone.error = null
            }
            ativarBtn()
        }

        binding.edtSenhaAtual.addTextChangedListener {
            isSenhaAtualValid = it.toString().isNotEmpty() && it.toString() == credentialData?.password
            if (!isSenhaAtualValid && it.toString().isNotEmpty()) {
                binding.edtSenhaAtual.error = getString(R.string.error_senha_atual)
            } else {
                binding.edtSenhaAtual.error = null
            }
            ativarBtn()
        }

        binding.edtNovaSenha.addTextChangedListener {
            isNovaSenhaValid = it.toString().isNotEmpty() && it.toString().matches(regexNovaSenha) && it.toString() != credentialData?.password
            if (!isNovaSenhaValid && it.toString().isNotEmpty()) {
                binding.edtNovaSenha.error = getString(R.string.error_senha_invalida)
            } else {
                binding.edtNovaSenha.error = null
            }
            ativarBtn()
        }

        // Validação específica para o tipo DRIVER
        if (userSection.type == "DRIVER") {
            binding.edtEditarPlaca.addTextChangedListener {
                isPlacaValid = it.toString().isNotEmpty() && it.toString().matches(regexPlaca)
                if (!isPlacaValid && it.toString().isNotEmpty()) {
                    binding.edtEditarPlaca.error = getString(R.string.error_placa_invalida)
                } else {
                    binding.edtEditarPlaca.error = null
                }
                ativarBtn()
            }

            binding.edtEditarVeiculo.addTextChangedListener {
                isCarroValid = it.toString().isNotEmpty() && it.toString().matches(regexNovoModeloCarro)
                if (!isCarroValid && it.toString().isNotEmpty()) {
                    binding.edtEditarVeiculo.error = getString(R.string.error_nome_carro_invalido)
                } else {
                    binding.edtEditarVeiculo.error = null
                }
                ativarBtn()
            }
        } else {
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
        // Ativa o botão se pelo menos um campo tiver sido preenchido e for válido
        val isAnyFieldValid = when (userSection.type) {
            "DRIVER" -> isNomeValid || isEmailValid || isPhoneValid || isSenhaAtualValid || isNovaSenhaValid || isPlacaValid || isCarroValid
            else -> isNomeValid || isEmailValid || isPhoneValid || isSenhaAtualValid || isNovaSenhaValid
        }

        binding.btnSalvar.isEnabled = isAnyFieldValid
        binding.btnSalvar.backgroundTintList =
            ContextCompat.getColorStateList(
                this,
                if (isAnyFieldValid) R.color.amarelo_principal else R.color.btn_desativado
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
        // Lógica de salvamento aqui...
        // Use as variáveis de estado (ex: isNomeValid) para saber quais campos atualizar.
        // Sua lógica de salvamento está no `btnSalvar.setOnClickListener` e parece OK,
        // apenas a movi para uma função separada para maior organização.
    }