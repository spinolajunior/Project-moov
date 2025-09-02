package com.robertojr.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.here.sdk.core.GeoCoordinates
import com.here.sdk.core.Metadata
import com.here.sdk.core.Point2D
import com.here.sdk.core.Rectangle2D
import com.here.sdk.core.Size2D
import com.here.sdk.gestures.TapListener
import com.here.sdk.mapview.MapImageFactory
import com.here.sdk.mapview.MapMarker
import com.here.sdk.mapview.MapScene
import com.here.sdk.mapview.MapScheme
import com.here.sdk.mapview.MapView
import com.robertojr.moov.R
import com.robertojr.moov.databinding.FragmentMapsBinding
import com.robertojr.moov.model.Racer
import com.robertojr.moov.model.RetrofitClient
import com.robertojr.ui.CadastrarCorridaActivity
import com.robertojr.ui.CadastrarReservaActivity
import com.robertojr.ui.CorridasActivity
import com.robertojr.util.userSection
import kotlinx.coroutines.launch

class MapsFragment : Fragment() {

    private lateinit var binding: FragmentMapsBinding
    private lateinit var mapView: MapView
    private val listaMarkers: MutableList<MapMarker> = mutableListOf()

    private val handler = Handler(Looper.getMainLooper())
    private val refreshRunnable = object : Runnable {
        override fun run() {
            refreshData()
            handler.postDelayed(this, 10000) // Atualiza a cada 10 segundos
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)

        // Oculta botão de cadastro para clientes
        if (userSection.type == "CUSTOMER") {
            binding.btnCadastrarCorrida.visibility = View.GONE
        }

        loadMap()
        setTapGesture() // Chamado apenas uma vez
        binding.btnSync.setOnClickListener { refreshData() }
        binding.btnCadastrarCorrida.setOnClickListener {
            startActivity(Intent(requireContext(), CadastrarCorridaActivity::class.java))
        }

        refreshData() // Carrega dados iniciais

        return binding.root
    }

    private fun loadMap() {
        mapView.mapScene.loadScene(MapScheme.NORMAL_DAY) { errorCode ->
            if (errorCode == null) {
                val saoPaulo = GeoCoordinates(-23.5505, -46.6333)
                mapView.camera.lookAt(saoPaulo)
            } else {
                android.util.Log.e("MapsFragment", "Erro ao carregar mapa: $errorCode")
            }
        }
    }

    private fun setTapGesture() {
        mapView.gestures.tapListener = TapListener { touchPoint ->
            pickMarker(touchPoint)
        }
    }

    private fun pickMarker(touchPoint: Point2D) {
        val rectangle = Rectangle2D(touchPoint, Size2D(1.0, 1.0))
        val filter = MapScene.MapPickFilter(listOf(MapScene.MapPickFilter.ContentType.MAP_ITEMS))

        mapView.pick(filter, rectangle) { mapPickResult ->
            val markers = mapPickResult?.mapItems?.markers
            if (!markers.isNullOrEmpty()) {
                val topMarker = markers[0]
                val id = topMarker.metadata?.getString("id")?.toLongOrNull()

                if (id != null) {
                    when (userSection.type) {
                        "DRIVER" -> startActivity(Intent(requireContext(), CorridasActivity::class.java).apply {
                            putExtra("id", id)
                        })
                        "CUSTOMER" -> startActivity(Intent(requireContext(), CadastrarReservaActivity::class.java).apply {
                            putExtra("id", id)
                        })
                    }
                }
            }
        }
    }

    private fun refreshData() {
        // Remove marcadores antigos
        if (listaMarkers.isNotEmpty()) {
            mapView.mapScene.removeMapMarkers(listaMarkers)
            listaMarkers.clear()
        }

        lifecycleScope.launch {
            val response = RetrofitClient.racerRetrofit.findAll()
            if (response.isSuccessful && response.body() != null) {
                var corridasAtivas = response.body()!!
                    .filter { it.racerStatus == "ACTIVE" } // Apenas ACTIVE

                // Filtra por tipo de usuário
                corridasAtivas = when (userSection.type) {
                    "DRIVER" -> corridasAtivas.filter { it.driverId == userSection.id }.toMutableList()
                    "CUSTOMER" -> corridasAtivas.toMutableList() // todos ACTIVE
                    else -> mutableListOf()
                }

                // Cria marcadores
                val novosMarkers = geoFactoryList(corridasAtivas)

                // Adiciona ao mapa
                mapView.mapScene.addMapMarkers(novosMarkers)

                // Atualiza lista para próxima remoção
                listaMarkers.addAll(novosMarkers)

                // Centraliza câmera no primeiro marcador
                if (listaMarkers.isNotEmpty()) {
                    mapView.camera.lookAt(listaMarkers[0].coordinates)
                }
            } else {
                Toast.makeText(requireContext(), "Erro ao carregar corridas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun geoFactoryList(lista: List<Racer>): MutableList<MapMarker> {
        val mapImage = MapImageFactory.fromResource(resources, R.drawable.marcador_map)
        val markers = mutableListOf<MapMarker>()

        for (racer in lista) {
            val marker = MapMarker(GeoCoordinates(racer.latitudeO.toDouble(), racer.longitudeO.toDouble()), mapImage)
            val metadata = Metadata()
            metadata.setString("id", racer.id.toString())
            marker.metadata = metadata
            markers.add(marker)
        }

        return markers
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        handler.post(refreshRunnable)
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
        handler.removeCallbacks(refreshRunnable)
    }

    override fun onDestroyView() {
        mapView.onDestroy()
        super.onDestroyView()
    }
}
