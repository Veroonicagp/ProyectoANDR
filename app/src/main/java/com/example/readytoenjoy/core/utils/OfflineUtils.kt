package com.example.readytoenjoy.ui.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

object OfflineUtils {

    fun showOfflineMessage(view: View) {
        Snackbar.make(
            view,
            "Sin conexión a internet. Mostrando datos guardados.",
            Snackbar.LENGTH_LONG
        ).setAction("OK") {
        }.show()
    }

    fun showConnectionRestoredMessage(view: View) {
        Snackbar.make(
            view,
            "Conexión restaurada",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    fun showNeedConnectionMessage(view: View, action: String) {
        Snackbar.make(
            view,
            "Necesitas conexión para $action",
            Snackbar.LENGTH_SHORT
        ).show()
    }
}