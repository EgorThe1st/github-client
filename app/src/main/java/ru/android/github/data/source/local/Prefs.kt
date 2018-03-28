package ru.android.github.data.source.local

import android.content.Context
import android.content.SharedPreferences
import ru.android.github.presentation.App

object Prefs {

    fun setToken(token: String) {
        val editor = getPreferencesEditor()
        editor.putString("token", token)
        editor.apply()
    }

    fun getToken(): String = getPreferences().getString("token", "")

    fun setUsername(name: String) {
        val editor = getPreferencesEditor()
        editor.putString("username", name)
        editor.apply()
    }

    fun getUsername(): String = getPreferences().getString("username", "")

    private fun getPreferences(): SharedPreferences {
        return App.appContext.getSharedPreferences("Preferences", Context.MODE_PRIVATE)
    }

    private fun getPreferencesEditor(): SharedPreferences.Editor {
        val sp = App.appContext.getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        return sp.edit()
    }
}