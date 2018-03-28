package ru.android.github.data.repository

import io.reactivex.Single
import ru.android.github.data.abstraction.ReposRepository
import ru.android.github.data.converter.Mapper
import ru.android.github.data.source.DataSource
import ru.android.github.data.source.remote.RemoteDataSource
import ru.android.github.data.utils.isOnline
import ru.android.github.domain.model.Repository
import ru.android.github.presentation.App

class ReposRepositoryImpl : ReposRepository {

    private val remoteDataSource: DataSource = RemoteDataSource()
    private val mapper = Mapper()
    private val db = App.database.reposDao()

    override fun getRepos(): Single<List<Repository>> {

        return if (isOnline()) {
            getReposFromRemote()
        } else {
            getReposFromDB()
        }
    }

    override fun getOneLanguage(lang: String): Single<List<Repository>> {

        return db.getLanguageRepos(lang)
                .map { mapper.convertToRepos(it) }
    }

    override fun getReposFromNet(): Single<List<Repository>> {

        return getReposFromRemote()
    }

    private fun getReposFromRemote(): Single<List<Repository>> {

        return remoteDataSource.getRepos()
                .map {
                    val repoEntities = mapper.parseRepos(it)
                    if (!repoEntities.isEmpty()) {
                        db.updateRepo(repoEntities)
                    } else {
                        db.clearTable()
                    }
                    mapper.convertToRepos(repoEntities)
                }
    }

    private fun getReposFromDB(): Single<List<Repository>> {

        return db.getRepos().map { mapper.convertToRepos(it) }
    }
}