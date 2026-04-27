package jesusernesto.lopezibarra.gestorgastos.screens.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import jesusernesto.lopezibarra.gestorgastos.ui.theme.DarkNavy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(title: String, onBack: () -> Unit) {
    TopAppBar(
        title = { Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Regresar",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
    )
}
