package jesusernesto.lopezibarra.gestorgastos.screens.budget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.TrendingDown
import androidx.compose.material.icons.outlined.TrendingUp
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
import androidx.lifecycle.viewmodel.compose.viewModel
import jesusernesto.lopezibarra.gestorgastos.data.SessionManager
import jesusernesto.lopezibarra.gestorgastos.screens.components.AppTopBar
import jesusernesto.lopezibarra.gestorgastos.screens.graphs.GraphicScreen
import jesusernesto.lopezibarra.gestorgastos.ui.theme.*

@Composable
fun BudgetScreen(
    onBack: () -> Unit = {},
    viewModel: BudgetViewModel = viewModel()
) {
    var estaEditando by remember { mutableStateOf(false) }
    var mostrarEnPesos by remember { mutableStateOf(true) }
    var verGraficas by remember { mutableStateOf(false) }

    val presupuestoCompleto by viewModel.presupuestoActual.collectAsState()
    val categorias by viewModel.categorias.collectAsState()
    val gastosReales by viewModel.gastosPorCategoria.collectAsState()

    // Cargar gastos fijos si hay presupuesto
    LaunchedEffect(presupuestoCompleto) {
        presupuestoCompleto?.presupuesto?.idPresupuesto?.let {
            viewModel.cargarGastosFijos(it)
        }
    }
    val gastosFijos by viewModel.gastosFijos.collectAsState()

    if (estaEditando) {
        EditBudgetScreen(
            onBack = { estaEditando = false },
            viewModel = viewModel
        )
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
                Text(text = "Editar", color = Purple, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable { estaEditando = true })
            }
            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.surface).border(1.5.dp, PurpleLight, RoundedCornerShape(12.dp)).padding(horizontal = 16.dp, vertical = 14.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Ingreso total", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                    val montoIngreso = presupuestoCompleto?.presupuesto?.ingresoMensual ?: 0.0
                    Text(text = "$${"%,.0f".format(montoIngreso)}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Purple)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.TrendingDown, contentDescription = null, tint = RedExpense, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Gastos fijos", fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = MaterialTheme.colorScheme.onBackground)
            }
            Spacer(modifier = Modifier.height(8.dp))

            if (gastosFijos.isEmpty()) {
                Text("No hay gastos fijos configurados", fontSize = 13.sp, color = TextGray, modifier = Modifier.padding(vertical = 8.dp))
            } else {
                gastosFijos.forEach { gasto ->
                    Box(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.surface).border(1.5.dp, PurpleLight, RoundedCornerShape(12.dp)).padding(horizontal = 16.dp, vertical = 14.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(text = gasto.nombre, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                            Text(text = "$${"%,.0f".format(gasto.monto)}", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Distribución por categoría", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.weight(1f))
                Box(modifier = Modifier.clip(RoundedCornerShape(20.dp)).border(1.5.dp, PurpleLight, RoundedCornerShape(20.dp))) {
                    Row {
                        Box(modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(if (mostrarEnPesos) Purple else Color.Transparent).clickable { mostrarEnPesos = true }.padding(horizontal = 10.dp, vertical = 6.dp), contentAlignment = Alignment.Center) {
                            Icon(Icons.Outlined.AttachMoney, contentDescription = null, tint = if (mostrarEnPesos) Color.White else TextGray, modifier = Modifier.size(16.dp))
                        }
                        Box(modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(if (!mostrarEnPesos) Purple else Color.Transparent).clickable { mostrarEnPesos = false }.padding(horizontal = 10.dp, vertical = 6.dp), contentAlignment = Alignment.Center) {
                            Text(text = "%", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if (!mostrarEnPesos) Color.White else TextGray)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            val detalles = presupuestoCompleto?.detalles ?: emptyList()
            if (detalles.isEmpty()) {
                Text("Crea un presupuesto para ver el desglose por categorías", fontSize = 13.sp, color = TextGray, modifier = Modifier.padding(vertical = 8.dp))
            } else {
                detalles.forEach { detalle ->
                    val categoria = categorias.find { it.idCategoria == detalle.idCategoria }
                    val nombreCat = categoria?.nombre ?: "Categoría"
                    val gastado = gastosReales[detalle.idCategoria] ?: 0.0
                    val restante = detalle.montoLimite - gastado
                    val porcentajeUso = if (detalle.montoLimite > 0) (gastado / detalle.montoLimite * 100).toInt() else 0
                    val excedido = restante < 0

                    Box(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.surface).border(1.5.dp, PurpleLight, RoundedCornerShape(12.dp)).padding(14.dp)) {
                        Column {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Column {
                                    Text(text = nombreCat, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                                    Text(
                                        text = if (mostrarEnPesos) "Restan: $${"%,.0f".format(restante)}" else "${(100 - porcentajeUso).coerceAtLeast(0)}% Disponible",
                                        fontSize = 12.sp,
                                        color = if (excedido) RedExpense else TextGray
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = if (mostrarEnPesos) "$${"%,.0f".format(detalle.montoLimite)}" else "$porcentajeUso%",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = if (excedido) RedExpense else Purple
                                    )
                                    Text(text = if (mostrarEnPesos) "PRESUPUESTO" else "USO ACTUAL", fontSize = 10.sp, color = TextGray)
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))

                            Box(modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(5.dp)).background(PurpleLight)) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth((porcentajeUso / 100f).coerceIn(0f, 1f))
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(5.dp))
                                        .background(if (excedido) RedExpense else Purple)
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
