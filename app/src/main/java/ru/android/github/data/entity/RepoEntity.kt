package ru.android.github.data.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "repositories",
        foreignKeys = [
            (ForeignKey(
                    entity = UserEntity::class,
                    parentColumns = arrayOf("id"),
                    childColumns = arrayOf("user_id"),
                    onDelete = ForeignKey.CASCADE,
                    onUpdate = ForeignKey.CASCADE
            ))
        ]
)
data class RepoEntity(

        @PrimaryKey
        @ColumnInfo(name = "name")
        var name: String,

        @ColumnInfo(name = "language")
        var language: String,

        @ColumnInfo(name = "user_id")
        var userId: Int
)