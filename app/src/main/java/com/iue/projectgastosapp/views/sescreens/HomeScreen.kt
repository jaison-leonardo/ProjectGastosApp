package com.iue.projectgastosapp.views.sescreens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iue.projectgastosapp.firebase.dataobjects.DataUser
import com.iue.projectgastosapp.viewmodel.DataModelHome
import com.iue.projectgastosapp.views.composable.ShowCircularIndicator
import com.iue.projectgastosapp.views.composable.ShowDialog
import com.iue.projectgastosapp.views.composable.home.CurrentBudgetCard
import com.iue.projectgastosapp.views.composable.home.SavingGoalCard
import com.iue.projectgastosapp.views.composable.home.StatisticCard


@Composable
fun HomeScreen(dataUser: DataUser) {
    val viewModel = remember { DataModelHome() }
    LaunchedEffect(true) {
        viewModel.loadData(dataUser)
    }
    if (viewModel.showLoading.not()) {
        LazyColumn {
            item {
                CurrentBudgetCard(viewModel)
            }
            item {
                StatisticCard(viewModel, dataUser)
            }
            item {
                SavingGoalCard(viewModel)
            }
            item {
                Spacer(modifier = Modifier.size(50.dp))
            }
        }
    }
    ShowDialog(
        show = viewModel.showDialog,
        message = viewModel.message,
        onDismiss = { viewModel.showDialog = false },
        onButtonClick = { viewModel.showDialog = false }
    )
    ShowCircularIndicator(viewModel.showLoading)
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(DataUser("1", "jorge", "arboleda", "jorge@correo.com"))
}