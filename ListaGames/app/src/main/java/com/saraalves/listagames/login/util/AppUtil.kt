package com.saraalves.listagames.login.util

import android.content.Context
import android.content.SharedPreferences

class AppUtil {

    fun salvarIdUsuario(context: Context, uiid: String?) {
        val preferences: SharedPreferences =
            context.getSharedPreferences("APP", Context.MODE_PRIVATE)
        preferences.edit().putString("UIID", uiid).apply()
    }
}