package jesusernesto.lopezibarra.gestorgastos.screens.graphs

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jesusernesto.lopezibarra.gestorgastos.dummy.DummyData
import jesusernesto.lopezibarra.gestorgastos.dummy.Presupuesto
import jesusernesto.lopezibarra.gestorgastos.ui.theme.DarkNavy
import jesusernesto.lopezibarra.gestorgastos.ui.theme.Purple
import jesusernesto.lopezibarra.gestorgastos.ui.theme.PurpleLight
import jesusernesto.lopezibarra.gestorgastos.ui.theme.TextGray





@Composable
fun GraphicScreen(
    onBack: () -> Unit = {}
){
    val periodos = listOf("Dia", "Semanal", "Quincenal")
    var periodosSeleccionados by remember { mutableStateOf("Dia") }
    val fechaSeleccionada = "24 Mar 2026"

    Column (modifier = Modifier.fillMaxSize().background(Color.White)){
        AppTopBar(title = "Graficas", onBack = onBack)

        Column (modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)){
            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "Periodo", fontSize = 13.sp, color = TextGray, fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 8.dp))

            PeriodoSelector(periodos = periodos, seleccionado = periodosSeleccionados, onSeleccionar = {periodosSeleccionados = it})

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Seleccionar fecha", fontSize = 13.sp, color = TextGray, fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 8.dp))

            FechaSelector(fecha = fechaSeleccionada)

            Spacer(modifier = Modifier.height(20.dp))

            DummyData.listapresupuestos.forEach { presupuesto -> val emoji = DummyData.categorias.find { it.second.equals(presupuesto.category, ignoreCase = true) }
                ?.first ?: "\uD83D\uDCB0"

                PresupuestoCard(presupuesto = presupuesto, emoji = emoji)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Categorias", fontSize = 14.sp, color = TextGray, fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 10.dp))

            CategoriasGrid(categorias = DummyData.categorias)

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}


@Composable
fun PeriodoSelector(periodos: List<String>, seleccionado: String, onSeleccionar: (String) -> Unit){
    Row (modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).border(1.5.dp,
        PurpleLight, RoundedCornerShape(12.dp)
    )){
        periodos.forEach { periodo ->
            val isSelected = periodo == seleccionado
            Box(modifier = Modifier.weight(1f).clip(RoundedCornerShape(12.dp)).background(if (isSelected) PurpleLight else Color.White).clickable{onSeleccionar(periodo)}.padding(vertical = 12.dp), contentAlignment = Alignment.Center){
                Text(
                    text = periodo,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isSelected) Color.White else DarkNavy
                )
            }
        }
    }
}

@Composable
fun FechaSelector(fecha: String){
    Row (modifier = Modifier.clip(RoundedCornerShape(10.dp)).border(1.5.dp, PurpleLight, RoundedCornerShape(10.dp)).padding(horizontal = 14.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)){
        Icon(imageVector = Icons.Outlined.CalendarMonth,
            contentDescription = null,
            tint = DarkNavy,
            modifier = Modifier.size(18.dp)
        )

        Text(
            text = fecha,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = DarkNavy
        )
    }
}

@Composable
fun PresupuestoCard(presupuesto: Presupuesto, emoji: String){
    Column (modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(14.dp)).border(1.5.dp, PurpleLight, RoundedCornerShape(14.dp)).padding(14.dp)){
        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
            Text(
                text = "Presupuesto",
                fontSize = 13.sp,
                color = TextGray,
                fontWeight = FontWeight.Medium
            )

            Text(
                text ="$${"%,.0f".format(presupuesto.assigned)}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Purple
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row (verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)){
            Text(text = emoji, fontSize = 18.sp)
            Text(
                text = presupuesto.category,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = TextGray
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        BarraProgreso(porcentajeReal = presupuesto.porcentaje, porcentajeRestante = 100 - presupuesto.porcentaje)

        Spacer(modifier = Modifier.height(6.dp))

        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
            Column {
                Text(text = "Gasto real", fontSize = 11.sp, color = TextGray)
                Text(
                    text = "$${"%,.0f".format(presupuesto.spent)}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkNavy
                )
            }
            Column (horizontalAlignment = Alignment.End){
                Text(text = "Estimado", fontSize = 11.sp, color = TextGray)
                Text(
                    text = "$${"%,.0f".format(presupuesto.restante)}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkNavy
                )
            }
        }
    }
}

@Composable
fun BarraProgreso(porcentajeReal: Int, porcentajeRestante: Int){
    Row (modifier = Modifier.fillMaxWidth()){
        if (porcentajeReal > 0){
            Box(
                modifier = Modifier.weight(porcentajeReal.toFloat()).clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp,
                    topEnd = if (porcentajeRestante == 0) 8.dp else 0.dp, bottomEnd = if (porcentajeRestante == 0) 8.dp else 0.dp)
            ).background(Purple).padding(vertical = 6.dp, horizontal = 8.dp), contentAlignment = Alignment.CenterStart){
                Text(
                    text = "$porcentajeReal%",
                    fontSize = 11.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        if (porcentajeRestante > 0){
            Box(modifier = Modifier.weight(porcentajeRestante.toFloat()).clip(RoundedCornerShape(topStart = if (porcentajeReal == 0) 8.dp else 0.dp, bottomStart = if (porcentajeReal == 0) 8.dp else 0.dp, topEnd = 8.dp, bottomEnd = 8.dp)
            ).background(PurpleLight).padding(vertical = 6.dp, horizontal = 8.dp), contentAlignment = Alignment.CenterEnd){
                Text(
                    text = "$porcentajeRestante%",
                    fontSize = 11.sp,
                    color = TextGray,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun CategoriasGrid(categorias: List<Pair<String, String>>){
    val filas = categorias.chunked(2)
    Column (verticalArrangement = Arrangement.spacedBy(10.dp)){
        filas.forEach { fila ->
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)){
                fila.forEach { (emoji, nombre) ->
                    CategoriaChip(emoji = emoji, nombre = nombre, isDestacada = DummyData.categorias.indexOf(emoji to nombre) < 2, modifier = Modifier.weight(1f))
                }
                if (fila.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun CategoriaChip(emoji: String, nombre: String, isDestacada: Boolean, modifier: Modifier = Modifier){
    Row (modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(if (isDestacada) Purple else Color.White).border(width = if (isDestacada) 0.dp else 1.5.dp, color = if (isDestacada) Color.Transparent else PurpleLight, shape = RoundedCornerShape(12.dp)
    ).padding(vertical = 14.dp, horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
        Text(
            text = nombre,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isDestacada) Color.White else DarkNavy
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(title: String, onBack: () -> Unit) {
    TopAppBar(
        title = { Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkNavy) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Regresar",
                    tint = DarkNavy
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GraphicScreenPreview(){
    MaterialTheme {
        GraphicScreen (onBack = {})
    }
}

