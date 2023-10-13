package com.iue.projectgastosapp.views.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iue.projectgastosapp.R

@Composable
fun TopBar(
    title: String,
    onClickDrawer: () -> Unit,
    icon: ImageVector,
    contentDescription: String
) {
    Box(
        modifier = Modifier
            .background(color = Color(0xFF2196F3))
            .fillMaxWidth()
            .height(117.dp)
    ) {
        IconButton(
            modifier = Modifier
                .padding(start = 5.dp)
                .align(Alignment.CenterStart),
            onClick = { onClickDrawer() }) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = Color.White,
                modifier = Modifier
                    .size(64.dp)
            )
        }
        Text(
            text = title,
            color = Color.White,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .align(Alignment.Center)
        )
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = R.string.content_desc_se_logo.toString(),
            modifier = Modifier
                .size(64.dp)
                .padding(end = 16.dp)
                .align(Alignment.CenterEnd)
        )
    }
}

@Composable
fun TopBarApp(
    title: String,
    onClickDrawer: () -> Unit = {},
    icon: ImageVector,
    contentDescription: String
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(
                onClick = { onClickDrawer() }) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(Color(0xFF2196F3)),
        actions = {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 16.dp)
            )
        }
    )
}


@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    TopBar(
        title = "TopBar",
        icon = Icons.Default.ArrowBack,
        contentDescription = "ArrowBack",
        onClickDrawer = {})
}

@Preview(showBackground = true)
@Composable
fun TopBarAppPreview() {
    TopBarApp(title = "TopBar", icon = Icons.Default.ArrowBack, contentDescription = "ArrowBack")
}
