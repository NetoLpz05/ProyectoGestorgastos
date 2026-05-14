package jesusernesto.lopezibarra.gestorgastos.screens.income_expenses

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jesusernesto.lopezibarra.gestorgastos.ui.theme.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import jesusernesto.lopezibarra.gestorgastos.data.entity.CategoriaEntity
import jesusernesto.lopezibarra.gestorgastos.data.entity.MetodoPagoEntity
import jesusernesto.lopezibarra.gestorgastos.data.enums.TipoMetodoPago
import jesusernesto.lopezibarra.gestorgastos.data.viewModel.MovimientoViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewMovementScreen(
    onBack: () -> Unit,
    onSave: () -> Unit,
    onNavigateToNewCard: () -> Unit,
    movimientoViewModel: MovimientoViewModel = viewModel(),
    metodoPagoViewModel: MetodoPagoViewModel = viewModel()
) {
    var isGasto by remember { mutableStateOf(true) }
    var amount by remember { mutableStateOf("0.00") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())) }
    var selectedCategory by remember { mutableStateOf(0) }
    var selectedPaymentIndex by remember { mutableStateOf(0) }
    var showDatePicker by remember { mutableStateOf(false) }
    var isEditingAmount by remember { mutableStateOf(false) }
    var showPaymentSheet by remember { mutableStateOf(false) }

    val metodosPago by metodoPagoViewModel.metodosPago.collectAsState()
    val selectedMetodo = metodosPago.getOrNull(selectedPaymentIndex)

    // Estados para ubicación y fotos
    var location by remember { mutableStateOf<String?>(null) }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var showLocationDialog by remember { mutableStateOf(false) }
    var locationInput by remember { mutableStateOf("") }

    val saveSuccess by movimientoViewModel.saveSuccess.collectAsState()
    val categorias by movimientoViewModel.categorias.collectAsState(initial = emptyList())

    LaunchedEffect(saveSuccess) {
        if (saveSuccess) {
            onSave()
            movimientoViewModel.resetSaveSuccess()
        }
    }

    val photoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        photoUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Surface(shadowElevation = 4.dp, color = MaterialTheme.colorScheme.surface) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Outlined.ArrowBack, contentDescription = "Regresar", tint = Purple)
                }
                Text(
                    text = "Nuevo Movimiento",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(48.dp))
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(46.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(2.dp, PurpleLight, RoundedCornerShape(10.dp))
            ) {
                Box(modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp))
                    .background(if (isGasto) MaterialTheme.colorScheme.surface else PurpleLight.copy(alpha = 0.2f)).clickable { isGasto = true }, contentAlignment = Alignment.Center) {
                    Text("Gasto", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = if (isGasto) RedGasto else TextGray)
                }
                Box(
                    modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp))
                        .background(if (!isGasto) MaterialTheme.colorScheme.surface else PurpleLight.copy(alpha = 0.2f)).clickable { isGasto = false }, contentAlignment = Alignment.Center
                ) {
                    Text("Ingreso", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = if (!isGasto) GreenIncome else TextGray)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = if (isGasto) "MONTO DEL GASTO" else "MONTO DEL INGRESO", fontWeight = FontWeight.Bold, fontSize = 12.sp,
                color = TextGray, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))

            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Text("$", fontWeight = FontWeight.Bold, fontSize = 40.sp)

                Spacer(modifier = Modifier.width(4.dp))

                if (isEditingAmount) {
                    OutlinedTextField(value = amount,
                        onValueChange = {
                            if (it.matches(Regex("^\\d*\\.?\\d{0,2}\$"))) {
                                amount = it
                            }
                        },
                        textStyle = LocalTextStyle.current.copy(fontSize = 40.sp, fontWeight = FontWeight.Bold),
                        singleLine = true, modifier = Modifier.width(140.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.Transparent, focusedBorderColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent, focusedContainerColor = Color.Transparent))
                } else {
                    Text(text = amount, fontWeight = FontWeight.Bold, fontSize = 40.sp,
                        modifier = Modifier.clickable {
                            isEditingAmount = true
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            SectionLabel("Descripción")
            OutlinedTextField(value = description, onValueChange = { description = it },
                modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(10.dp), singleLine = true, colors = movementFieldColors())

            Spacer(modifier = Modifier.height(12.dp))

            SectionLabel("Fecha")

            OutlinedTextField(value = date, onValueChange = {}, readOnly = true,
                modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().height(54.dp).clickable { showDatePicker = true },
                shape = RoundedCornerShape(10.dp), singleLine = true,
                leadingIcon = {
                    Icon(Icons.Outlined.CalendarMonth, contentDescription = null, tint = Purple)
                },
                colors = movementFieldColors())

            if (showDatePicker) {
                val datePickerState = rememberDatePickerState()

                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = { TextButton(onClick = {
                        val millis = datePickerState.selectedDateMillis
                        millis?.let {
                            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            date = sdf.format(Date(it))
                        }
                        showDatePicker = false
                    }) {
                        Text("OK")
                    }
                    },
                    dismissButton = { TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancelar")
                    }
                    }) {
                    DatePicker(state = datePickerState)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            SectionLabel("Categoría")

            CategoryGrid(
                categories = categorias,
                selectedIndex = selectedCategory,
                onItemClick = { selectedCategory = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "MÉTODO DE PAGO",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = TextGray,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
            
            selectedMetodo?.let { metodo ->
                val emoji = when(metodo.tipo) {
                    TipoMetodoPago.EFECTIVO -> "💵"
                    else -> "💳"
                }
                val label = metodo.nombre ?: (if (metodo.tipo == TipoMetodoPago.TARJETA_CREDITO) "Tarjeta de Crédito" else if (metodo.tipo == TipoMetodoPago.TARJETA_DEBITO) "Tarjeta de Débito" else "Efectivo")
                val detail = if (metodo.tipo == TipoMetodoPago.EFECTIVO) "DINERO FÍSICO" else "**** ${metodo.ultimosDigitos ?: "0000"}"

                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .height(57.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(GreenPayment)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = emoji, fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(label, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
                        Text(detail, fontWeight = FontWeight.Bold, fontSize = 10.sp, color = Color.White.copy(alpha = 0.5f))
                    }
                    Text(
                        text = "Cambiar >",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.clickable {
                            showPaymentSheet = true
                        }
                    )
                }
            } ?: run {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .height(57.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(PurpleLight.copy(alpha = 0.2f))
                        .clickable { showPaymentSheet = true },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Seleccionar método de pago", color = TextGray, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().height(47.dp)
                    .clip(RoundedCornerShape(10.dp)).border(2.dp, PurpleLight, RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surface).clickable { showLocationDialog = true }.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.LocationOn, contentDescription = null, tint = Purple, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = location ?: "Agregar ubicación...", fontWeight = FontWeight.Bold, fontSize = 12.sp,
                    color = if (location == null) TextGray else Purple, fontStyle = if (location == null) FontStyle.Italic else FontStyle.Normal)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().height(47.dp).clip(RoundedCornerShape(10.dp))
                    .border(2.dp, PurpleLight, RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surface).clickable { photoLauncher.launch("image/*") }.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center) {
                Icon(imageVector = if (photoUri == null) Icons.Outlined.CameraAlt else Icons.Outlined.CheckCircle,
                    contentDescription = null, tint = if (photoUri == null) TextGray else GreenIncome,
                    modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = if (photoUri == null) "Adjuntar foto del recibo" else "Foto adjuntada", fontWeight = FontWeight.Bold,
                    fontSize = 12.sp, color = if (photoUri == null) TextGray else GreenIncome)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val catId = categorias.getOrNull(selectedCategory)?.idCategoria ?: 0
                    val mpId = selectedMetodo?.idMetodoPago ?: 0
                    movimientoViewModel.guardarMovimiento(
                        isGasto = isGasto,
                        monto = amount.toFloatOrNull() ?: 0f,
                        descripcion = description,
                        fecha = date,
                        idCategoria = catId,
                        idMetodoPago = mpId,
                        ubicacion = location,
                        fotoUri = photoUri?.toString()
                    )
                },
                modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().height(46.dp),
                shape = RoundedCornerShape(10.dp), colors = ButtonDefaults.buttonColors(containerColor = Purple)) {
                Text("Guardar Movimiento", fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        if (showPaymentSheet) {
            ModalBottomSheet(
                onDismissRequest = { showPaymentSheet = false }
            ) {
                FormaPagoCard(
                    metodos = metodosPago,
                    selectedIndex = selectedPaymentIndex,
                    onSelect = { index ->
                        selectedPaymentIndex = index
                        showPaymentSheet = false
                    },
                    onAddCard = {
                        showPaymentSheet = false
                        onNavigateToNewCard()
                    }
                )
            }
        }

        if (showLocationDialog) {
            AlertDialog(onDismissRequest = { showLocationDialog = false },
                title = { Text("Agregar Ubicación") },
                text = {
                    OutlinedTextField(value = locationInput, onValueChange = { locationInput = it },
                        label = { Text("Lugar / Dirección") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                },
                confirmButton = {
                    TextButton(onClick = { location = locationInput
                        showLocationDialog = false
                    }) {
                        Text("Aceptar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLocationDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        color = TextGray,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
    )
}

@Composable
private fun CategoryItem(
    emoji: String,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick).width(50.dp)) {
        Box(modifier = Modifier.size(34.dp).clip(CircleShape).background(if (selected) Purple else PurpleLight.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center) {
            Text(emoji, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (selected) Purple else TextGray,
            textAlign = TextAlign.Center, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
fun CategoryGrid(
    categories: List<CategoriaEntity>,
    selectedIndex: Int,
    onItemClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxWidth().height(140.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(categories) { index, item ->
            val partes = item.nombre.split(" ", limit = 2)
            val emoji = partes.firstOrNull() ?: "📦"
            val texto = partes.getOrNull(1) ?: item.nombre

            CategoryItem(
                emoji = emoji,
                label = texto,
                selected = index == selectedIndex,
                onClick = { onItemClick(index) }
            )
        }
    }
}

@Composable
private fun movementFieldColors() = OutlinedTextFieldDefaults.colors(
    unfocusedBorderColor = PurpleLight,
    focusedBorderColor = Purple,
    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
    focusedContainerColor = MaterialTheme.colorScheme.surface,
    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
    focusedTextColor = MaterialTheme.colorScheme.onSurface
)

@Composable
fun FormaPagoCard(
    metodos: List<MetodoPagoEntity>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    onAddCard: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface).padding(horizontal = 20.dp).padding(bottom = 32.dp)) {
        Box(modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 16.dp).size(width = 40.dp, height = 4.dp)
            .clip(RoundedCornerShape(2.dp)).background(PurpleLight))

        Text(text = "Selecciona el método", fontWeight = FontWeight.Bold, fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp))

        metodos.forEachIndexed { index, metodo ->
            val isSelected = selectedIndex == index
            
            val emoji = when(metodo.tipo) {
                TipoMetodoPago.EFECTIVO -> "💵"
                else -> "💳"
            }
            val label = metodo.nombre ?: (if (metodo.tipo == TipoMetodoPago.TARJETA_CREDITO) "Tarjeta de Crédito" else if (metodo.tipo == TipoMetodoPago.TARJETA_DEBITO) "Tarjeta de Débito" else "Efectivo")
            val detail = if (metodo.tipo == TipoMetodoPago.EFECTIVO) "DINERO FÍSICO" else "**** ${metodo.ultimosDigitos ?: "0000"}"

            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                .clip(RoundedCornerShape(12.dp)).background(if (isSelected) Purple else PurpleLight.copy(alpha = 0.2f))
                .clickable { onSelect(index) }.padding(horizontal = 16.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp))
                    .background(
                        if (isSelected) Color.White.copy(alpha = 0.2f)
                        else Color.White
                    ), contentAlignment = Alignment.Center) {
                    Text(emoji, fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = label, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface)
                    Text(text = detail, fontSize = 12.sp, color = if (isSelected) Color.White.copy(alpha = 0.7f) else TextGray)
                }

                if (isSelected) {
                    Icon(imageVector = Icons.Outlined.CheckCircle, contentDescription = "Seleccionado", tint = Color.White, modifier = Modifier.size(22.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth().height(46.dp).clip(RoundedCornerShape(10.dp))
            .border(2.dp, Purple, RoundedCornerShape(10.dp)).clickable { onAddCard() }.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Icon(imageVector = Icons.Outlined.AddCard, contentDescription = null,
                tint = Purple, modifier = Modifier.size(20.dp))

            Spacer(modifier = Modifier.width(10.dp))

            Text(text = "Añadir tarjeta crédito / débito", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Purple)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewMovementScreenPreview() {
    GestorGastosTheme {
        NewMovementScreen(
            onBack = {},
            onSave = {},
            onNavigateToNewCard = {}
        )
    }
}

@Composable
fun GestorGastosTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = MaterialTheme.colorScheme, content = content)
}