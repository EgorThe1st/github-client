package ru.android.github.presentation.abstraction

import com.androidnetworking.error.ANError
import org.json.JSONObject


interface BasePresenter {

    fun start()

    fun parseError(error: Throwable): String {

        val anError = error as? ANError
        anError?.let {
            val errorObj: JSONObject
            try {
                errorObj = JSONObject(it.errorBody.toString())
            } catch (e: Exception) {
                return "Error occurred"
            }
            return "Error: ${errorObj.getString("message")}"
        }
        return "Error occurred"
    }

    fun handleOnDestroy()
}
