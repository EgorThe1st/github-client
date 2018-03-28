package ru.android.github.data.repository

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import org.jetbrains.anko.doAsync
import org.json.JSONArray
import org.json.JSONObject
import ru.android.github.data.abstraction.UserInfoRepository
import ru.android.github.data.converter.Mapper
import ru.android.github.data.entity.UserEntity
import ru.android.github.data.source.DataSource
import ru.android.github.data.source.local.Prefs
import ru.android.github.data.source.remote.RemoteDataSource
import ru.android.github.data.utils.isOnline
import ru.android.github.domain.model.User
import ru.android.github.presentation.App

class UserInfoRepositoryImpl : UserInfoRepository {

    private val db = App.database.userDao()
    private val remoteDataSource: DataSource = RemoteDataSource()
    private val mapper = Mapper()
    private lateinit var userEntity: UserEntity

    override fun getUser(fromDB: Boolean): Single<User> {

        return if (isOnline() && !fromDB) getUserFromRemote() else getUserFromDB()
    }

    override fun deAuthorizeUser() {

        Prefs.setToken("")
        doAsync { db.clearTable() }
    }

    override fun updateUser(userData: Triple<String, String, String>): Completable {

        return remoteDataSource.updateUser(toJsonObject(userData))
                .map {
                    val userEntity = mapper.parseUserEntity(it)
                    db.updateUser(userEntity)
                }
                .toCompletable()
    }

    private fun toJsonObject(userData: Triple<String, String, String>): JSONObject {

        val newDataJson = JSONObject()
        newDataJson.put("name", userData.first)
        newDataJson.put("email", userData.second)
        newDataJson.put("bio", userData.third)

        return newDataJson
    }

    private fun getUserFromRemote(): Single<User> {

        val userObservable: Single<JSONObject> = remoteDataSource.getUser()
        val starredRepositoryObservable: Single<JSONArray> = remoteDataSource.getStarredRepo()

        return Single.zip(
                userObservable,
                starredRepositoryObservable,
                BiFunction<JSONObject, JSONArray, User> { user, sRepos ->
                    combineUserWithStarredRepos(user, sRepos)
                    db.insertUser(userEntity)
                    mapper.convertToUser(userEntity)
                }
        )
    }

    private fun combineUserWithStarredRepos(user: JSONObject, sRepos: JSONArray) {
        userEntity = mapper.parseUserEntity(user)
        val starredRepoCont = sRepos.length()
        userEntity.starredReposCount = starredRepoCont
    }

    private fun getUserFromDB(): Single<User> = db.getUser().map { mapper.convertToUser(it) }
}

