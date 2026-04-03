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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jesusernesto.lopezibarra.gestorgastos.dummy.DummyData
import jesusernesto.lopezibarra.gestorgastos.ui.theme.*

@Composable
fun NewMovementScreen(
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    var isGasto by remember { mutableStateOf(true) }
    var amount by remember { mutableStateOf("0.00") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("22/03/2026") }
    var selectedCategory by remember { mutableStateOf(1) }
    var selectedPayment by remember { mutableStateOf(0) }

    val accent = if (isGasto) RedGasto else GreenIncome

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Surface(shadowElevation = 4.dp, color = Color.White) {
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
                    color = DarkNavy,
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
                    .background(if (isGasto) Color.White else PurpleLight).clickable { isGasto = true }, contentAlignment = Alignment.Center) {
                    Text("Gasto", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = if (isGasto) RedGasto else TextGray)
                }
                Box(
                    modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp))
                        .background(if (!isGasto) Color.White else PurpleLight).clickable { isGasto = false }, contentAlignment = Alignment.Center
                ) {
                    Text("Ingreso", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = if (!isGasto) GreenIncome else TextGray)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = if (isGasto) "MONTO DEL GASTO" else "MONTO DEL INGRESO", fontWeight = FontWeight.Bold, fontSize = 12.sp,
                color = TextGray, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Text("$", fontWeight = FontWeight.Bold, fontSize = 40.sp, color = TextGray)
                Spacer(modifier = Modifier.width(4.dp))
                Text(amount, fontWeight = FontWeight.Bold, fontSize = 40.sp, color = TextGray)
            }

            Spacer(modifier = Modifier.height(8.dp))

            SectionLabel("Descripción")
            OutlinedTextField(value = description, onValueChange = { description = it },
                modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(10.dp), singleLine = true, colors = movementFieldColors())

            Spacer(modifier = Modifier.height(12.dp))

            SectionLabel("Fecha")
            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Outlined.CalendarMonth, contentDescription = null, tint = Purple)
                },
                colors = movementFieldColors()
            )

            Spacer(modifier = Modifier.height(12.dp))

            SectionLabel("Categoría")
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DummyData.categorias.forEachIndexed { index, (emoji, label) ->
                    CategoryItem(
                        emoji = emoji,
                        label = label,
                        selected = selectedCategory == index,
                        onClick = { selectedCategory = index }
                    )
                }
            }

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
                        selectedPayment = (selectedPayment + 1) % DummyData.formasPago.size
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().height(47.dp).clip(RoundedCornerShape(10.dp))
                    .border(2.dp, PurpleLight, RoundedCornerShape(10.dp), ).padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.LocationOn, contentDescription = null, tint = Purple, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text("Agregar ubicación...", fontWeight = FontWeight.Bold, fontSize = 12.sp,
                    color = TextGray, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().height(47.dp)
                    .clip(RoundedCornerShape(10.dp)).border(2.dp, PurpleLight, RoundedCornerShape(10.dp)).padding(horizontal = 16.dp),
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
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable(onClick = onClick)) {
        Box(modifier = Modifier.size(34.dp).clip(CircleShape).background(if (selected) Purple else PurpleLight),
            contentAlignment = Alignment.Center) {
            Text(emoji, fontSize = 16.sp)
        }
        Text(text = label, fontSize = 9.sp, fontWeight = FontWeight.Bold,
            color = if (selected) Purple else TextGray, textAlign = TextAlign.Center, modifier = Modifier.width(50.dp))
    }
}

@Composable
private fun movementFieldColors() = OutlinedTextFieldDefaults.colors(
    unfocusedBorderColor = PurpleLight,
    focusedBorderColor = Purple,
    unfocusedContainerColor = Color.White,
    focusedContainerColor = Color.White
)

@Composable
fun FormaPagoCard(selectedIndex: Int, onSelect: (Int) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 32.dp)) {
        Box(modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 16.dp).size(width = 40.dp, height = 4.dp)
                .clip(RoundedCornerShape(2.dp)).background(PurpleLight))

        Text(text = "Selecciona el método", fontWeight = FontWeight.Bold, fontSize = 18.sp,
            color = DarkNavy, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp))

        DummyData.formasPago.forEachIndexed { index, (emoji, label, detail) ->
            val isSelected = selectedIndex == index
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                    .clip(RoundedCornerShape(12.dp)).background(if (isSelected) Purple else PurpleLight)
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
                    Text(text = label, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = if (isSelected) Color.White else DarkNavy)

                    Text(text = detail, fontSize = 12.sp, color = if (isSelected) Color.White.copy(alpha = 0.7f) else TextGray)
                }

                if (isSelected) {
                    Icon(imageVector = Icons.Outlined.CheckCircle, contentDescription = "Seleccionado", tint = Color.White, modifier = Modifier.size(22.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true,)
@Composable
fun NewMovementScreenPreview() {
    GestorGastosTheme {
        NewMovementScreen(onBack = {}, onSave = {})
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
    MaterialTheme(colorScheme = lightColorScheme(), content = content)
}