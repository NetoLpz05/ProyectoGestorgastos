package jesusernesto.lopezibarra.gestorgastos.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import jesusernesto.lopezibarra.gestorgastos.dummy.DummyData
import jesusernesto.lopezibarra.gestorgastos.dummy.Transaccion
import jesusernesto.lopezibarra.gestorgastos.screens.budget.BudgetScreen
import jesusernesto.lopezibarra.gestorgastos.screens.components.BottomNavBar
import jesusernesto.lopezibarra.gestorgastos.screens.group.MisGruposScreen
import jesusernesto.lopezibarra.gestorgastos.screens.income_expenses.NewMovementScreen
import jesusernesto.lopezibarra.gestorgastos.screens.user.ProfileScreen
import jesusernesto.lopezibarra.gestorgastos.ui.theme.BackgroundLight
import jesusernesto.lopezibarra.gestorgastos.ui.theme.DarkNavy
import jesusernesto.lopezibarra.gestorgastos.ui.theme.GreenIncome
import jesusernesto.lopezibarra.gestorgastos.ui.theme.Purple
import jesusernesto.lopezibarra.gestorgastos.ui.theme.PurpleLight
import jesusernesto.lopezibarra.gestorgastos.ui.theme.RedExpense
import jesusernesto.lopezibarra.gestorgastos.ui.theme.TextGray

@Composable
fun MainScreen(onLogout: () -> Unit = {}) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "Inicio"

    Scaffold(
        containerColor = BackgroundLight,
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
                    onAbrirGrupo = { /* TODO */ },
                    onCrearGrupo = { /* TODO */ }
                )
            }
            composable("Perfil") {
                ProfileScreen(
                    onBack = { navController.popBackStack() },
                    onLogout = onLogout
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

@Composable
fun HomeScreen() {
    val categoriasFiltro = listOf("Todos", "Hogar", "Comida", "Transporte", "Compras")
    var categoriaSeleccionada by remember { mutableStateOf("Todos") }
    var busqueda by remember { mutableStateOf("") }

    val transaccionesFiltradas = DummyData.transacciones.filter { t ->
        val matchCategoria = categoriaSeleccionada == "Todos" || t.category.equals(categoriaSeleccionada, ignoreCase = true)
        val matchBusqueda = busqueda.isBlank() || t.title.contains(busqueda, ignoreCase = true)
        matchCategoria && matchBusqueda
    }

    Column(modifier = Modifier.fillMaxSize().background(BackgroundLight)) {
        Row(modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(44.dp).clip(CircleShape).background(PurpleLight), contentAlignment = Alignment.Center){
                Image(painter = painterResource(id = DummyData.userActual.pfp), contentDescription = "Foto de perfil", modifier = Modifier.fillMaxSize().clip(CircleShape))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = DummyData.mesActual, fontSize = 12.sp, color = TextGray)
                Text(text = "Mis finanzas", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkNavy)
            }
        }
        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            Box(modifier = Modifier.padding(16.dp).fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Purple).padding(20.dp)){
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                    Column {
                        Text(text = "Balance del mes", fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
                        Text(text = "$${"%,.0f".format(DummyData.balanceMensual)}", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Ingresos", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                        Text(text = "$${"%,.0f".format(DummyData.ingresoMensual)}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Gastos", fontSize = 12.sp, color = Color(0xFFFF7675))
                        Text(text = "$${"%,.0f".format(DummyData.gastoMensual)}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF7675))
                    }
                }
            }
            Row(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                FechaFiltro(fecha = "01 Mar 2026", modifier = Modifier.weight(1f))
                Icon(Icons.Outlined.ArrowForward, contentDescription = null, tint = TextGray, modifier = Modifier.size(16.dp))
                FechaFiltro(fecha = "30 Mar 2026", modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = busqueda,
                onValueChange = {busqueda = it},
                modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                placeholder = {
                    Text("Buscar movimientos...", color = TextGray.copy(alpha = 0.5f), fontSize = 14.sp)
                },
                leadingIcon = {Icon(Icons.Outlined.Search, contentDescription = null, tint = TextGray)},
                colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = PurpleLight, focusedBorderColor = Purple, unfocusedContainerColor = Color.White, focusedContainerColor = Color.White),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                categoriasFiltro.forEach { cat ->
                    val isSelected = cat == categoriaSeleccionada
                    Box(modifier = Modifier.clip(RoundedCornerShape(20.dp)).
                    background(if (isSelected) DarkNavy else Color.White).border(1.dp, if (isSelected) DarkNavy else PurpleLight, RoundedCornerShape(20.dp)).
                    clickable{categoriaSeleccionada = cat}.padding(horizontal = 14.dp, vertical = 8.dp)){
                        Text(text = cat, fontSize = 13.sp, fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal, color = if (isSelected) Color.White else TextGray)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Ultimos movimientos", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkNavy, modifier = Modifier.padding(horizontal = 19.dp))
            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Color.White)){
                Column {
                    if (transaccionesFiltradas.isEmpty()){
                        Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center){
                            Text("Sin movimientos", color = TextGray, fontSize = 14.sp)
                        }
                    } else{
                        transaccionesFiltradas.forEachIndexed { index, t ->
                            TransaccionRow(transaccion = t)
                            if (index < transaccionesFiltradas.lastIndex){
                                Divider(color = PurpleLight, modifier = Modifier.padding(horizontal = 16.dp))
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Gastos por categoria", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkNavy, modifier = Modifier.padding(horizontal = 16.dp))

            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Color.White)){
                Column {
                    DummyData.gastosPorCategoria.forEachIndexed { index, gasto ->
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = gasto.iconEmoji, fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = gasto.name,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = DarkNavy,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "$${"%,.2f".format(gasto.amount)}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = DarkNavy
                            )
                        }
                        if (index < DummyData.gastosPorCategoria.lastIndex) {
                            Divider( color = PurpleLight, modifier = Modifier.padding(horizontal = 16.dp))
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun FechaFiltro(fecha: String, modifier: Modifier = Modifier){
    Row(
        modifier = Modifier.
        clip(RoundedCornerShape(20.dp)).
        border(1.5.dp, PurpleLight, RoundedCornerShape(10.dp)).
        background(Color.White).
        padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Icon(Icons.Outlined.CalendarMonth, contentDescription = null, tint = DarkNavy, modifier = Modifier.size(16.dp))

        Text(text = fecha, fontSize = 13.sp, color = DarkNavy, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun TransaccionRow(transaccion: Transaccion){
    val isIngreso = transaccion.amount > 0
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(42.dp).clip(RoundedCornerShape(12.dp)).background(PurpleLight), contentAlignment = Alignment.Center) {
            Text(text = transaccion.iconEmoji, fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = transaccion.title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = DarkNavy)
            Text(text = "${transaccion.category} - ${transaccion.date}", fontSize = 12.sp, color = TextGray)
        }
        Text(text = "${if (isIngreso) "+ " else "- "}$${"%,.2f".format(Math.abs(transaccion.amount))}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (isIngreso) GreenIncome else RedExpense)

        Spacer(modifier = Modifier.width(6.dp))
        Icon(Icons.Outlined.MoreVert, contentDescription = null, tint = TextGray, modifier = Modifier.size(18.dp))
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview(){
    MaterialTheme{
        MainScreen()
    }
}
