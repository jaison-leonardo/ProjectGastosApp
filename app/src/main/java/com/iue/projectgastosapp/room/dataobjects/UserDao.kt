package com.iue.projectgastosapp.room.dataobjects

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.iue.projectgastosapp.models.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM user WHERE idUser = :id")
    fun getUserById(id: String): Flow<User>

    @Query("SELECT * FROM user WHERE email = :email")
    fun getUserByEmail(email: String): Flow<User>

    @Query("SELECT COUNT(*) FROM user WHERE email = :email")
    fun getCountUserByEmail(email: String): Flow<Int>

    @Insert
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)
}
