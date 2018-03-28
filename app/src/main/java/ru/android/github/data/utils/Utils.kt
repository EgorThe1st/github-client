package ru.android.github.data.utils

import android.content.Context
import android.net.ConnectivityManager
import ru.android.github.presentation.App

fun isOnline(): Boolean {
    val connectivityManager = App.appContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return (networkInfo != null && networkInfo.isConnected)
}