package ru.android.github.data.abstraction

import io.reactivex.Completable
import org.json.JSONObject


interface AuthorizeRepository {

    fun makeRequest(key: String, body: JSONObject, userName: String): Completable

    fun getToken(): String
}