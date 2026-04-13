package jesusernesto.lopezibarra.gestorgastos.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jesusernesto.lopezibarra.gestorgastos.ui.theme.*

@Composable
fun BottomNavBar(
    tabActivo: String = "Inicio",
    onTabSelected: (String) -> Unit = {},
    onNewMovement: () -> Unit = {}
) {
    data class NavItem(val label: String, val icon: ImageVector)

    val items = listOf(
        NavItem("Inicio", Icons.Outlined.Home),
        NavItem("Presupuesto", Icons.Outlined.BarChart),
        NavItem("Grupos", Icons.Outlined.Groups),
        NavItem("Perfil", Icons.Outlined.Person)
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp,
    ) {
        items.forEachIndexed { index, item ->
            if (index == 2) {
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
                            .clickable { onNewMovement() },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = "+", fontSize = 24.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
            val isSelected = item.label == tabActivo

            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(item.label) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Purple,
                    selectedTextColor = Purple,
                    unselectedIconColor = TextGray,
                    unselectedTextColor = TextGray,
                    indicatorColor = Color.Transparent,
                ),
            )
        }
    }
}
