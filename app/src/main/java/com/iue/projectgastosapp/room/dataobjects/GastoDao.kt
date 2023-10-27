package com.iue.projectgastosapp.room.dataobjects

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import com.iue.projectgastosapp.models.entities.Gasto
import com.iue.projectgastosapp.models.relationship.GastoAndCategoria

@Dao
interface GastoDao {

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM GASTOS JOIN  CATEGORIA ON categoriaGasto=idCategoria where userGasto = :idUser")
    fun getGastosByUser(idUser: String): List<GastoAndCategoria>

    @Insert
    suspend fun insertGasto(gasto: Gasto)
}