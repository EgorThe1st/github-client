package ru.android.github.domain.abstraction

import io.reactivex.Completable


interface AuthorizeUserUseCase {

    fun authorize(name: String, password: String): Completable

    fun checkIfTokenAlreadyExist()

    interface Callback {

        fun onUserAuthorized()

        fun onAuthorizationFailed(error: String)

        fun onTokenAlreadyExist()
    }
}