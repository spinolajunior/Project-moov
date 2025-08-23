package com.robertojr.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.here.sdk.core.GeoCoordinates
import com.here.sdk.mapview.MapScheme
import com.here.sdk.mapview.MapView
import com.robertojr.moov.R
import com.robertojr.moov.databinding.ActivityCadastrarCorridaBinding

class CadastrarCorridaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCadastrarCorridaBinding
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityCadastrarCorridaBinding.inflate(layoutInflater)
        setContentView(binding.root)


        mapView = binding.root.findViewById(R.id.map_view_cadastro_viagem)


        mapView.onCreate(savedInstanceState)
        loadMap()
    }

    private fun loadMap() {
        mapView.mapScene.loadScene(MapScheme.NORMAL_DAY) { errorCode ->
            if (errorCode == null) {
                val saoPaulo = GeoCoordinates(-23.5505, -46.6333)
                mapView.camera.lookAt(saoPaulo)

            } else {
                android.util.Log.e("CadastrarCorridaActivity", "Erro ao carregar mapa: $errorCode")
            }
        }
    }

    // Não se esqueça de implementar os métodos do ciclo de vida
    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }
}