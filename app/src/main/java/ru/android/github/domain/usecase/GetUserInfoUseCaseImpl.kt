package ru.android.github.domain.usecase

import io.reactivex.Single
import ru.android.github.data.repository.UserInfoRepositoryImpl
import ru.android.github.domain.abstraction.GetUserInfoUseCase
import ru.android.github.domain.model.User


class GetUserInfoUseCaseImpl : GetUserInfoUseCase {

    private val userInfoRepository = UserInfoRepositoryImpl()

    override fun getUserInfo(fromDB: Boolean): Single<User> = userInfoRepository.getUser(fromDB)
}