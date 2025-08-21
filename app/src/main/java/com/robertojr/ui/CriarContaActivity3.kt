package com.robertojr.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.robertojr.moov.R
import com.robertojr.moov.databinding.ActivityCriarConta3Binding
import com.robertojr.moov.model.Customer
import com.robertojr.moov.model.Driver
import com.robertojr.moov.model.Login
import com.robertojr.moov.model.RetrofitClient
import com.robertojr.moov.model.User
import kotlinx.coroutines.launch

class CriarContaActivity3 : AppCompatActivity() {
    lateinit var binding: ActivityCriarConta3Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityCriarConta3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        var newUsuario: User

        val nome = intent.getStringExtra("nome").toString()
        val sobrenome = intent.getStringExtra("sobrenome").toString()
        val phone = intent.getStringExtra("phone").toString()
        val dataNascimento = intent.getStringExtra("dataNascimento").toString()
        val usuario = intent.getStringExtra("usuario").toString()
        val senha = intent.getStringExtra("senha").toString()
        val email = intent.getStringExtra("email").toString()


        val opcoes = listOf<String>("Selecione uma opção", "Motorista", "Cliente")
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
                        binding.btnCriarConta.backgroundTintList =
                            ContextCompat.getColorStateList(
                                this@CriarContaActivity3, R.color.btn_desativado
                            )

                        val watcher = {
                            val veiculo = binding.edtModeloCarro.text.toString()
                            val placa = binding.edtPlacaVeiculo.text.toString()

                            val regexVeiculo = veiculo.isNotEmpty()
                            val regexPlaca = placa.isNotEmpty()

                            val validate = regexPlaca and regexVeiculo

                            binding.btnCriarConta.isEnabled = validate
                            binding.imgConfirmMotorista.visibility =
                                if (validate) View.VISIBLE else View.INVISIBLE
                            binding.btnCriarConta.backgroundTintList =
                                ContextCompat.getColorStateList(
                                    this@CriarContaActivity3,
                                    if (validate) R.color.amarelo_principal else R.color.btn_desativado
                                )

                            if (!regexVeiculo) {
                                binding.edtModeloCarro.error = getString(R.string.campoIncompleto)
                            }
                            if (!regexPlaca) {
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
                        binding.btnCriarConta.backgroundTintList =
                            ContextCompat.getColorStateList(
                                this@CriarContaActivity3, R.color.amarelo_principal
                            )

                    }


                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        binding.btnCriarConta.setOnClickListener {
            lifecycleScope.launch {


                if (binding.spEscolha.selectedItemPosition == 1) {
                    newUsuario = Driver(
                        null, nome, sobrenome, phone, null, null, null, null,
                        Login(email, null, senha, null, usuario),
                        binding.edtModeloCarro.text.toString(),
                        binding.edtPlacaVeiculo.text.toString(),
                        null, "0", null
                    )

                    val cadastrarDriver = RetrofitClient.driverRetrofit.insert(newUsuario as Driver)
                    if (cadastrarDriver.isSuccessful) {
                        val intent = Intent(this@CriarContaActivity3, LoginActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    } else {
                        // logic for failure
                    }

                } else {
                    newUsuario = Customer(
                        null, nome, sobrenome, null, null,
                        Login(email, null, senha, null, usuario),
                        phone, null, null, null
                    )

                    val cadastarCustomer =
                        RetrofitClient.customerRetrofit.insert(newUsuario as Customer)
                    if (cadastarCustomer.isSuccessful) {
                        val intent = Intent(this@CriarContaActivity3, LoginActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    } else {
                        // logic for failure
                    }
                }


            }
        }
        binding.btnVoltarLogin.setOnClickListener {
            val intent = Intent(this@CriarContaActivity3, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}