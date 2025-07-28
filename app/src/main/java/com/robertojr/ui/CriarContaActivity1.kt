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

            val regexNome = nome.isNotEmpty()
            val regexSobrenome = sobrenome.isNotEmpty()
            val regexPhone = phone.isNotEmpty()
            val regexDataNascimento = dataNascimento.isNotEmpty()

            val validate = regexNome and regexSobrenome and regexPhone and regexDataNascimento
            binding.btnCriarContaAvancar.isEnabled = validate
            binding.btnCriarContaAvancar.backgroundTintList =
                ContextCompat.getColorStateList(
                    this@CriarContaActivity1,
                    if (validate) R.color.amarelo_principal else R.color.btn_desativado
                )

            if (!regexNome) binding.edtNomeCadastro.error = getString(R.string.campoIncompleto)
            if (!regexSobrenome) binding.edtSobrenome.error = getString(R.string.campoIncompleto)
            if (!regexPhone) binding.edtPhone.error = getString(R.string.campoIncompleto)
            if (!regexDataNascimento) binding.edtDataNascimento.error = getString(R.string.campoIncompleto)

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


    }

}
