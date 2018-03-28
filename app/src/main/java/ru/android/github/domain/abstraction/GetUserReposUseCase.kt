package ru.android.github.domain.abstraction

import io.reactivex.Single
import ru.android.github.domain.model.Repository

interface GetUserReposUseCase {

    fun getRepos(): Single<List<Repository>>

    fun updateReposList(): Single<List<Repository>>

    fun getOneLanguage(lang: String): Single<List<Repository>>
}