package com.iue.projectgastosapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.iue.projectgastosapp.models.entities.Categoria
import com.iue.projectgastosapp.models.entities.Gasto
import com.iue.projectgastosapp.models.entities.User
import com.iue.projectgastosapp.room.dataobjects.GastoDao
import com.iue.projectgastosapp.room.dataobjects.UserDao

@Database(
    entities = [User::class, Gasto::class, Categoria::class],
    version = 1,
    exportSchema = false
)
abstract class DatabaseControlSE : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun gastoDao(): GastoDao

    companion object {
        private const val DATABASE_NAME = "control_se.bd"
        fun getInstance(context: Context): DatabaseControlSE {
            return Room.databaseBuilder(
                context.applicationContext,
                DatabaseControlSE::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}