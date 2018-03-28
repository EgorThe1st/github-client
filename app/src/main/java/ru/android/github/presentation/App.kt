package ru.android.github.presentation

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.androidnetworking.AndroidNetworking
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import ru.android.github.BuildConfig
import ru.android.github.data.source.local.db.UserDatabase
import java.io.BufferedReader
import java.io.InputStreamReader

class App : Application() {

    companion object {
        lateinit var appContext: Context
            private set

        lateinit var database: UserDatabase

        fun canExecuteSuCommands(): Boolean {

            val suCmds = arrayListOf(
                    "/system/xbin/which su",
                    "/system/bin/which su",
                    "which su"
            )

            suCmds.forEach {
                try {
                    val process = Runtime.getRuntime().exec(it)
                    val reader = BufferedReader(InputStreamReader(process.inputStream))
                    val suPath = reader.readLine()

                    if (!suPath.isNullOrEmpty()) {
                        Runtime.getRuntime().exec(it)
                        return true
                    }
                } catch (e: Exception) {
                    return false
                }
            }
            return false
        }

    }

    override fun onCreate() {
        super.onCreate()

        AndroidNetworking.initialize(applicationContext, getClient())
        appContext = applicationContext

        database = Room.databaseBuilder(this, UserDatabase::class.java, "user_db")
                .build()
    }


    private fun getClient(): OkHttpClient {

        val logging = LoggingInterceptor.Builder()
                .loggable(BuildConfig.DEBUG)
                .setLevel(Level.BASIC)
                .build()

        return OkHttpClient.Builder()
                .addInterceptor(logging)
                .certificatePinner(CertificatePinner.Builder()
                        .add("api.github.com",
                                "sha256/VRtYBz1boKOXjChfZYssN1AeNZCjywl77l2RTl/v380=")
                        .add("api.github.com",
                                "sha256/k2v657xBsOVe1PQRwOsHsw3bsGT2VzIqz5K+59sNQws=")
                        .add("api.github.com",
                                "sha256/WoiWRyIOVNa9ihaBciRSC7XHjliYS9VwUGOIud4PB18=")
                        .build())
                .build()
    }
}