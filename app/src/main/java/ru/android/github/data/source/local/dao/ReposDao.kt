package ru.android.github.data.source.local.dao

import android.arch.persistence.room.*
import io.reactivex.Single
import ru.android.github.data.entity.RepoEntity

@Dao
abstract class ReposDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertRepo(repo: RepoEntity)

    @Insert
    abstract fun insertAll(repos: List<RepoEntity>)

    @Transaction
    open fun updateRepo(repos: List<RepoEntity>) {
        clearTable()
        insertAll(repos)
    }

    @Delete
    abstract fun deleteRepo(repo: RepoEntity)

    @Query("SELECT * FROM repositories")
    abstract fun getRepos(): Single<List<RepoEntity>>

    @Query("SELECT * FROM repositories WHERE language = :lang")
    abstract fun getLanguageRepos(lang: String): Single<List<RepoEntity>>

    @Query("DELETE from repositories")
    abstract fun clearTable()


}