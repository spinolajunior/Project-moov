package com.robertojr.moov.model.helper

import android.util.Log
import com.here.sdk.core.GeoBox
import com.here.sdk.core.GeoCoordinates
import com.here.sdk.core.LanguageCode
import com.here.sdk.search.SearchCallback
import com.here.sdk.search.SearchEngine
import com.here.sdk.search.SearchError
import com.here.sdk.search.SearchOptions
import com.here.sdk.search.TextQuery
import com.robertojr.moov.model.Lugar

class ProcurarLugar() {

    private lateinit var searchEngine: SearchEngine

    fun buscarCordenadasPorNome(nome: String , resultado : (error: SearchError?, lugares : MutableList<Lugar>?) -> Unit) {

        try {
            searchEngine = SearchEngine()
        } catch (e: InstantiationException) {

            throw RuntimeException("Falha ao inicializar o SearchEngine: " + e.message)
        }


        val brasilGeoBox = GeoBox(
            GeoCoordinates(-33.7, -73.9),  // Sudoeste
            GeoCoordinates(5.2, -34.8)     // Nordeste
        )
        val area = TextQuery.Area(brasilGeoBox)
        val query = TextQuery(nome, area)
        val options = SearchOptions()

        options.languageCode = LanguageCode.PT_BR

        val searchCallback = SearchCallback { searchError, list ->
            if (searchError != null) {
                Log.d("Search", "$searchError")
                resultado(searchError,null)
                return@SearchCallback
            }
            Log.d("Result", "${list!!.size}")


            var lista = mutableListOf<Lugar>()
            for (item in list) {
                var lugar = Lugar(
                    item.title,
                    item.geoCoordinates!!.latitude,
                    item.geoCoordinates!!.longitude
                )
                lista.add(lugar)
            }
            resultado(null,lista)

        }
        searchEngine.searchByText(query, options, searchCallback)

    }
}


