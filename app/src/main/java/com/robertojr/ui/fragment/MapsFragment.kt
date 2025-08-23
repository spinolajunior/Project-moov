package com.robertojr.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.here.sdk.core.GeoCoordinates
import com.here.sdk.mapview.MapScheme
import com.here.sdk.mapview.MapView
import com.robertojr.moov.R

class MapsFragment : Fragment() {

    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_maps, container, false)


        mapView = view.findViewById(R.id.map_view)


        mapView.onCreate(savedInstanceState)


        loadMap()

        return view
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


    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroyView() {
        mapView.onDestroy()
        super.onDestroyView()
    }
}
