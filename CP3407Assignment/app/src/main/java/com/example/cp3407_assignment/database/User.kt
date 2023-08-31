package com.example.cp3407_assignment.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int,

    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "password") val password: String,

)