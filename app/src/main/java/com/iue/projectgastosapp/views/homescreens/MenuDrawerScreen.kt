package com.iue.projectgastosapp.views.homescreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iue.projectgastosapp.R
import com.iue.projectgastosapp.ui.theme.BackgroundColor
import com.iue.projectgastosapp.views.composable.TopBar
import kotlinx.coroutines.launch


@Composable
fun MenuDrawerScreen(profileImage: Painter, nameUser: String, emailUser: String) {
    val drawerState = rememberDrawerState(DrawerValue.Open)
    val scope = rememberCoroutineScope()
    val itemsList = getDrawerItems()
    val selectedItem = remember { mutableStateOf(itemsList[0]) }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(brush = Brush.verticalGradient(colors = BackgroundColor)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(vertical = 36.dp)
                ) {

                    item {
                        UserContent(profileImage, nameUser, emailUser)
                    }
                    items(itemsList) { item ->
                        NavigationListItem(
                            item = item,
                            isSelected = item == selectedItem.value,
                            itemClick = {
                                selectedItem.value = item
                                scope.launch { drawerState.close() }
                            }
                        )
                    }
                }
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopBar(
                    title = "Home",
                    onClickDrawer = { scope.launch { drawerState.open() } },
                    icon = Icons.Default.Menu,
                    contentDescription = "Drawer"
                )
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun MenuDrawerScreenPreview() {
    val profileImage = painterResource(id = R.drawable.logo)
    val nameUser = "Jaison Arboleda"
    val emailUser = "jaison@correo.com"
    MenuDrawerScreen(profileImage, nameUser, emailUser)
}

