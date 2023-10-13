package com.iue.projectgastosapp.views.homescreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iue.projectgastosapp.R


data class MenuItem(
    val id: String,
    val title: String,
    val contentDescription: String,
    val icon: Painter
)

@Composable
fun NavigationListItem(
    item: MenuItem,
    isSelected: Boolean,
    itemClick: () -> Unit
) {
    val alpha = 0.2f
    val backgroundColor =
        if (isSelected) Color(0, 0, 0, (alpha * 255).toInt()) else Color.Transparent
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                itemClick()
            }
            .padding(horizontal = 24.dp, vertical = 10.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(16.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .padding(start = 16.dp),
            painter = item.icon,
            contentDescription = null,
            tint = Color.White
        )
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = item.title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}


@Composable
fun getDrawerItems(): List<MenuItem> {
    return listOf(
        MenuItem(
            id = "home",
            title = "Inicio",
            contentDescription = "go to screen home",
            icon = painterResource(id = R.drawable.home_48)
        ),
        MenuItem(
            id = "gastos",
            title = "Registro de Gastos",
            contentDescription = "go to screen expenses",
            icon = painterResource(id = R.drawable.outline_payments_48)
        ),
        MenuItem(
            id = "presupuesto",
            title = "Presupuesto Mensual",
            contentDescription = "go to screen budget",
            icon = painterResource(id = R.drawable.budget_48)
        ),
        MenuItem(
            id = "reportes",
            title = "Análisis y Reportes",
            contentDescription = "go to screen reports",
            icon = painterResource(id = R.drawable.report_48)
        ),
        MenuItem(
            id = "notificaciones",
            title = "Notificaciones y Alertas",
            contentDescription = "go to screen notifications",
            icon = painterResource(id = R.drawable.notifications_48)
        ),
        MenuItem(
            id = "ahorro",
            title = "Metas de ahorro",
            contentDescription = "go to screen goals",
            icon = painterResource(id = R.drawable.goals_48)
        ),
        MenuItem(
            id = "seguridad",
            title = "Seguridad y Privacidad",
            contentDescription = "go to screen security",
            icon = painterResource(id = R.drawable.security_48)
        ),
        MenuItem(
            id = "backup",
            title = "Respaldo de datos",
            contentDescription = "go to screen backup",
            icon = painterResource(id = R.drawable.backup_48)
        ),
        MenuItem(
            id = "logout",
            title = "Cerrar sesión",
            contentDescription = "go to screen logout",
            icon = painterResource(id = R.drawable.logout_48)
        )
    )
}