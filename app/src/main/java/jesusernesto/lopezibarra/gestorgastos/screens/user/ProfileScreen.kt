package jesusernesto.lopezibarra.gestorgastos.screens.user

import androidx.compose.foundation.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jesusernesto.lopezibarra.gestorgastos.dummy.DummyData
import jesusernesto.lopezibarra.gestorgastos.dummy.UserProfile
import jesusernesto.lopezibarra.gestorgastos.ui.theme.*

@Composable
fun ProfileScreen(onBack: () -> Unit, onLogout: () -> Unit) {
    var modoOscuro by remember { mutableStateOf(false) }
    var biometria by remember { mutableStateOf(true) }
    var alertaPresupuesto by remember { mutableStateOf(true) }
    var estaEditando by remember { mutableStateOf(false) }
    var estaEnAlertas by remember { mutableStateOf(false) }

    val user = DummyData.userActual

    if (estaEditando) {
        EditProfileScreen(
            user = user,
            onBack = { estaEditando = false },
            onSave = { estaEditando = false }
        )
        return
    }

    if (estaEnAlertas){
        AlertasScreen (onBack = {estaEnAlertas = false})
        return
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {

        AppTopBar(title = "Mi Perfil", onBack = onBack)

        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(114.dp).clip(CircleShape).background(PurpleLight).border(3.dp, PurpleLight, CircleShape), contentAlignment = Alignment.Center) {
                    Image(painter = painterResource(id = user.pfp), contentDescription = "Foto de perfil", modifier = Modifier.fillMaxSize().clip(CircleShape))
                }
                Text(text = "Editar Foto", color = Purple, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 6.dp))
                Spacer(modifier = Modifier.height(4.dp))
                Text(user.nombre, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = DarkNavy)
                Text(user.correo, fontSize = 14.sp, color = TextGray)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("INFORMACIÓN PERSONAL", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextGray, modifier = Modifier.weight(1f))
                Text(text = "Editar Información", color = Purple, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable { estaEditando = true })
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().clip(RoundedCornerShape(12.dp))
                    .border(2.dp, PurpleLight, RoundedCornerShape(12.dp))) {
                Column {
                    InfoRow(icon = Icons.Outlined.Person, label = "NOMBRE COMPLETO", value = user.nombre)
                    Divider(color = PurpleLight)
                    InfoRow(icon = Icons.Outlined.Phone, label = "TELÉFONO", value = user.telefono)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TituloSeccion("SEGURIDAD")
            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().clip(RoundedCornerShape(12.dp))
                    .border(2.dp, PurpleLight, RoundedCornerShape(12.dp))) {
                Row(modifier = Modifier.fillMaxWidth().height(58.dp)
                        .padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Fingerprint, contentDescription = null, tint = Purple, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Biometría", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = DarkNavy)
                        Text("HUELLA / FACEID", fontSize = 12.sp, color = TextGray)
                    }
                    Switch(
                        checked = biometria,
                        onCheckedChange = { biometria = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Purple)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            TituloSeccion("PRIVACIDAD")
            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().clip(RoundedCornerShape(12.dp))
                    .border(2.dp, PurpleLight, RoundedCornerShape(12.dp)).clickable(onClick = {estaEnAlertas = true})) {
                Row(modifier = Modifier.fillMaxWidth().height(54.dp)
                        .padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Notifications, contentDescription = null, tint = Purple, modifier = Modifier.size(27.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Editar Notificaciones", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = DarkNavy)
                        Text("ALERTAS DE PRESUPUESTO", fontSize = 12.sp, color = TextGray)
                    }
                    Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = TextGray)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            TituloSeccion("PERSONALIZACIÓN")
            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)).border(2.dp, PurpleLight, RoundedCornerShape(12.dp))) {
                Row(modifier = Modifier.fillMaxWidth().height(54.dp)
                        .padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.DarkMode, contentDescription = null, tint = Purple, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Modo Oscuro", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = DarkNavy)
                        Text("ACTIVAR MODO OSCURO EN LA APP", fontSize = 11.sp, color = TextGray)
                    }
                    Switch(
                        checked = modoOscuro,
                        onCheckedChange = { modoOscuro = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Purple)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Cerrar Sesión", fontWeight = FontWeight.SemiBold, fontSize = 15.sp,
                color = RedExpense, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().clickable { onLogout() }.padding(vertical = 12.dp))

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun EditProfileScreen(
    user: UserProfile,
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    var name by remember { mutableStateOf(user.nombre) }
    var phone by remember { mutableStateOf(user.telefono) }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {

        AppTopBar(title = "Editar mi Perfil", onBack = onBack)

        Column(modifier = Modifier.weight(1f)
                .verticalScroll(rememberScrollState()).padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(114.dp).clip(CircleShape).background(PurpleLight),
                    contentAlignment = Alignment.Center) {
                    Image(painter = painterResource(id = user.pfp),
                        contentDescription = "Foto de perfil", modifier = Modifier.size(114.dp).clip(CircleShape))
                }
                Text("Editar Foto", color = Purple, fontSize = 12.sp, fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 6.dp, bottom = 16.dp))
            }

            TituloSeccion("NOMBRE COMPLETO")
            Spacer(modifier = Modifier.height(4.dp))
            EditField(value = name, onValueChange = { name = it }, icon = Icons.Outlined.Person)

            Spacer(modifier = Modifier.height(12.dp))

            TituloSeccion("TELÉFONO")
            Spacer(modifier = Modifier.height(4.dp))
            EditField(value = phone, onValueChange = { phone = it }, icon = Icons.Outlined.Phone)

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth().height(46.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Purple)
            ) {
                Text("Guardar Cambios", fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().height(58.dp).padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Purple, modifier = Modifier.size(27.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(label, fontSize = 11.sp, color = TextGray, fontWeight = FontWeight.Bold)
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = DarkNavy)
        }
    }
}

@Composable
private fun TituloSeccion(text: String) {
    Text(text = text, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextGray, modifier = Modifier.padding(horizontal = 16.dp))
}

@Composable
private fun EditField(value: String, onValueChange: (String) -> Unit, icon: ImageVector) {
    OutlinedTextField(value = value,
        onValueChange = onValueChange, modifier = Modifier.fillMaxWidth().height(54.dp),
        shape = RoundedCornerShape(10.dp), singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = PurpleLight,
            focusedBorderColor = Purple,
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White
        )
    )
}

@Preview(showBackground = true, name = "Vista Perfil Usuario")
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileScreen(
            onBack = {  },
            onLogout = {  }
        )
    }
}

@Preview(showBackground = true, name = "Vista Editar Perfil")
@Composable
fun EditProfileScreenPreview() {
    val mockUser = UserProfile(
        nombre = "Jesús Ernesto",
        correo = "jesus@ejemplo.com",
        telefono = "6441234567",
        pfp = android.R.drawable.ic_menu_gallery
    )

    MaterialTheme {
        EditProfileScreen(
            user = mockUser,
            onBack = { },
            onSave = { }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(title: String, onBack: () -> Unit) {
    TopAppBar(
        title = { Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkNavy) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Regresar",
                    tint = DarkNavy
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}