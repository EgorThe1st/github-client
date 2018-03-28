package ru.android.github.data.source.local.dao

import android.arch.persistence.room.*
import io.reactivex.Single
import ru.android.github.data.entity.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: UserEntity)

    @Update
    fun updateUser(user: UserEntity)

    @Delete
    fun deleteUser(user: UserEntity)

    @Query("SELECT * FROM user")
    fun getUser(): Single<UserEntity>

    @Query("DELETE from user")
    fun clearTable()
}