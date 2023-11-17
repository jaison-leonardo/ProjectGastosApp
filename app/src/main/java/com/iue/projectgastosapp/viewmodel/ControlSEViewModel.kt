package com.iue.projectgastosapp.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iue.projectgastosapp.models.entities.Gasto
import com.iue.projectgastosapp.models.entities.User
import com.iue.projectgastosapp.models.relationship.GastoAndCategoria
import com.iue.projectgastosapp.room.dataobjects.GastoDao
import com.iue.projectgastosapp.room.dataobjects.UserDao
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ControlSEViewModel(
    private val usuarioDao: UserDao,
    private val gastoDao: GastoDao
) : ViewModel() {
    var userById by mutableStateOf(User("", "", "", ""))
        private set

    var userByEmail by mutableStateOf(User("", "", "", ""))
        private set

    var countUser by mutableStateOf(0)
        private set

    var userAll by mutableStateOf(listOf<User>())
        private set

    var staticGastosCategoria by mutableStateOf(listOf<GastoAndCategoria>())
        private set

    init {
        viewModelScope.launch {
            usuarioDao.getAllUsers().collectLatest {
                userAll = userAll.plus(it)
                Log.i("userAll", userAll.size.toString())
            }
        }
    }


    // User
    fun getUserById(id: String) {
        viewModelScope.launch {
            usuarioDao.getUserById(id).collectLatest { user ->
                userById = user
                Log.i("userById", user.toString())
            }
        }
    }

    fun getUserByEmail(email: String) {
        viewModelScope.launch {
            usuarioDao.getUserByEmail(email).collectLatest { user ->
                userByEmail = user
                Log.i("userByEmail", user.toString())
            }
        }
    }

    fun getCountUserByEmail(email: String) {
        viewModelScope.launch {
            usuarioDao.getCountUserByEmail(email).collectLatest { count ->
                countUser = count
                Log.i("count_email", count.toString())
            }
        }
    }

    fun insertUser(user: User) {
        viewModelScope.launch {
            usuarioDao.insertUser(user)
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            usuarioDao.updateUser(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            usuarioDao.deleteUser(user)
        }
    }

    // Gasto
    fun getGastosByUser(idUser: String) {
        viewModelScope.launch {
            staticGastosCategoria = gastoDao.getGastosByUser(idUser)
        }
    }

    fun insertGasto(gasto: Gasto) {
        viewModelScope.launch {
            gastoDao.insertGasto(gasto)
        }
    }

}