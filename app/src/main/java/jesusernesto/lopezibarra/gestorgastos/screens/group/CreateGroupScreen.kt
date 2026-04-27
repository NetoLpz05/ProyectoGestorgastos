package jesusernesto.lopezibarra.gestorgastos.screens.group


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jesusernesto.lopezibarra.gestorgastos.dummy.DummyData.groupTypes
import jesusernesto.lopezibarra.gestorgastos.dummy.DummyData.GroupType
import jesusernesto.lopezibarra.gestorgastos.ui.theme.Background
import jesusernesto.lopezibarra.gestorgastos.ui.theme.DarkNavy
import jesusernesto.lopezibarra.gestorgastos.ui.theme.White
import jesusernesto.lopezibarra.gestorgastos.ui.theme.Purple
import jesusernesto.lopezibarra.gestorgastos.ui.theme.PurpleLight
import jesusernesto.lopezibarra.gestorgastos.ui.theme.TextGray


@Composable
fun CrearGrupoScreen(
    onBack: () -> Unit = {},
    onCrear: (title: String, type: String) -> Unit = { _, _ -> },
    onCancelar: () -> Unit = {},
) {
    var titulo by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("Familiar") }

    Scaffold(
        containerColor = Background,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White)
                    .padding(horizontal = 4.dp, vertical = 12.dp),
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Regresar",
                        tint = Purple,
                    )
                }
                Text(
                    text = "Crear Grupo",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = DarkNavy,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(24.dp))

            GroupAvatar()
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Editar Foto",
                color = Purple,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { /* abrir selector de imagen */ },
            )

            Spacer(Modifier.height(28.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "TÍTULO",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextGray,
                    letterSpacing = 1.sp,
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = TextGray,
                        focusedBorderColor = Purple,
                        unfocusedContainerColor = White,
                        focusedContainerColor = White,
                    ),
                )
            }

            Spacer(Modifier.height(28.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "TIPO DE GRUPO",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextGray,
                    letterSpacing = 1.sp,
                )
                Spacer(Modifier.height(12.dp))
                GroupTypeGrid(
                    types = groupTypes,
                    selected = selectedType,
                    onSelect = { selectedType = it },
                )
            }

            Spacer(Modifier.weight(1f))
            Spacer(Modifier.height(32.dp))

            Button(
                onClick = { onCrear(titulo, selectedType) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Purple),
            ) {
                Text(
                    text = "Crear Grupo",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = White,
                )
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = onCancelar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkNavy),
            ) {
                Text(
                    text = "Cancelar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = White,
                )
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

// Avatar del grupo
@Composable
private fun GroupAvatar() {
    Box(
        modifier = Modifier
            .size(90.dp)
            .clip(CircleShape)
            .background(PurpleLight),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "👨‍👩‍👧‍👦", fontSize = 40.sp)
    }
}

@Composable
private fun GroupTypeGrid(
    types: List<GroupType>,
    selected: String,
    onSelect: (String) -> Unit,
) {
    val rows = types.chunked(3)
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                row.forEach { type ->
                    GroupTypeChip(
                        groupType = type,
                        isSelected = type.label == selected,
                        onClick = { onSelect(type.label) },
                        modifier = Modifier.weight(1f),
                    )
                }
                // Si la fila tiene menos de 3 elementos, rellenar
                repeat(3 - row.size) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun GroupTypeChip(
    groupType: GroupType,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .height(52.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = if (isSelected) Purple else White,
        border = BorderStroke(
            width = 1.5.dp,
            color = if (isSelected) Purple else TextGray,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(text = groupType.emoji, fontSize = 18.sp)
            Spacer(Modifier.width(6.dp))
            Text(
                text = groupType.label,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isSelected) White else DarkNavy,
                maxLines = 1,
            )
        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CrearGrupoScreenPreview() {
    MaterialTheme {
        CrearGrupoScreen()
    }
}
