package com.robertojr.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.here.sdk.core.engine.AuthenticationMode
import com.here.sdk.core.engine.SDKNativeEngine
import com.here.sdk.core.engine.SDKOptions
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
        // INICIALIZA SDK PRIMEIRO
        val accessKeyID = "uAY9T3367Dii69cztCGyqQ"
        val accessKeySecret = "smC9nQV6T5XLXtkccC-BwFaQQwTDwGoW2951D3NIVJlQVT2wWiViFX2aq5A7Csd4YGsdlT63n1Yer9zlhwe3dg"
        val authenticationMode = AuthenticationMode.withKeySecret(accessKeyID, accessKeySecret)
        val options = SDKOptions(authenticationMode)
        SDKNativeEngine.makeSharedInstance(requireContext(), options)

        // LAYOUT SIMPLES
        val view = inflater.inflate(R.layout.fragment_maps, container, false)
        val containerLayout = view.findViewById<FrameLayout>(R.id.map_container)

        // CRIA MAPVIEW
        mapView = MapView(requireContext())
        containerLayout.addView(mapView)

        // CONFIGURA MAPA
        mapView.onCreate(savedInstanceState)
        mapView.mapScene.loadScene(MapScheme.NORMAL_DAY) { }

        return view
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