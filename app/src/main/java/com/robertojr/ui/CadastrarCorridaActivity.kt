package com.robertojr.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.here.sdk.core.GeoCoordinates
import com.here.sdk.mapview.MapImageFactory
import com.here.sdk.mapview.MapMarker
import com.here.sdk.mapview.MapScheme
import com.here.sdk.mapview.MapView
import com.robertojr.moov.R
import com.robertojr.moov.databinding.ActivityCadastrarCorridaBinding
import com.robertojr.moov.model.Lugar
import com.robertojr.moov.model.RetrofitClient
import com.robertojr.moov.model.helper.ProcurarLugar
import com.robertojr.moov.model.sending.Driver
import com.robertojr.moov.model.sending.RacerSending
import com.robertojr.util.userSection
import kotlinx.coroutines.launch

class CadastrarCorridaActivity : AppCompatActivity() {

    lateinit var binding: ActivityCadastrarCorridaBinding
    lateinit var mapView: MapView
    lateinit var origem: Lugar
    lateinit var destino: Lugar
    private val mapMarkers = mutableListOf<MapMarker>()
    private var origemValida = false
    private var destinoValida = false

    private fun loadMap() {
        mapView.mapScene.loadScene(MapScheme.NORMAL_DAY) { errorCode ->
            if (errorCode != null) {
                Log.e("CadastrarCorridaActivity", "Erro ao carregar mapa: $errorCode")
            }
        }
    }

    private fun adicionarMarcador(nome: String, tipo: String) {
        val procurarLugar = ProcurarLugar()
        procurarLugar.buscarCordenadasPorNome(nome) { error, lugares ->
            if (error != null) {
                Log.d("MOOV LOG", "Error na busca $error")
                return@buscarCordenadasPorNome
            }
            if (!lugares.isNullOrEmpty()) {
                val posicao = lugares[0]

                val mapImage = MapImageFactory.fromResource(
                    this@CadastrarCorridaActivity.resources,
                    when (tipo) {
                        "origem" -> R.drawable.map_marker_origem
                        "destino" -> R.drawable.map_marker_destino
                        else -> R.drawable.marcador_map
                    }
                )

                val marcador = MapMarker(GeoCoordinates(posicao.latitude, posicao.longitude), mapImage)

                // Remove marcador antigo do mesmo tipo
                mapMarkers.find { it.metadata?.getString("tipo") == tipo }?.let {
                    mapView.mapScene.removeMapMarker(it)
                    mapMarkers.remove(it)
                }

                marcador.metadata = com.here.sdk.core.Metadata().apply { setString("tipo", tipo) }
                mapView.mapScene.addMapMarker(marcador)
                mapMarkers.add(marcador)

                when (tipo) {
                    "origem" -> {
                        origem = posicao
                        origemValida = true
                        mapView.camera.lookAt(GeoCoordinates(posicao.latitude, posicao.longitude))
                    }
                    "destino" -> {
                        destino = posicao
                        destinoValida = true
                        if (!origemValida) { // Só centraliza no destino se origem não estiver preenchida
                            mapView.camera.lookAt(GeoCoordinates(posicao.latitude, posicao.longitude))
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Nenhum lugar encontrado com o nome $nome", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validarMarcadores() {
        val origemText = binding.etOrigin.text.toString()
        val destinoText = binding.etDestination.text.toString()

        if (origemText.isEmpty() && destinoText.isEmpty()) {
            Toast.makeText(this, "Preencha pelo menos origem ou destino!", Toast.LENGTH_SHORT).show()
            return
        }

        if (origemText.isNotEmpty()) adicionarMarcador(origemText, "origem")
        if (destinoText.isNotEmpty()) adicionarMarcador(destinoText, "destino")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastrarCorridaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapView = binding.mapViewCadastroViagem
        mapView.onCreate(savedInstanceState)
        loadMap()

        binding.btnValidar.setOnClickListener {
            validarMarcadores()
        }

        binding.btnConfirmTrip.setOnClickListener {
            if (!origemValida || !destinoValida) {
                Toast.makeText(this, "Valide origem e destino antes de criar a corrida!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val corrida = RacerSending(
                binding.edtDescricao.text.toString(),
                destino.nome,
                Driver(userSection.id),
                destino.latitude.toString(),
                origem.latitude.toString(),
                destino.longitude.toString(),
                origem.longitude.toString(),
                origem.nome,
                binding.etPrice.text.toString().toDoubleOrNull() ?: 0.0,
                0,
                binding.edtDataCorrida.text.toString(),
                binding.etVacancies.text.toString()
            )

            lifecycleScope.launch {
                val solicitacao = RetrofitClient.racerRetrofit.insert(corrida)
                if (solicitacao.isSuccessful) {
                    Toast.makeText(this@CadastrarCorridaActivity, "Corrida cadastrada com sucesso!", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("RacerObjeto", corrida.toString())
                    Log.d("RacerObjeto", solicitacao.errorBody()?.string() ?: "")
                    Toast.makeText(this@CadastrarCorridaActivity, "Falha ao criar corrida!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onPause() { super.onPause(); mapView.onPause() }
    override fun onResume() { super.onResume(); mapView.onResume() }
    override fun onDestroy() { super.onDestroy(); mapView.onDestroy() }
}
