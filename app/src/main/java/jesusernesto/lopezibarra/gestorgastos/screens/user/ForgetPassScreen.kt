package jesusernesto.lopezibarra.gestorgastos.screens.user

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import jesusernesto.lopezibarra.gestorgastos.ui.theme.*

@Composable
fun ForgotPasswordScreen(
    onSendClick: (String) -> Unit,
    onBackToLogin: () -> Unit
) {

    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

        Text(text = "Recuperar contraseña", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = DarkNavy)

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Ingresa tu correo y te enviaremos instrucciones para restablecer tu contraseña", fontSize = 14.sp, color = TextGray)

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(value = email,
            onValueChange = {
                email = it
                message = ""
            },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = PurpleLight, focusedBorderColor = Purple,
                unfocusedContainerColor = Color.White, focusedContainerColor = Color.White))

        Spacer(modifier = Modifier.height(16.dp))

        if (message.isNotEmpty()) {
            Text(text = message, color = if (isError) MaterialTheme.colorScheme.error else Color(0xFF2E7D32), fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (email.isBlank() || !email.contains("@")) {
                    message = "Correo inválido"
                    isError = true
                } else {
                    message = "Correo enviado correctamente"
                    isError = false
                    onSendClick(email)
                }
            },
            modifier = Modifier.fillMaxWidth().height(45.dp),
            shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Purple)
        ) {
            Text(text = "Enviar", color = Color.White, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Volver al login", color = Purple, fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable { onBackToLogin() })
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordPreview() {
    MaterialTheme {
        ForgotPasswordScreen(
            onSendClick = {},
            onBackToLogin = {}
        )
    }
}