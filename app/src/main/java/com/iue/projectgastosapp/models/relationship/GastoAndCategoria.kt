package com.iue.projectgastosapp.models.relationship

import androidx.room.Embedded
import androidx.room.Relation
import com.iue.projectgastosapp.models.entities.Categoria
import com.iue.projectgastosapp.models.entities.Gasto

data class GastoAndCategoria(
    @Embedded val categoria: Categoria,
    @Relation(
        parentColumn = "idCategoria",
        entityColumn = "categoriaGasto"
    )
    val gasto: Gasto
)

