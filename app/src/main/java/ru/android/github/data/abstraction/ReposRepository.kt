package ru.android.github.data.abstraction

import io.reactivex.Single
import ru.android.github.domain.model.Repository

interface ReposRepository {

    fun getRepos(): Single<List<Repository>>

    fun getReposFromNet(): Single<List<Repository>>

    fun getOneLanguage(lang: String): Single<List<Repository>>
}