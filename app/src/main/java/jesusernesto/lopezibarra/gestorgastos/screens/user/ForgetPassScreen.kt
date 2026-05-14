package jesusernesto.lopezibarra.gestorgastos.screens.user

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import jesusernesto.lopezibarra.gestorgastos.data.enums.PasoRecuperacion
import jesusernesto.lopezibarra.gestorgastos.ui.theme.*


@Composable
fun ForgotPasswordScreen(
    onBackToLogin: () -> Unit,
    viewModel: ForgotpasswordViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var paso by remember { mutableStateOf(PasoRecuperacion.BUSCAR_EMAIL) }

    var email by remember { mutableStateOf("") }
    var nuevaContrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var nuevaVisible by remember { mutableStateOf(false) }
    var confirmarVisible by remember { mutableStateOf(false) }

    // Avanzar al paso 2 cuando se encontró el usuario
    LaunchedEffect(uiState.usuarioEncontrado) {
        if (uiState.usuarioEncontrado) paso = PasoRecuperacion.NUEVA_CONTRASEÑA
    }

    // Avanzar al paso 3 cuando se cambió la contraseña
    LaunchedEffect(uiState.cambioExitoso) {
        if (uiState.cambioExitoso) paso = PasoRecuperacion.EXITO
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (paso) {

            PasoRecuperacion.BUSCAR_EMAIL -> {
                Text(
                    text = "Recuperar contraseña",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkNavy
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Ingresa el correo con el que te registraste",
                    fontSize = 14.sp,
                    color = TextGray
                )
                Spacer(modifier = Modifier.height(28.dp))

                FieldLabel("Correo electrónico")
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        viewModel.resetError()
                    },
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    isError = uiState.error != null,
                    colors = fieldColors()
                )
                if (uiState.error != null) {
                    Text(
                        text = uiState.error!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { viewModel.buscarEmail(email.trim().lowercase()) },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Purple),
                    enabled = !uiState.cargando
                ) {
                    if (uiState.cargando) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Continuar", fontWeight = FontWeight.SemiBold, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Volver al login",
                    color = Purple,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onBackToLogin() }
                )
            }

            PasoRecuperacion.NUEVA_CONTRASEÑA -> {
                Text(
                    text = "Nueva contraseña",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkNavy
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Elige una contraseña segura de al menos 6 caracteres",
                    fontSize = 14.sp,
                    color = TextGray
                )
                Spacer(modifier = Modifier.height(28.dp))

                FieldLabel("Nueva contraseña")
                OutlinedTextField(
                    value = nuevaContrasena,
                    onValueChange = {
                        nuevaContrasena = it
                        viewModel.resetError()
                    },
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    isError = uiState.error != null,
                    visualTransformation = if (nuevaVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { nuevaVisible = !nuevaVisible }) {
                            Icon(
                                imageVector = if (nuevaVisible) Icons.Filled.VisibilityOff
                                else Icons.Filled.Visibility,
                                contentDescription = null
                            )
                        }
                    },
                    colors = fieldColors()
                )

                Spacer(modifier = Modifier.height(12.dp))

                FieldLabel("Confirmar contraseña")
                OutlinedTextField(
                    value = confirmarContrasena,
                    onValueChange = {
                        confirmarContrasena = it
                        viewModel.resetError()
                    },
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    isError = uiState.error != null,
                    visualTransformation = if (confirmarVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { confirmarVisible = !confirmarVisible }) {
                            Icon(
                                imageVector = if (confirmarVisible) Icons.Filled.VisibilityOff
                                else Icons.Filled.Visibility,
                                contentDescription = null
                            )
                        }
                    },
                    colors = fieldColors()
                )

                if (uiState.error != null) {
                    Text(
                        text = uiState.error!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.cambiarContraseña(nuevaContrasena, confirmarContrasena)
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Purple),
                    enabled = !uiState.cargando
                ) {
                    if (uiState.cargando) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Guardar contraseña", fontWeight = FontWeight.SemiBold, color = Color.White)
                    }
                }
            }

            PasoRecuperacion.EXITO -> {
                Text("✅", fontSize = 56.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "¡Contraseña actualizada!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkNavy
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Ya puedes iniciar sesión con tu nueva contraseña",
                    fontSize = 14.sp,
                    color = TextGray
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = onBackToLogin,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Purple)
                ) {
                    Text("Ir al login", fontWeight = FontWeight.SemiBold, color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        color = TextGray,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp)
    )
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    unfocusedBorderColor = PurpleLight,
    focusedBorderColor = Purple,
    unfocusedContainerColor = Color.White,
    focusedContainerColor = Color.White
)

@Preview(showBackground = true)
@Composable
fun ForgotPasswordPreview() {
    MaterialTheme {
        ForgotPasswordScreen(onBackToLogin = {})
    }
}