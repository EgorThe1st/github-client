package ru.android.github.data.repository

import io.reactivex.Completable
import org.json.JSONObject
import ru.android.github.data.abstraction.AuthorizeRepository
import ru.android.github.data.converter.Security
import ru.android.github.data.source.local.Prefs
import ru.android.github.data.source.remote.RemoteDataSource

open class AuthorizeRepositoryImpl : AuthorizeRepository {

    private var name: String = ""

    override fun makeRequest(key: String, body: JSONObject, userName: String): Completable {

        name = userName
        val remoteDataSource = RemoteDataSource()

        return remoteDataSource.authorize(key, body)
                .map { onDataReceive(it) }
                .toCompletable()
    }

    private fun onDataReceive(response: JSONObject) {
        val token = response.getString("token")
        val encrypted = Security().encryptString(token)
        Prefs.setToken(encrypted)
        Prefs.setUsername(name)
    }

    override fun getToken(): String = Prefs.getToken()

}