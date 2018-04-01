package ru.android.github.data.source.remote

import com.rx2androidnetworking.Rx2AndroidNetworking
import io.reactivex.Single
import org.json.JSONArray
import org.json.JSONObject
import ru.android.github.data.converter.Security
import ru.android.github.data.source.DataSource
import ru.android.github.data.source.local.Prefs
import ru.android.github.data.utils.Api

class RemoteDataSource : DataSource {

    private val security = Security()
    private val encryptToken = Prefs.getToken()
    private val username = Prefs.getUsername()
    private val token = if (encryptToken != "") security.decryptString(encryptToken) else ""
    private val headerValue = Api.AUTH_VALUE + token
    private val headerKey = Api.AUTH_HEADER
    private val baseGetUrl = Api.GET_USER + username
    private val patchUserUrl = Api.BASE + Api.PATCH_USER

    override fun authorize(key: String, body: JSONObject): Single<JSONObject> {

        return Rx2AndroidNetworking
                .post(Api.AUTH_URL)
                .addHeaders(headerKey, key)
                .addJSONObjectBody(body)
                .build()
                .jsonObjectSingle
    }

    override fun getUser(): Single<JSONObject> {

        return Rx2AndroidNetworking
                .get(baseGetUrl)
                .addHeaders(headerKey, headerValue)
                .build()
                .jsonObjectSingle
    }

    override fun getStarredRepo(): Single<JSONArray> {

        return Rx2AndroidNetworking
                .get(baseGetUrl + Api.GET_STARRED_REPOS)
                .addHeaders(headerKey, headerValue)
                .build()
                .jsonArraySingle
    }

    override fun getRepos(): Single<JSONArray> {

        return Rx2AndroidNetworking
                .get(baseGetUrl + Api.GET_REPOS)
                .addHeaders(headerKey, headerValue)
                .build()
                .jsonArraySingle
    }

    override fun updateUser(jsonObject: JSONObject): Single<JSONObject> {

        return Rx2AndroidNetworking
                .patch(patchUserUrl)
                .addHeaders(headerKey, headerValue)
                .addJSONObjectBody(jsonObject)
                .build()
                .jsonObjectSingle
    }
}
