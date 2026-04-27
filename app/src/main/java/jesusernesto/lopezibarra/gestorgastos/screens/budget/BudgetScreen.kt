package jesusernesto.lopezibarra.gestorgastos.screens.budget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.TrendingDown
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jesusernesto.lopezibarra.gestorgastos.dummy.DummyData
import jesusernesto.lopezibarra.gestorgastos.screens.components.AppTopBar
import jesusernesto.lopezibarra.gestorgastos.screens.graphs.GraphicScreen
import jesusernesto.lopezibarra.gestorgastos.ui.theme.DarkNavy
import jesusernesto.lopezibarra.gestorgastos.ui.theme.GreenIncome
import jesusernesto.lopezibarra.gestorgastos.ui.theme.Purple
import jesusernesto.lopezibarra.gestorgastos.ui.theme.PurpleLight
import jesusernesto.lopezibarra.gestorgastos.ui.theme.RedExpense
import jesusernesto.lopezibarra.gestorgastos.ui.theme.TextGray

@Composable
fun BudgetScreen(onBack: () -> Unit = {}){
    var estaEditando by remember { mutableStateOf(false) }
    var mostrarEnPesos by remember { mutableStateOf(true) }
    var verGraficas by remember { mutableStateOf(false) }

    if (estaEditando){
        EditBudgetScreen(onBack = {estaEditando = false})
        return
    }

    if (verGraficas) {
        GraphicScreen(onBack = { verGraficas = false })
        return
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        AppTopBar(title = "Mi presupuesto", onBack = onBack)

        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.TrendingUp, contentDescription = null, tint = GreenIncome, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Ingreso Mensual", fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.weight(1f))
                Text(text = "Gráficas", color = Purple, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable { verGraficas = true }.padding(end = 8.dp))
                Text(text = "Editar", color = Purple, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable{estaEditando = true})
            }
            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.surface).border(1.5.dp, PurpleLight, RoundedCornerShape(12.dp)).padding(horizontal = 16.dp, vertical = 14.dp)){

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Ingreso fijo", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                    Text(text = "$${"%,.0f".format(DummyData.ingreso)}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Purple)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.TrendingDown, contentDescription = null, tint = RedExpense, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(8.dp))

                Text(text = "Gastos fijos", fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = MaterialTheme.colorScheme.onBackground)
            }
            Spacer(modifier = Modifier.height(8.dp))

            DummyData.gastos.forEach { (nombre, monto) ->
                val partes = nombre.split("(",")")
                val titulo = partes[0].trim()
                val categoria = if (partes.size > 1) partes[1].trim() else ""

                Box(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.surface).border(1.5.dp, PurpleLight, RoundedCornerShape(12.dp)).padding(horizontal = 16.dp, vertical = 14.dp)){
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row {
                            Text(text = titulo, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                            if (categoria.isNotEmpty()){
                                Text(text = " ($categoria)", fontSize = 13.sp, color = TextGray)
                            }
                        }
                        Text(text = "$${"%.0f".format(monto)}", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Distribucion por categoria", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.weight(1f))
                Box(modifier = Modifier.clip(RoundedCornerShape(20.dp)).border(1.5.dp, PurpleLight, RoundedCornerShape(20.dp))){
                    Row {
                        Box(modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(if (mostrarEnPesos) Purple else Color.Transparent).clickable{mostrarEnPesos = true}.padding(horizontal = 10.dp, vertical = 6.dp), contentAlignment = Alignment.Center) {
                            Icon(Icons.Outlined.AttachMoney, contentDescription = null, tint = if (mostrarEnPesos) Color.White else TextGray, modifier = Modifier.size(16.dp))
                        }
                        Box(modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(if (!mostrarEnPesos) Purple else Color.Transparent).clickable{mostrarEnPesos = false}.padding(horizontal = 10.dp, vertical = 6.dp), contentAlignment = Alignment.Center){
                            Text(text = "%", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if (!mostrarEnPesos) Color.White else TextGray)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            DummyData.listapresupuestos.forEach { presupuesto ->
                val excedido = presupuesto.restante < 0
                val  porcentaje = presupuesto.porcentaje.coerceIn(0, 100)

                Box(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.surface).border(1.5.dp, PurpleLight, RoundedCornerShape(12.dp)).padding(14.dp)){
                    Column {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text(text = presupuesto.category, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                                Text(text = if (mostrarEnPesos) "Restan: $${"%,.0f".format(presupuesto.restante)}" else "${100 - porcentaje}% Disponible", fontSize = 12.sp, color = if (excedido) RedExpense else TextGray)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = if (mostrarEnPesos) "$${"%,.0f".format(presupuesto.assigned)}" else "$porcentaje%", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = if (excedido) RedExpense else Purple)
                                Text(text = if (mostrarEnPesos) "MONTO TOTAL" else "USO ACTUAL", fontSize = 10.sp, color = TextGray)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        Box(modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(5.dp)).background(PurpleLight)) {
                            Box(modifier = Modifier.fillMaxWidth(porcentaje / 100f).fillMaxHeight().clip(RoundedCornerShape(5.dp)).background(if (excedido) RedExpense else Purple))
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BudgetScreenPreview() {
    MaterialTheme {
        BudgetScreen()
    }
}
