package jesusernesto.lopezibarra.gestorgastos.screens.budget

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import jesusernesto.lopezibarra.gestorgastos.data.entity.CategoriaEntity
import jesusernesto.lopezibarra.gestorgastos.data.entity.DetallePresupuestoEntity
import jesusernesto.lopezibarra.gestorgastos.data.entity.GastoFijoEntity
import jesusernesto.lopezibarra.gestorgastos.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBudgetScreen(
    onBack: () -> Unit = {},
    viewModel: BudgetViewModel = viewModel()
) {
    val presupuestoActual by viewModel.presupuestoActual.collectAsState()
    val categorias by viewModel.categorias.collectAsState()
    val gastosFijosDB by viewModel.gastosFijos.collectAsState()

    var montoIngreso by remember { mutableStateOf("") }
    val listaGastosFijos = remember { mutableStateListOf<GastoFijoState>() }
    val listaDetalles = remember { mutableStateListOf<CategoriaPresupuestoState>() }

    var cargadoInitialData by remember { mutableStateOf(false) }

    // Inicializar datos cuando el presupuesto o categorías carguen
    LaunchedEffect(presupuestoActual, categorias, gastosFijosDB) {
        if (!cargadoInitialData && categorias.isNotEmpty()) {
            montoIngreso = presupuestoActual?.presupuesto?.ingresoMensual?.toString() ?: ""
            
            listaGastosFijos.clear()
            if (gastosFijosDB.isNotEmpty()) {
                gastosFijosDB.forEach {
                    listaGastosFijos.add(GastoFijoState(it.nombre, it.monto.toString(), it.idCategoria))
                }
            }

            listaDetalles.clear()
            categorias.forEach { cat ->
                val detalleExistente = presupuestoActual?.detalles?.find { it.idCategoria == cat.idCategoria }
                listaDetalles.add(
                    CategoriaPresupuestoState(
                        idCategoria = cat.idCategoria,
                        nombre = cat.nombre,
                        monto = detalleExistente?.montoLimite?.toString() ?: "0"
                    )
                )
            }
            cargadoInitialData = true
        }
    }

    val totalIngresoDouble = montoIngreso.toDoubleOrNull() ?: 0.0
    val totalGastosFijos = listaGastosFijos.sumOf { it.monto.toDoubleOrNull() ?: 0.0 }
    val disponibleTrasGastosFijos = totalIngresoDouble - totalGastosFijos
    val totalAsignadoCategorias = listaDetalles.sumOf { it.monto.toDoubleOrNull() ?: 0.0 }
    val restanteSinAsignar = disponibleTrasGastosFijos - totalAsignadoCategorias

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Configurar Presupuesto", fontWeight = FontWeight.Bold, color = Purple) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar", tint = Purple)
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    val fixedEntities = listaGastosFijos.map { 
                        GastoFijoEntity(
                            nombre = it.nombre, 
                            monto = it.monto.toDoubleOrNull() ?: 0.0, 
                            idCategoria = it.idCategoria,
                            idPresupuesto = 0
                        ) 
                    }
                    val detailEntities = listaDetalles.map { 
                        DetallePresupuestoEntity(
                            idCategoria = it.idCategoria, 
                            montoLimite = it.monto.toDoubleOrNull() ?: 0.0, 
                            idPresupuesto = 0
                        ) 
                    }
                    viewModel.guardarPresupuesto(totalIngresoDouble, fixedEntities, detailEntities)
                    onBack()
                },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Purple),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Guardar Presupuesto", fontSize = 16.sp, modifier = Modifier.padding(8.dp))
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("INGRESO TOTAL MENSUAL", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextGray)
                OutlinedTextField(
                    value = montoIngreso,
                    onValueChange = { montoIngreso = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("0.00") },
                    prefix = { Text("$", color = Purple, fontWeight = FontWeight.Bold) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Purple, unfocusedBorderColor = PurpleLight)
                )
            }

            item {
                Text("GASTOS FIJOS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextGray)
            }

            itemsIndexed(listaGastosFijos) { index, gasto ->
                var menuAbierto by remember { mutableStateOf(false) }
                val categoriaSeleccionada = categorias.find { it.idCategoria == gasto.idCategoria }

                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = gasto.nombre,
                            onValueChange = { listaGastosFijos[index] = gasto.copy(nombre = it) },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Ej. Renta") },
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = gasto.monto,
                            onValueChange = { listaGastosFijos[index] = gasto.copy(monto = it) },
                            modifier = Modifier.width(100.dp),
                            prefix = { Text("$") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp)
                        )
                        IconButton(onClick = { listaGastosFijos.removeAt(index) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = RedExpense)
                        }
                    }
                    
                    // Selector de categoría para gasto fijo
                    Box(modifier = Modifier.padding(top = 4.dp).padding(start = 4.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(PurpleLight.copy(alpha = 0.2f))
                                .clickable { menuAbierto = true }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Outlined.Category, contentDescription = null, tint = Purple, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = categoriaSeleccionada?.nombre ?: "Sin categoría",
                                fontSize = 12.sp,
                                color = if (categoriaSeleccionada != null) Purple else TextGray
                            )
                        }

                        DropdownMenu(expanded = menuAbierto, onDismissRequest = { menuAbierto = false }) {
                            DropdownMenuItem(
                                text = { Text("Sin categoría") },
                                onClick = {
                                    listaGastosFijos[index] = gasto.copy(idCategoria = null)
                                    menuAbierto = false
                                }
                            )
                            categorias.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat.nombre) },
                                    onClick = {
                                        listaGastosFijos[index] = gasto.copy(idCategoria = cat.idCategoria)
                                        menuAbierto = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            item {
                DashedAddButton("Añadir gasto fijo") {
                    listaGastosFijos.add(GastoFijoState("", ""))
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = PurpleLight.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Disponible para categorías:", fontSize = 14.sp)
                            Text("$${"%,.2f".format(disponibleTrasGastosFijos)}", fontWeight = FontWeight.Bold, color = Purple)
                        }
                    }
                }
            }

            item {
                Text("PRESUPUESTO POR CATEGORÍA", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextGray)
            }

            if (categorias.isEmpty()) {
                item {
                    Text(
                        "No hay categorías disponibles. Crea categorías o reinicia la app.",
                        color = RedExpense,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

            itemsIndexed(listaDetalles) { index, detalle ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(detalle.nombre, modifier = Modifier.weight(1f), fontSize = 15.sp, fontWeight = FontWeight.Medium)
                    OutlinedTextField(
                        value = detalle.monto,
                        onValueChange = { listaDetalles[index] = detalle.copy(monto = it) },
                        modifier = Modifier.width(140.dp),
                        prefix = { Text("$") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            item {
                val colorRestante = if (restanteSinAsignar < 0) RedExpense else GreenIncome
                Text(
                    text = if (restanteSinAsignar >= 0) "Restante sin asignar: $${"%,.2f".format(restanteSinAsignar)}" 
                           else "Excedido por: $${"%,.2f".format(Math.abs(restanteSinAsignar))}",
                    color = colorRestante,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun DashedAddButton(label: String, onClick: () -> Unit) {
    val stroke = Stroke(width = 3f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f))
    Box(
        modifier = Modifier.fillMaxWidth().height(50.dp).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRoundRect(color = Color.LightGray, style = stroke, cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx()))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Add, contentDescription = null, tint = TextGray, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = label, color = TextGray, fontSize = 14.sp)
        }
    }
}

data class GastoFijoState(val nombre: String, val monto: String, val idCategoria: Int? = null)
data class CategoriaPresupuestoState(val idCategoria: Int, val nombre: String, val monto: String)

@Preview
@Composable
fun EditBudgetScreenPreview() {
    MaterialTheme {
        EditBudgetScreen()
    }
}
