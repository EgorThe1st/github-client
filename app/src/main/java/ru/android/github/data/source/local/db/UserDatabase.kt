package ru.android.github.data.source.local.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import ru.android.github.data.entity.RepoEntity
import ru.android.github.data.entity.UserEntity
import ru.android.github.data.source.local.dao.ReposDao
import ru.android.github.data.source.local.dao.UserDao

@Database(
        entities = [UserEntity::class, RepoEntity::class],
        version = 1
)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun reposDao(): ReposDao
}