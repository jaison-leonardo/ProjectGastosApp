package com.iue.projectgastosapp.views.sescreens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.iue.projectgastosapp.firebase.dataobjects.DataUser
import com.iue.projectgastosapp.views.composable.home.CurrentBudgetCard
import com.iue.projectgastosapp.views.composable.home.SavingGoalCard
import com.iue.projectgastosapp.views.composable.home.StatisticCard


@Composable
fun HomeScreen(dataUser: DataUser) {
    LazyColumn(content = {
        item {
            StatisticCard()
        }
        item {
            SavingGoalCard(dataUser)
        }
        item {
            CurrentBudgetCard(dataUser)
        }
    })
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(DataUser("1", "jorge", "arboleda", "jorge@correo.com"))
}