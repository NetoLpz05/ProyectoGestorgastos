package jesusernesto.lopezibarra.gestorgastos.screens.income_expenses

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
import jesusernesto.lopezibarra.gestorgastos.dummy.DummyData
import jesusernesto.lopezibarra.gestorgastos.ui.theme.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewMovementScreen(onBack: () -> Unit, onSave: () -> Unit, onNavigateToNewCard: () -> Unit) {
    var isGasto by remember { mutableStateOf(true) }
    var amount by remember { mutableStateOf("0.00") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("22/03/2026") }
    var selectedCategory by remember { mutableStateOf(1) }
    var selectedPayment by remember { mutableStateOf(0) }
    var showDatePicker by remember { mutableStateOf(false) }
    var isEditingAmount by remember { mutableStateOf(false) }
    var showPaymentSheet by remember { mutableStateOf(false) }

    val accent = if (isGasto) RedGasto else GreenIncome

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
                categories = DummyData.categorias,
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
            val payment = DummyData.formasPago[selectedPayment]
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
                Icon(Icons.Outlined.Wallet, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(payment.second, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
                    Text(payment.third, fontWeight = FontWeight.Bold, fontSize = 10.sp, color = Color.White.copy(alpha = 0.5f))
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

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().height(47.dp).clip(RoundedCornerShape(10.dp))
                .border(2.dp, PurpleLight, RoundedCornerShape(10.dp), ).padding(horizontal = 16.dp).background(MaterialTheme.colorScheme.surface), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.LocationOn, contentDescription = null, tint = Purple, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text("Agregar ubicación...", fontWeight = FontWeight.Bold, fontSize = 12.sp,
                    color = TextGray, fontStyle = FontStyle.Italic)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().height(47.dp)
                    .clip(RoundedCornerShape(10.dp)).border(2.dp, PurpleLight, RoundedCornerShape(10.dp)).padding(horizontal = 16.dp).background(MaterialTheme.colorScheme.surface),
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Icon(Icons.Outlined.CameraAlt, contentDescription = null, tint = TextGray, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Adjuntar foto del recibo", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextGray)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onSave, modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().height(46.dp),
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
                    selectedIndex = selectedPayment,
                    onSelect = { index ->
                        selectedPayment = index
                        showPaymentSheet = false
                    },
                    onAddCard = {
                        showPaymentSheet = false
                        onNavigateToNewCard()
                    }
                )
            }
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
    categories: List<Pair<String, String>>,
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
            CategoryItem(
                emoji = item.first,
                label = item.second,
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
fun FormaPagoCard(selectedIndex: Int, onSelect: (Int) -> Unit, onAddCard: () -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface).padding(horizontal = 20.dp).padding(bottom = 32.dp)) {
        Box(modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 16.dp).size(width = 40.dp, height = 4.dp)
            .clip(RoundedCornerShape(2.dp)).background(PurpleLight))

        Text(text = "Selecciona el método", fontWeight = FontWeight.Bold, fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp))

        DummyData.formasPago.forEachIndexed { index, (emoji, label, detail) ->
            val isSelected = selectedIndex == index
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

@Preview(showBackground = true,)
@Composable
fun FormaPagoCardPreview() {
    GestorGastosTheme {
        var selectedIndex by remember { mutableStateOf(0) }

        FormaPagoCard(selectedIndex = selectedIndex, onSelect = { selectedIndex = it })
    }
}

@Composable
fun GestorGastosTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = MaterialTheme.colorScheme, content = content)
}