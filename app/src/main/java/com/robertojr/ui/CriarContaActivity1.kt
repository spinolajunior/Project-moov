package com.robertojr.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.robertojr.moov.R
import com.robertojr.moov.databinding.ActivityCriarConta1Binding

class CriarContaActivity1 : AppCompatActivity() {
    lateinit var binding: ActivityCriarConta1Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCriarConta1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()



        val watcher = {
            var nome = binding.edtNomeCadastro.text.toString()
            var sobrenome = binding.edtSobrenome.text.toString()
            var phone = binding.edtPhone.text.toString()
            var dataNascimento = binding.edtDataNascimento.text.toString()

            val regexNome = nome.isNotEmpty() and nome.matches(Regex("^[A-Za-zÀ-ÖØ-öø-ÿ]{2,}(\\s[A-Za-zÀ-ÖØ-öø-ÿ]{2,})*\$"))
            val regexSobrenome = sobrenome.isNotEmpty() and sobrenome.matches(Regex("^[A-Za-zÀ-ÖØ-öø-ÿ]{2,}(\\s[A-Za-zÀ-ÖØ-öø-ÿ]{2,})*\$"))
            val regexPhone = phone.isNotEmpty() and phone.matches(Regex("^\\(?\\d{2}\\)?[\\s-]?9\\d{4}-?\\d{4}\$"))
            val regexDataNascimento = dataNascimento.isNotEmpty() and dataNascimento.matches(Regex("^\\d{2}/\\d{2}/\\d{4}\$"))

            val validate = regexNome and regexSobrenome and regexPhone and regexDataNascimento
            binding.btnCriarContaAvancar.isEnabled = validate
            binding.btnCriarContaAvancar.backgroundTintList =
                ContextCompat.getColorStateList(
                    this@CriarContaActivity1,
                    if (validate) R.color.amarelo_principal else R.color.btn_desativado
                )

            if (!regexNome) binding.edtNomeCadastro.error = "Nome inválido. Use apenas letras e pelo menos 2 caracteres."
            if (!regexSobrenome) binding.edtSobrenome.error = "Sobrenome inválido. Use apenas letras e pelo menos 2 caracteres."
            if (!regexPhone) binding.edtPhone.error = "Telefone inválido. Use o formato (11) 99999-8888 ou similar."
            if (!regexDataNascimento) binding.edtDataNascimento.error = "Data inválida. Use o formato dd/MM/aaaa com uma data real."

        }

        binding.edtNomeCadastro.addTextChangedListener { watcher() }
        binding.edtSobrenome.addTextChangedListener { watcher() }
        binding.edtPhone.addTextChangedListener { watcher() }
        binding.edtDataNascimento.addTextChangedListener { watcher() }


        binding.btnCriarContaAvancar.setOnClickListener {
            val intent = Intent(this@CriarContaActivity1, CriarContaActivity2::class.java)


            var nome = binding.edtNomeCadastro.text.toString()
            var sobrenome = binding.edtSobrenome.text.toString()
            var phone = binding.edtPhone.text.toString()
            var dataNascimento = binding.edtDataNascimento.text.toString()


            intent.putExtra("nome", nome)
            intent.putExtra("sobrenome", sobrenome)
            intent.putExtra("phone", phone)
            intent.putExtra("dataNascimento", dataNascimento)

            startActivity(intent)


        }
        binding.btnVoltarLogin.setOnClickListener {
            val intent = Intent(this@CriarContaActivity1, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }


    }

}
