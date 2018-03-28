package ru.android.github.domain.usecase

import android.util.Base64
import io.reactivex.Completable
import org.json.JSONException
import org.json.JSONObject
import ru.android.github.data.abstraction.AuthorizeRepository
import ru.android.github.data.repository.AuthorizeRepositoryImpl
import ru.android.github.domain.abstraction.AuthorizeUserUseCase

class AuthorizeUserUseCaseImpl(
        private val callback: AuthorizeUserUseCase.Callback
) : AuthorizeUserUseCase {

    private var authorizeRepository: AuthorizeRepository = AuthorizeRepositoryImpl()

    override fun authorize(name: String, password: String): Completable {

        val authValue = createHeaderValue(name, password)
        val params = prepareJson(JSONObject())

        return authorizeRepository.makeRequest(authValue, params, name)
    }

    override fun checkIfTokenAlreadyExist() {
        val token = authorizeRepository.getToken()
        if (token != "") callback.onTokenAlreadyExist()
    }

    private fun createHeaderValue(name: String, password: String): String {

        val userData = "$name:$password"
        val authBase64 = Base64.encodeToString(
                userData.toByteArray(),
                Base64.NO_WRAP
        )
        return "Basic $authBase64"
    }

    private fun prepareJson(jO: JSONObject): JSONObject {

        try {
            jO.put("scopes", "user")
            jO.put("note", Math.random().toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return jO
    }
}