package jesusernesto.lopezibarra.gestorgastos.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import jesusernesto.lopezibarra.gestorgastos.data.gruposEjemplo
import jesusernesto.lopezibarra.gestorgastos.dummy.DummyData
import jesusernesto.lopezibarra.gestorgastos.dummy.Transaccion
import jesusernesto.lopezibarra.gestorgastos.screens.budget.BudgetScreen
import jesusernesto.lopezibarra.gestorgastos.screens.components.BottomNavBar
import jesusernesto.lopezibarra.gestorgastos.screens.group.CrearGrupoScreen
import jesusernesto.lopezibarra.gestorgastos.screens.group.MisGruposScreen
import jesusernesto.lopezibarra.gestorgastos.screens.group.ViajesScreen
import jesusernesto.lopezibarra.gestorgastos.screens.income_expenses.NewMovementScreen
import jesusernesto.lopezibarra.gestorgastos.screens.user.ProfileScreen
import jesusernesto.lopezibarra.gestorgastos.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

private val displayFormat = SimpleDateFormat("dd MM yyyy", Locale("es", "MX"))

private fun Long.toDisplayDate(): String = displayFormat.format(Date(this))

private fun todayUtcMillis(): Long{
    val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}

private fun firstDayMonthUtcMillis(): Long{
    val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    cal.set(Calendar.DAY_OF_MONTH, 1)
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}


