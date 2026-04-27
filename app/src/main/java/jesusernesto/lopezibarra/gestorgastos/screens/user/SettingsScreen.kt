package jesusernesto.lopezibarra.gestorgastos.screens.user

import android.R
import android.R.attr.label
import android.view.Surface
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jesusernesto.lopezibarra.gestorgastos.dummy.DummyData
import jesusernesto.lopezibarra.gestorgastos.screens.components.AppTopBar
import jesusernesto.lopezibarra.gestorgastos.ui.theme.DarkNavy
import jesusernesto.lopezibarra.gestorgastos.ui.theme.Purple
import jesusernesto.lopezibarra.gestorgastos.ui.theme.PurpleLight
import jesusernesto.lopezibarra.gestorgastos.ui.theme.TextGray

@Composable
fun AlertasScreen(onBack: () -> Unit = {}){
    var alertasHabilitadas by remember { mutableStateOf(true) }
    var umbral by remember { mutableStateOf(0.8f) }

    val categoriasState = remember {
        mutableMapOf<String, Boolean>().apply {
            DummyData.categorias.forEach { (_, nombre) -> put(nombre, true) }
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F))) {
        AppTopBar(title = "Alertas", onBack = onBack)

        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(12.dp))

            Card(modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Habilitar alertas", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkNavy)
                        Text(text = "Recibe notificaciones en tiempo real", fontSize = 13.sp, color = TextGray)
                    }

                    Switch(checked = alertasHabilitadas, onCheckedChange = {alertasHabilitadas = it}, colors = SwitchDefaults.colors(Color.White, Purple))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Umbral de Notificación", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DarkNavy)
                Text(text = "${(umbral * 100).toInt()}%", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DarkNavy)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                    Slider(value = umbral, onValueChange = {umbral = it}, valueRange = 0f..1f,
                        colors = SliderDefaults.colors(thumbColor = Purple, activeTrackColor = Purple, inactiveTrackColor = PurpleLight
                    ))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        listOf("0%", "25%", "50%", "75%", "100%").forEach {
                            label ->
                            Text(text = label, fontSize = 11.sp, color = TextGray)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "\"Notifícame cuando el presupuesto llegue al ${(umbral * 100).toInt()}%\"",
                        fontSize = 13.sp, color = TextGray, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Alertas por Categoría", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkNavy, modifier = Modifier.padding(bottom = 10.dp))

            DummyData.categorias.forEach { (emoji, nombre) ->
                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp), shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)) {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {

                        Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(PurpleLight), contentAlignment = Alignment.Center){
                            Text(text = emoji, fontSize = 22.sp)
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Text(text = nombre, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = DarkNavy, modifier = Modifier.weight(1f))

                        Switch(checked = categoriasState[nombre]?: true,
                            onCheckedChange = {categoriasState[nombre] = it},
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Purple))
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        Surface(shadowElevation = 8.dp, color = Color(0xFFF5F5F5)){
            Button(onClick = {}, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp).height(48.dp),
                shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Purple)) {

                Text(text = "Guardar Configuración", fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Color.White)
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AlertasScreenPreview(){
    MaterialTheme {
        AlertasScreen (onBack = {})
    }
}
