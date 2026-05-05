package jesusernesto.lopezibarra.gestorgastos.screens.user

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import jesusernesto.lopezibarra.gestorgastos.ui.theme.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.text.input.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onRegisterClick: () -> Unit, onLoginClick: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("03 Mar 2005") }
    var gender by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val genderOptions = listOf("Hombre", "Mujer", "Otro")

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White).verticalScroll(rememberScrollState()).padding(horizontal = 45.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Spacer(Modifier.padding(5.dp))

        Box(modifier = Modifier.padding(top = 0.dp).size(162.dp).background(PurpleLight, RoundedCornerShape(16.dp)), contentAlignment = Alignment.Center) {
            Text("icono de la app :p", fontSize = 72.sp)
        }

        Spacer(Modifier.padding(5.dp))

        Text(
            text = "Crear Cuenta",
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            color = DarkNavy,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        RegisterLabel("Nombre")
        RegisterTextField(value = nombre, onValueChange = { nombre = it }, placeholder = "Tu nombre completo")

        Spacer(modifier = Modifier.height(12.dp))

        RegisterLabel("Correo Electrónico")
        RegisterTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = "correo@ejemplo.com",
        )

        Spacer(modifier = Modifier.height(12.dp))


        RegisterLabel("Fecha de Nacimiento")
        OutlinedTextField(value = birthDate, onValueChange = { birthDate = it },
            modifier = Modifier.fillMaxWidth().height(54.dp), shape = RoundedCornerShape(10.dp),

        RegisterLabel("Fecha de nacimiento")

        OutlinedTextField(
            value = birthDate,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clickable { showDatePicker = true },
            shape = RoundedCornerShape(10.dp),

        if (showDatePicker) {
            val datePickerState = rememberDatePickerState()

            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        val millis = datePickerState.selectedDateMillis
                        millis?.let {
                            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                            birthDate = sdf.format(Date(it))
                        }
                        showDatePicker = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancelar")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        RegisterLabel("Género")

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = gender,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .height(54.dp),
                placeholder = { Text("Seleccionar género") },
                shape = RoundedCornerShape(10.dp),
                colors = registerTextFieldColors()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                genderOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            gender = option
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        RegisterLabel("Contraseña")
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(10.dp),
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
                    Icon(icon, contentDescription = null)
                }
            },

            colors = registerTextFieldColors()
        )
        Spacer(modifier = Modifier.height(12.dp))

        RegisterLabel("Confirmar Contraseña")
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(10.dp),
            singleLine = true,

            visualTransformation = if (confirmPasswordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),

            trailingIcon = {
                val icon = if (confirmPasswordVisible)
                    Icons.Filled.VisibilityOff
                else
                    Icons.Filled.Visibility

                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(icon, contentDescription = null)
                }
            },

            colors = registerTextFieldColors()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onRegisterClick, modifier = Modifier.fillMaxWidth().height(45.dp),
            shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Purple)) {
            Text("Crear Cuenta", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = buildAnnotatedString {
            append("¿Ya tienes cuenta? ")
            withStyle(SpanStyle(color = Purple)) { append("Iniciar Sesión") } },
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp, modifier = Modifier.clickable { onLoginClick() }.padding(bottom = 28.dp))
    }
}


@Composable
private fun RegisterLabel(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        color = TextGray,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp)
    )
}

@Composable
private fun RegisterTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = TextGray.copy(alpha = 0.5f)) },
        modifier = Modifier.fillMaxWidth().height(54.dp),
        shape = RoundedCornerShape(10.dp),
        singleLine = true,
        colors = registerTextFieldColors()
    )
}

@Composable
private fun registerTextFieldColors() = OutlinedTextFieldDefaults.colors(
    unfocusedBorderColor = PurpleLight,
    focusedBorderColor = Purple,
    unfocusedContainerColor = Color.White,
    focusedContainerColor = Color.White
)

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    MaterialTheme {
        RegisterScreen(
            onRegisterClick = {},
            onLoginClick = {}
        )
    }
}