package com.robertojr.util

import android.content.Context
import android.content.Intent
import com.robertojr.ui.LoginActivity
import java.io.File

fun logoutUser(context: Context) {


    // Apagar JSON de login
    val file = File(context.filesDir, "login.json")
    if (file.exists()) {
        file.delete()
    }

    // Voltar para tela de login
    val intent = Intent(context, LoginActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    context.startActivity(intent)

    // Se o contexto for Activity, podemos finalizar
    if (context is android.app.Activity) {
        context.finish()
    }
}
