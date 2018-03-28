package ru.android.github.data.abstraction

import io.reactivex.Completable
import io.reactivex.Single
import ru.android.github.domain.model.User


interface UserInfoRepository {

    fun getUser(fromDB: Boolean): Single<User>

    fun deAuthorizeUser() {}

    fun updateUser(userData: Triple<String, String, String>): Completable
}