package jesusernesto.lopezibarra.gestorgastos.screens.group

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jesusernesto.lopezibarra.gestorgastos.data.Grupo
import jesusernesto.lopezibarra.gestorgastos.data.enums.CategoriaGrupo
import jesusernesto.lopezibarra.gestorgastos.data.gruposEjemplo
import jesusernesto.lopezibarra.gestorgastos.ui.theme.Background
import jesusernesto.lopezibarra.gestorgastos.ui.theme.DarkNavy
import jesusernesto.lopezibarra.gestorgastos.ui.theme.Purple
import jesusernesto.lopezibarra.gestorgastos.ui.theme.PurpleGrey40
import jesusernesto.lopezibarra.gestorgastos.ui.theme.PurpleLight
import jesusernesto.lopezibarra.gestorgastos.ui.theme.White



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisGruposScreen(
    grupos: List<Grupo>           = gruposEjemplo,
    onAtras: () -> Unit           = {},
    onAbrirGrupo: (Grupo) -> Unit = {},
    onCrearGrupo: () -> Unit      = {},
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Mis Grupos", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface) },
                navigationIcon = {
                    IconButton(onClick = onAtras) {
                        Icon(Icons.Filled.ArrowBack, "Regresar", tint = MaterialTheme.colorScheme.onSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(Modifier.height(8.dp)) }
            items(grupos, key = { it.id }) { grupo ->
                GrupoCard(grupo = grupo, onClick = { onAbrirGrupo(grupo) })
            }
            item {
                CrearGrupoCard(onClick = onCrearGrupo)
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun GrupoCard(grupo: Grupo, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = grupo.nombre,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.People,
                        contentDescription = null,
                        tint = PurpleGrey40,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "${grupo.miembros} miembros",
                        fontSize = 14.sp,
                        color = PurpleGrey40
                    )
                }
            }

            Box(modifier = Modifier.width(1.dp).fillMaxHeight().background(PurpleLight.copy(alpha = 0.3f)))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.width(90.dp).fillMaxHeight()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(fondoIcono(grupo.iconoCategoria))
                ) {
                    Icon(
                        imageVector = iconoVector(grupo.iconoCategoria),
                        contentDescription = null,
                        tint = colorIcono(grupo.iconoCategoria),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CrearGrupoCard(onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
        border = BorderStroke(width = 1.dp, color = PurpleLight),
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.GroupAdd,
                contentDescription = null,
                tint = PurpleGrey40,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.height(12.dp))
            Text("Crear un nuevo grupo", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Text(
                "Divide gastos de cenas o viajes",
                fontSize = 12.sp,
                color = PurpleGrey40,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun fondoIcono(cat: CategoriaGrupo) = when (cat) {
    CategoriaGrupo.PAREJA   -> Color(0xFFFEECEC)
    CategoriaGrupo.VIAJES   -> Color(0xFFFEF0E4)
    CategoriaGrupo.CASA     -> Color(0xFFE4F0FE)
    CategoriaGrupo.AMIGOS   -> Color(0xFFE4FEEE)
    CategoriaGrupo.TRABAJO  -> Color(0xFFEEE4FE)
    CategoriaGrupo.OTRO     -> Color(0xFFF2F2F7)
}
private fun colorIcono(cat: CategoriaGrupo) = when (cat) {
    CategoriaGrupo.PAREJA   -> Color(0xFFE05252)
    CategoriaGrupo.VIAJES   -> Color(0xFFBF7B3A)
    CategoriaGrupo.CASA     -> Color(0xFF3A7ABF)
    CategoriaGrupo.AMIGOS   -> Color(0xFF3ABF7A)
    CategoriaGrupo.TRABAJO  -> Color(0xFF7A3ABF)
    CategoriaGrupo.OTRO     -> Color(0xFF888888)
}
private fun iconoVector(cat: CategoriaGrupo): ImageVector = when (cat) {
    CategoriaGrupo.PAREJA   -> Icons.Filled.Favorite
    CategoriaGrupo.VIAJES   -> Icons.Filled.Flight
    CategoriaGrupo.CASA     -> Icons.Filled.Home
    CategoriaGrupo.AMIGOS   -> Icons.Filled.People
    CategoriaGrupo.TRABAJO  -> Icons.Filled.Work
    CategoriaGrupo.OTRO     -> Icons.Filled.Category
}

@Preview(showBackground = true)
@Composable
fun MisGruposScreenPreview() {
    MaterialTheme { MisGruposScreen() }
}
