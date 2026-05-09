package jesusernesto.lopezibarra.gestorgastos.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import jesusernesto.lopezibarra.gestorgastos.ui.theme.*

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    viewModel: UsuarioViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    // Navegar automáticamente cuando el login es exitoso
    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Exito) {
            viewModel.resetState()
            onLoginClick()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 45.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.padding(5.dp))

        Box(
            modifier = Modifier
                .size(162.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(PurpleLight),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "💰", fontSize = 72.sp)
        }

        Text(text = "¡Bienvenido!", fontWeight = FontWeight.Bold, fontSize = 32.sp)

        Spacer(Modifier.padding(18.dp))

        // Mensaje de error visible bajo el título
        if (uiState is AuthUiState.Error) {
            Text(
                text = (uiState as AuthUiState.Error).mensaje,
                color = MaterialTheme.colorScheme.error,
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Text(
            text = "Correo Electrónico",
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            color = TextGray,
            modifier = Modifier.align(Alignment.Start).padding(bottom = 6.dp)
        )
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                if (uiState is AuthUiState.Error) viewModel.resetState()
            },
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = PurpleLight, focusedBorderColor = Purple,
                unfocusedContainerColor = Color.White, focusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Contraseña",
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            color = TextGray,
            modifier = Modifier.align(Alignment.Start).padding(bottom = 6.dp)
        )
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                if (uiState is AuthUiState.Error) viewModel.resetState()
            },
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = null
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = PurpleLight, focusedBorderColor = Purple,
                unfocusedContainerColor = Color.White, focusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { viewModel.login(email, password) },
            modifier = Modifier.fillMaxWidth().height(45.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Purple),
            enabled = uiState !is AuthUiState.Cargando
        ) {
            if (uiState is AuthUiState.Cargando) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
                Text("Iniciar Sesión", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().height(45.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DarkNavy)
        ) {
            Icon(imageVector = Icons.Outlined.Fingerprint, contentDescription = null, tint = Color.White, modifier = Modifier.size(25.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Acceso Biométrico", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = buildAnnotatedString {
                append("¿No tienes cuenta? ")
                withStyle(SpanStyle(color = Purple)) { append("Regístrate") }
            },
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            modifier = Modifier.clickable { onRegisterClick() }
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Olvidé mi contraseña",
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            color = Purple,
            modifier = Modifier.clickable { onForgotPasswordClick() }.padding(bottom = 24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen(onLoginClick = {}, onRegisterClick = {}, onForgotPasswordClick = {})
    }
}