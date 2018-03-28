package ru.android.github.domain.usecase

import ru.android.github.data.repository.UserInfoRepositoryImpl
import ru.android.github.domain.abstraction.DeAuthorizeUserUseCase

class DeAuthorizeUserUseCaseImpl : DeAuthorizeUserUseCase {

    private val userInfoRepository = UserInfoRepositoryImpl()

    override fun deAuthorize() {
        userInfoRepository.deAuthorizeUser()
    }
}