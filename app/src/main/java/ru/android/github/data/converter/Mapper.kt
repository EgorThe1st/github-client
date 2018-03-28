package ru.android.github.data.converter

import org.json.JSONArray
import org.json.JSONObject
import ru.android.github.data.entity.RepoEntity
import ru.android.github.data.entity.UserEntity
import ru.android.github.domain.model.Repository
import ru.android.github.domain.model.User

class Mapper {

    companion object {
        private const val DB_ID = 1
        private const val USER_ID = 1
        private const val REPOS_START_COUNT = 0
        private const val TO_MEGA_BYTE = 0.001
    }

    fun parseUserEntity(userJson: JSONObject): UserEntity {
        with(userJson) {
            return UserEntity(
                    DB_ID,
                    getString("name"),
                    getString("email"),
                    getString("bio"),
                    getInt("disk_usage"),
                    getBoolean("site_admin"),
                    getString("created_at"),
                    getString("avatar_url"),
                    getInt("public_repos"),
                    getInt("total_private_repos"),
                    REPOS_START_COUNT,
                    getInt("followers"),
                    getInt("following"),
                    getInt("public_gists"),
                    getInt("private_gists")
            )
        }
    }

    fun parseRepos(response: JSONArray): List<RepoEntity> {

        return if (response.length() > 0) {

            val repoEntities = ArrayList<RepoEntity>()

            for (i in 0 until response.length()) {
                val obj = response.getJSONObject(i)
                repoEntities.add(i,
                        RepoEntity(
                                obj.getString("name"),
                                obj.getString("language"),
                                USER_ID
                        )
                )
            }
            repoEntities
        } else emptyList()
    }

    fun convertToUser(userEntity: UserEntity): User {

        val nameForUser: String = if (userEntity.name == "null") "" else userEntity.name
        val emailForUser: String = if (userEntity.email == "null") "" else userEntity.email
        val bioForUser: String = if (userEntity.bio == "null") "" else userEntity.bio
        val dateFromServer = userEntity.createdAt
        val dateForUser = toDate(dateFromServer)
        val discUsageForUser = (userEntity.discUsage * TO_MEGA_BYTE).toString()

        val reposSum = (userEntity.publicReposCount + userEntity.privateReposCount).toString()
        val starredReposCountForUser = userEntity.starredReposCount.toString()
        val followersCountForUser = userEntity.followersCount.toString()
        val followingCountForUser = userEntity.followingCount.toString()
        val gistsSum = (userEntity.privateGistsCount + userEntity.publicGistsCount).toString()

        with(userEntity) {
            return User(
                    nameForUser,
                    emailForUser,
                    bioForUser,
                    discUsageForUser,
                    siteAdmin,
                    dateForUser,
                    avatarUrl,
                    reposSum,
                    starredReposCountForUser,
                    followersCountForUser,
                    followingCountForUser,
                    gistsSum
            )
        }
    }

    fun convertToRepos(repoEntities: List<RepoEntity>?): List<Repository> {

        if (repoEntities == null || repoEntities.isEmpty()) return emptyList()

        val reposList = ArrayList<Repository>()
        repoEntities.map { reposList.add(Repository(it.name, it.language)) }

        return reposList
    }

    private fun toDate(dateFromServer: String): String {
        with(dateFromServer) {
            val year = substring(0, 4)
            val month = substring(5, 7)
            val day = substring(8, 10)

            return "$day/$month/$year"
        }
    }
}