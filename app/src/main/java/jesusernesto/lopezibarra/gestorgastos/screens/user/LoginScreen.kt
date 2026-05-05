package jesusernesto.lopezibarra.gestorgastos.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import jesusernesto.lopezibarra.gestorgastos.ui.theme.*
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.text.input.*

@Composable
fun LoginScreen(onLoginClick: () -> Unit, onRegisterClick: () -> Unit, onForgotPasswordClick: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(horizontal = 45.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        Spacer(Modifier.padding(5.dp))

        Box(modifier = Modifier.size(162.dp).clip(RoundedCornerShape(16.dp)).background(PurpleLight), contentAlignment = Alignment.Center) {
            Text(
                text = "aquí iría la imagen pero tengo weba de ponerla ahorita jaja",
                fontSize = 72.sp
            )
        }

        Text(text = "¡Bienvenido!", fontWeight = FontWeight.Bold, fontSize = 32.sp)

        Spacer(Modifier.padding(18.dp))

        Text(text = "Correo Electrónico", fontWeight = FontWeight.SemiBold, fontSize = 15.sp,
            color = TextGray, modifier = Modifier.align(Alignment.Start).padding(bottom = 6.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = PurpleLight,
                focusedBorderColor = Purple,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Contraseña", fontWeight = FontWeight.SemiBold, fontSize = 15.sp,
            color = TextGray, modifier = Modifier.align(Alignment.Start).padding(bottom = 6.dp))
        OutlinedTextField(value = password, onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth().height(54.dp), shape = RoundedCornerShape(10.dp),
            singleLine = true,

            visualTransformation = if (passwordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),

            trailingIcon = {
                val icon = if (passwordVisible)
                    Icons.Filled.VisibilityOff
                else
                    Icons.Filled.Visibility

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = icon, contentDescription = "Mirar Contraseña")
                }
            },

            colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = PurpleLight, focusedBorderColor = Purple,
                unfocusedContainerColor = Color.White, focusedContainerColor = Color.White))

        Spacer(modifier = Modifier.weight(1f))

        Button(onClick = onLoginClick, modifier = Modifier.fillMaxWidth().height(45.dp),
            shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Purple)) {
            Text(text = "Iniciar Sesión", fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        //Este es el botón de la biometría pero puede cambiar si lo dejamos o no :p
        Button(
            onClick = {  },
            modifier = Modifier.fillMaxWidth().height(45.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DarkNavy)) {
            Spacer(modifier = Modifier.width(8.dp))
            Icon(imageVector = Icons.Outlined.Fingerprint, contentDescription = null, tint = Color.White, modifier = Modifier.size(25.dp).padding(0.dp))
            Text(text = "Acceso Biométrico", fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = buildAnnotatedString { append("¿No tienes cuenta? ")
                withStyle(SpanStyle(color = Purple)) { append("Regístrate") } },
            fontWeight = FontWeight.SemiBold, fontSize = 15.sp, modifier = Modifier.clickable { onRegisterClick() })

        Spacer(modifier = Modifier.height(6.dp))

        Text(text = "Olvidé mi contraseña", fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp, color = Purple, modifier = Modifier
                .clickable { onForgotPasswordClick() }.padding(bottom = 24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen(
            onLoginClick = {},
            onRegisterClick = {},
            onForgotPasswordClick = {}
        )
    }
}