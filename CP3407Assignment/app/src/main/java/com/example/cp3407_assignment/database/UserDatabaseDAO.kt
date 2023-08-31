package com.example.cp3407_assignment.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDatabaseDAO {
    @Query("SELECT * FROM user_table")
    fun getAll(): List<User>

    @Query("SELECT * FROM user_table ORDER BY id DESC LIMIT 1")
    fun getUser(): User?

    @Query("SELECT * FROM user_table WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user_table WHERE username LIKE :first AND " +
            "password LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): User

    @Insert
    fun insert(user: User)

    @Delete
    fun delete(user: User)
}