@Composable
fun MainScreen(
    onLogout: () -> Unit = {},
    isDarkMode: Boolean = false,
    onDarkModeChange: (Boolean) -> Unit = {}
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "Inicio"

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (currentRoute != "NuevoMovimiento") {
                BottomNavBar(
                    tabActivo = currentRoute,
                    onTabSelected = { route ->
                        if (route != currentRoute) {
                            navController.navigate(route) {
                                popUpTo("Inicio") { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    onNewMovement = {
                        navController.navigate("NuevoMovimiento")
                    }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "Inicio",
            modifier = Modifier.padding(padding)
        ) {
            composable("Inicio") {
                HomeScreen()
            }
            composable("Presupuesto") {
                BudgetScreen(onBack = { navController.popBackStack() })
            }
            composable("Grupos") {
                MisGruposScreen(
                    onAtras = { navController.popBackStack() },
                    onAbrirGrupo = { grupo ->
                        navController.navigate("GrupoDetalle/${grupo.id}")
                    },
                    onCrearGrupo = {
                        navController.navigate("CrearGrupo")
                    }
                )
            }
            composable("CrearGrupo") {
                CrearGrupoScreen(
                    onBack = { navController.popBackStack() },
                    onCrear = { titulo, tipo ->
                        navController.popBackStack()
                    },
                    onCancelar = { navController.popBackStack() }
                )
            }
            composable("GrupoDetalle/{grupoId}") { backStackEntry ->
                val grupoId = backStackEntry.arguments?.getString("grupoId")?.toIntOrNull()
                val grupo = gruposEjemplo.find { it.id == grupoId }
                if (grupo != null) {
                    ViajesScreen(
                        onAtras = { navController.popBackStack() }
                    )
                }
            }

            composable("Perfil") {
                ProfileScreen(
                    onBack = { navController.popBackStack() },
                    onLogout = onLogout,
                    isDarkMode = isDarkMode,
                    onDarkModeChange = onDarkModeChange
                )
            }
            composable("NuevoMovimiento") {
                NewMovementScreen(
                    onBack = { navController.popBackStack() },
                    onSave = { navController.popBackStack() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val categoriasFiltro = listOf("Todos", "Hogar", "Comida", "Transporte", "Compras")
    var categoriaSeleccionada by remember { mutableStateOf("Todos") }
    var busqueda by remember { mutableStateOf("") }
    var fechaDesde by remember { mutableStateOf(firstDayMonthUtcMillis()) }
    var fechaHasta by remember { mutableStateOf(todayUtcMillis()) }
    var pickerAbierto by remember { mutableStateOf<String?>(null) }

    val stateDesde = rememberDatePickerState(
        initialSelectedDateMillis = fechaDesde,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long) = utcTimeMillis <= fechaHasta
        })
    val stateHasta = rememberDatePickerState(
        initialSelectedDateMillis = fechaHasta,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long) = utcTimeMillis <= fechaDesde
        })

    val transaccionesFiltradas = DummyData.transacciones.filter { t ->
        val matchCategoria = categoriaSeleccionada == "Todos" || t.category.equals(
            categoriaSeleccionada,
            ignoreCase = true
        )
        val matchBusqueda = busqueda.isBlank() || t.title.contains(busqueda, ignoreCase = true)
        matchCategoria && matchBusqueda
    }

    if (pickerAbierto == "desde") {
        DatePickerDialog(
            onDismissRequest = { pickerAbierto = null }, confirmButton = {
            TextButton(onClick = {
                stateDesde.selectedDateMillis?.let { fechaDesde = it }
                pickerAbierto = null
            }) { Text("Aceptar", color = Purple) }
        },
            dismissButton = {
                TextButton(onClick = { pickerAbierto = null }) {
                    Text("Cancelar", color = TextGray)
                }
            }) {
            DatePicker(
                state = stateDesde,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = Purple,
                    todayDateBorderColor = Purple,
                    selectedYearContainerColor = Purple
                )
            )
        }
    }

    if (pickerAbierto == "hasta") {
        DatePickerDialog(
            onDismissRequest = { pickerAbierto = null }, confirmButton = {
            TextButton(onClick = {
                stateHasta.selectedDateMillis?.let { fechaHasta = it }
                pickerAbierto = null
            }) { Text("Aceptar", color = Purple) }
        },
            dismissButton = {
                TextButton(onClick = { pickerAbierto = null }) {
                    Text("Cancelar", color = TextGray)
                }
            }) {
            DatePicker(
                state = stateHasta,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = Purple,
                    todayDateBorderColor = Purple,
                    selectedYearContainerColor = Purple
                )
            )
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Row(
            modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(44.dp).clip(CircleShape).background(PurpleLight),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = DummyData.userActual.pfp),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier.fillMaxSize().clip(CircleShape)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = DummyData.mesActual, fontSize = 12.sp, color = TextGray)
                Text(
                    text = "Mis finanzas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)) {
            item {
                Box(modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Purple)
                        .padding(20.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top) {
                        Column {
                            Text(text = "Balance del mes",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.8f))
                            Text(text = "$${"%,.0f".format(DummyData.balanceMensual)}",
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Ingresos",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.8f))
                            Text(text = "$${"%,.0f".format(DummyData.ingresoMensual)}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Gastos", fontSize = 12.sp, color = Color(0xFFFF7675))
                            Text(text = "$${"%,.0f".format(DummyData.gastoMensual)}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF7675))
                        }
                    }
                }
            }
            item {
                Row(modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    FechaFiltro(label = "Desde",
                        fecha = fechaDesde.toDisplayDate(),
                        modifier = Modifier.weight(1f),
                        onClick = { pickerAbierto = "desde" })
                    Icon(Icons.Outlined.ArrowForward,
                        contentDescription = null,
                        tint = TextGray,
                        modifier = Modifier.size(16.dp))
                    FechaFiltro(label = "Hasta",
                        fecha = fechaHasta.toDisplayDate(),
                        modifier = Modifier.weight(1f),
                        onClick = { pickerAbierto = "hasta" })
                }
                Spacer(modifier = Modifier.height(10.dp))
            }


            item {
                OutlinedTextField(value = busqueda,
                    onValueChange = { busqueda = it },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    placeholder = {
                        Text("Buscar movimientos...",
                            color = TextGray.copy(alpha = 0.5f),
                            fontSize = 14.sp)
                    },
                    leadingIcon = {
                        Icon(Icons.Outlined.Search, contentDescription = null, tint = TextGray)
                    },
                    colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = PurpleLight,
                        focusedBorderColor = Purple,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface), singleLine = true)

                Spacer(modifier = Modifier.height(10.dp))
            }


            item {
                LazyRow(modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                    items(categoriasFiltro) { cat ->
                        val isSelected = cat == categoriaSeleccionada
                        Box(modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.onSurface
                                    else MaterialTheme.colorScheme.surface)
                                .border(
                                    1.dp,
                                    if (isSelected) MaterialTheme.colorScheme.onSurface else PurpleLight,
                                    RoundedCornerShape(20.dp)
                                ).clickable { categoriaSeleccionada = cat }
                                .padding(horizontal = 14.dp, vertical = 8.dp)) {

                            Text(text = cat,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.surface else TextGray)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Text(text = "Últimos movimientos",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 19.dp))

                Spacer(modifier = Modifier.height(8.dp))
            }

            if (transaccionesFiltradas.isEmpty()) {
                item {
                    Box(modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(24.dp),
                        contentAlignment = Alignment.Center) {

                        Text("Sin movimientos", color = TextGray, fontSize = 14.sp)
                    }
                }
            } else {
                item {
                    Box(modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .clip(
                                RoundedCornerShape(
                                    topStart = 14.dp,
                                    topEnd = 14.dp,
                                    bottomStart = 0.dp,
                                    bottomEnd = 0.dp)).background(MaterialTheme.colorScheme.surface)) {
                        TransaccionRow(transaccion = transaccionesFiltradas.first())
                    }
                }

                items(items = transaccionesFiltradas.drop(1).dropLast(
                        if (transaccionesFiltradas.size > 1) 1 else 0)) { t ->
                    Box(modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface)) {
                        Divider(color = PurpleLight, modifier = Modifier.padding(horizontal = 16.dp))
                        TransaccionRow(transaccion = t)
                    }
                }

                if (transaccionesFiltradas.size > 1) {
                    item {
                        Box(modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 0.dp,
                                        topEnd = 0.dp,
                                        bottomStart = 14.dp,
                                        bottomEnd = 14.dp))

                                .background(MaterialTheme.colorScheme.surface)) {

                            Divider(color = PurpleLight, modifier = Modifier.padding(horizontal = 16.dp))
                            TransaccionRow(transaccion = transaccionesFiltradas.last())
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Gastos por categoría",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 16.dp))

                Spacer(modifier = Modifier.height(8.dp))
            }

            items(items = DummyData.gastosPorCategoria,
                key = { it.name }) { gasto ->
                val index = DummyData.gastosPorCategoria.indexOf(gasto)
                val isFirst = index == 0
                val isLast = index == DummyData.gastosPorCategoria.lastIndex
                val shape = RoundedCornerShape(topStart = if (isFirst) 14.dp else 0.dp,
                    topEnd = if (isFirst) 14.dp else 0.dp,
                    bottomStart = if (isLast) 14.dp else 0.dp,
                    bottomEnd = if (isLast) 14.dp else 0.dp)

                Box(modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .clip(shape)
                        .background(MaterialTheme.colorScheme.surface)) {

                    if (!isFirst) {
                        Divider(color = PurpleLight,
                            modifier = Modifier.padding(horizontal = 16.dp))
                    }
                    Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically) {

                        Text(text = gasto.iconEmoji, fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(12.dp))

                        Text(text = gasto.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f))

                        Text(text = "$${"%,.2f".format(gasto.amount)}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }
    }
}


@Composable
private fun FechaFiltro(fecha: String, modifier: Modifier = Modifier){
    Row(
        modifier = modifier.
        clip(RoundedCornerShape(20.dp)).
        border(1.5.dp, PurpleLight, RoundedCornerShape(10.dp)).
        background(MaterialTheme.colorScheme.surface).
        padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Icon(Icons.Outlined.CalendarMonth, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(16.dp))

        Text(text = fecha, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun FechaFiltro(label: String,
    fecha: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}){
    Row(modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .border(1.5.dp, PurpleLight, RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Icon(Icons.Outlined.CalendarMonth,
            contentDescription = null,
            tint = Purple,
            modifier = Modifier.size(16.dp))
        Column {
            Text(text = label,
                fontSize = 9.sp,
                color = TextGray,
                fontWeight = FontWeight.Medium)
            Text(text = fecha,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun TransaccionRow(transaccion: Transaccion) {
    val isIngreso = transaccion.amount > 0
    Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(PurpleLight),
            contentAlignment = Alignment.Center) {
            Text(text = transaccion.iconEmoji, fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = transaccion.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface)
            Text(text = "${transaccion.category} - ${transaccion.date}",
                fontSize = 12.sp,
                color = TextGray)
        }
        Text(text = "${if (isIngreso) "+ " else "- "}$${"%,.2f".format(Math.abs(transaccion.amount))}",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = if (isIngreso) GreenIncome else RedExpense)
        Spacer(modifier = Modifier.width(6.dp))
        Icon(Icons.Outlined.MoreVert,
            contentDescription = null,
            tint = TextGray,
            modifier = Modifier.size(18.dp))
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview(){
    MaterialTheme{
        MainScreen()
    }
}
