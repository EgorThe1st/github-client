package ru.android.github.domain.abstraction

import io.reactivex.Single
import ru.android.github.domain.model.User


interface GetUserInfoUseCase {

    fun getUserInfo(fromDB: Boolean): Single<User>

}