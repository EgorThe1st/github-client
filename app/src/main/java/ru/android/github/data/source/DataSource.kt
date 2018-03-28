package ru.android.github.data.source

import io.reactivex.Single
import org.json.JSONArray
import org.json.JSONObject

interface DataSource {

    fun authorize(key: String, body: JSONObject): Single<JSONObject>

    fun getUser(): Single<JSONObject>

    fun getRepos(): Single<JSONArray>

    fun getStarredRepo(): Single<JSONArray>

    fun updateUser(jsonObject: JSONObject): Single<JSONObject>
}