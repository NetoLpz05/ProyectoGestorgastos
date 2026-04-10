package jesusernesto.lopezibarra.gestorgastos.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jesusernesto.lopezibarra.gestorgastos.ui.theme.*

@Composable
fun BottomNavBar() {
    data class NavItem(val label: String, val icon: String, val isActive: Boolean = false)

    val items = listOf(
        NavItem("Inicio",      "🏠"),
        NavItem("Presupuesto", "📊"),
        NavItem("+",           ""),
        NavItem("Grupos",      "👥", true),
        NavItem("Perfil",      "👤"),
    )

    NavigationBar(
        containerColor = White,
        tonalElevation = 4.dp,
    ) {
        items.forEach { item ->
            if (item.label == "+") {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Purple)
                            .clickable { },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = "+", fontSize = 24.sp, color = White, fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                NavigationBarItem(
                    selected = item.isActive,
                    onClick = {},
                    icon = { Text(text = item.icon, fontSize = if (item.isActive) 22.sp else 20.sp) },
                    label = {
                        Text(
                            text = item.label,
                            fontSize = 10.sp,
                            color = if (item.isActive) Purple else TextGray,
                            fontWeight = if (item.isActive) FontWeight.SemiBold else FontWeight.Normal,
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor   = Purple,
                        selectedTextColor   = Purple,
                        unselectedIconColor = TextGray,
                        unselectedTextColor = TextGray,
                        indicatorColor      = Color.Transparent,
                    ),
                )
            }
        }
    }
}