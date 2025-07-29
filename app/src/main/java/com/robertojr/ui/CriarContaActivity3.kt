package com.robertojr.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.robertojr.moov.R
import com.robertojr.moov.databinding.ActivityCriarConta3Binding

class CriarContaActivity3 : AppCompatActivity() {
    lateinit var binding: ActivityCriarConta3Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityCriarConta3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val opcoes = listOf<String>("Selecione uma opção","Motorista", "Cliente")
        binding.spEscolha.adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            opcoes
        )

        binding.spEscolha.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val itemSelecionado = opcoes[position]
                when (itemSelecionado) {

                    "Motorista" -> {
                        binding.btnCriarConta.isEnabled = false
                        binding.llCliente.visibility = View.GONE
                        binding.llMotorista.visibility = View.VISIBLE

                        val watcher = {
                            val veiculo = binding.edtModeloCarro.text.toString()
                            val placa = binding.edtPlacaVeiculo.text.toString()

                            var regexVeiculo = veiculo.isNotEmpty()
                            var regexPlaca = placa.isNotEmpty()

                            var validate = regexPlaca and regexVeiculo

                            binding.btnCriarConta.isEnabled = validate
                            binding.imgConfirmMotorista.visibility = if(validate) View.VISIBLE else View.INVISIBLE
                            binding.btnCriarConta.backgroundTintList =
                                ContextCompat.getColorStateList(
                                    this@CriarContaActivity3,
                                    if (validate) R.color.amarelo_principal else R.color.btn_desativado
                                )

                            if (!regexVeiculo){
                                binding.edtModeloCarro.error = getString(R.string.campoIncompleto)
                            }
                            if(!regexPlaca){
                                binding.edtPlacaVeiculo.error = getString(R.string.campoIncompleto)
                            }

                        }

                        binding.edtPlacaVeiculo.addTextChangedListener { watcher() }
                        binding.edtModeloCarro.addTextChangedListener { watcher() }

                    }

                    "Cliente" -> {
                        binding.llMotorista.visibility = View.GONE
                        binding.llCliente.visibility = View.VISIBLE
                        binding.btnCriarConta.isEnabled = true

                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }
}