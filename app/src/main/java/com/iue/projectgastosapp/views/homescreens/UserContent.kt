package com.iue.projectgastosapp.views.homescreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UserContent(profileImage: Painter, nameUser: String, emailUser: String) {
    Image(
        modifier = Modifier
            .size(size = 120.dp)
            .clip(shape = CircleShape),
        painter = profileImage,
        contentDescription = "Profile Image"
    )
    Text(
        modifier = Modifier
            .padding(top = 12.dp),
        text = nameUser,
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White
    )
    Text(
        modifier = Modifier.padding(top = 8.dp, bottom = 30.dp),
        text = emailUser,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = Color.White
    )
}