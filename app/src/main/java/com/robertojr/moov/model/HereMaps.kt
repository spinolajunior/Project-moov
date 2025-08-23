package com.robertojr.moov.model

import android.app.Application
import com.here.sdk.core.engine.AuthenticationMode
import com.here.sdk.core.engine.SDKNativeEngine
import com.here.sdk.core.engine.SDKOptions
import com.robertojr.moov.R

class HereMaps: Application() {

    override fun onCreate() {
        super.onCreate()
        initializeHereSDK()
    }

     private fun initializeHereSDK() {
        try {

            val accessKeyId = getString(R.string.acess_key_id)
            val accessKeySecret = getString(R.string.acess_key_secret)
            val autenticatorMode = AuthenticationMode.withKeySecret(accessKeyId,accessKeySecret)
            val sdkOptions = SDKOptions(autenticatorMode)

            SDKNativeEngine.makeSharedInstance(this, sdkOptions)
        } catch (e: Exception) {
            // Log de erro mais detalhado
            android.util.Log.e("MyApp", "Erro ao inicializar HERE SDK: ${e.message}")
        }
    }


}


