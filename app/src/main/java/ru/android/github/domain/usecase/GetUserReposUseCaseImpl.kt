package ru.android.github.domain.usecase

import io.reactivex.Single
import ru.android.github.data.repository.ReposRepositoryImpl
import ru.android.github.domain.abstraction.GetUserReposUseCase
import ru.android.github.domain.model.Repository

class GetUserReposUseCaseImpl : GetUserReposUseCase {

    private val reposRepository = ReposRepositoryImpl()

    override fun getRepos(): Single<List<Repository>> = reposRepository.getRepos()

    override fun updateReposList(): Single<List<Repository>> = reposRepository.getReposFromNet()

    override fun getOneLanguage(lang: String): Single<List<Repository>> = reposRepository.getOneLanguage(lang)
}