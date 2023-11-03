package com.iue.projectgastosapp.views.sescreens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.iue.projectgastosapp.firebase.dataobjects.DataUser
import com.iue.projectgastosapp.ui.theme.BackgroundColor
import com.iue.projectgastosapp.views.composable.TopBar
import com.iue.projectgastosapp.views.composable.home.menudrawer.NavigationListItem
import com.iue.projectgastosapp.views.composable.home.menudrawer.UserContent
import com.iue.projectgastosapp.views.composable.home.menudrawer.getDrawerItems
import com.iue.projectgastosapp.views.composable.home.menudrawer.getMenuItemById
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MenuDrawerScreen(
    profileImage: Painter,
    navController: NavHostController,
    dataUser: DataUser,
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val itemsList = getDrawerItems()
    val selectedItem = remember { mutableStateOf(itemsList[0]) }
    var isFabVisible by remember { mutableStateOf(true) }
    var isFloatingButtonClick by remember { mutableStateOf(false) }

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
                        UserContent(
                            profileImage,
                            "${dataUser.name} ${dataUser.lastName}",
                            dataUser.email
                        )
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
            Scaffold(
                floatingActionButton = {
                    if (isFabVisible) {
                        SmallFloatingActionButton(
                            onClick = {
                                isFloatingButtonClick = true
                            },
                            containerColor = Color(0xFF1E88E5),
                            shape = RoundedCornerShape(50),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "add icon",
                                tint = Color.White
                            )
                        }
                    }
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TopBar(
                        title = selectedItem.value.title,
                        onClickDrawer = { scope.launch { drawerState.open() } },
                        icon = Icons.Default.Menu,
                        contentDescription = "Drawer"
                    )
                    selectedItem.value.screen(navController, dataUser)
                    isFabVisible = selectedItem.value.id == "home"
                }
            }
        }
    )
    if (isFloatingButtonClick) {
        selectedItem.value = getMenuItemById(id = "gastos")
        isFloatingButtonClick = false
    }
}

