package ru.android.github.domain.model

data class User(
        val name: String,
        val email: String,
        val bio: String,
        val discUsage: String,
        val siteAdmin: Boolean,
        val createdAt: String,
        val avatarUrl: String,
        val reposCount: String,
        val starredReposCount: String,
        val followersCount: String,
        val followingCount: String,
        val gistsCount: String
)