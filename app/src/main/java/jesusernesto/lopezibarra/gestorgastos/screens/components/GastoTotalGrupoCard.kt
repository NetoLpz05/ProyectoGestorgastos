package jesusernesto.lopezibarra.gestorgastos.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jesusernesto.lopezibarra.gestorgastos.ui.theme.Purple
import jesusernesto.lopezibarra.gestorgastos.ui.theme.TextSubtle
import jesusernesto.lopezibarra.gestorgastos.ui.theme.White

data class GastoTotalCardData(
    val titulo: String = "GASTO TOTAL\nCOMPARTIDO",
    val montoTotal: String = "$1,200.00",
    val totalParticipantes: Int = 5
)


@Composable
fun GastoTotalCard(
    data: GastoTotalCardData,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Purple)
            .padding(24.dp)
    ) {
        Column {
            Text(
                text = data.titulo,
                color = TextSubtle,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp,
                lineHeight = 18.sp
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = data.montoTotal,
                color = White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp
            )

            Spacer(Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Group,
                    contentDescription = null,
                    tint = TextSubtle,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "${data.totalParticipantes} participantes activos",
                    color = White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5)
@Composable
fun GastoTotalCardPreview() {
    GastoTotalCard(
        data = GastoTotalCardData(
            montoTotal = "$1,200.00",
            totalParticipantes = 5
        ),
        modifier = Modifier.padding(16.dp)
    )
}