package ru.android.github.data.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(

        @PrimaryKey
        @ColumnInfo(name = "id")
        var id: Int,

        @ColumnInfo(name = "name")
        var name: String,

        @ColumnInfo(name = "email")
        var email: String,

        @ColumnInfo(name = "bio")
        var bio: String,

        @ColumnInfo(name = "disk_usage")
        var discUsage: Int,

        @ColumnInfo(name = "site_admin")
        var siteAdmin: Boolean,

        @ColumnInfo(name = "created_at")
        var createdAt: String,

        @ColumnInfo(name = "avatar_url")
        var avatarUrl: String,

        @ColumnInfo(name = "public_repos_count")
        var publicReposCount: Int,

        @ColumnInfo(name = "private_repos_count")
        var privateReposCount: Int,

        @ColumnInfo(name = "starred_repos_count")
        var starredReposCount: Int,

        @ColumnInfo(name = "followers_count")
        var followersCount: Int,

        @ColumnInfo(name = "following_count")
        var followingCount: Int,

        @ColumnInfo(name = "public_gists_count")
        var publicGistsCount: Int,

        @ColumnInfo(name = "private_gists_count")
        var privateGistsCount: Int
)