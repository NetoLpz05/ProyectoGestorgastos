package jesusernesto.lopezibarra.gestorgastos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import jesusernesto.lopezibarra.gestorgastos.screens.MainScreen
import jesusernesto.lopezibarra.gestorgastos.screens.user.LoginScreen
import jesusernesto.lopezibarra.gestorgastos.screens.user.RegisterScreen
import jesusernesto.lopezibarra.gestorgastos.ui.theme.GestorgastosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GestorgastosTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppRoot()
                }
            }
        }
    }
}

private enum class Screen { LOGIN, REGISTER, MAIN }

@Composable
private fun AppRoot() {
    var pantallaActual by remember { mutableStateOf(Screen.LOGIN) }

    when (pantallaActual) {
        Screen.LOGIN -> LoginScreen(
            onLoginClick = { pantallaActual = Screen.MAIN },
            onRegisterClick = { pantallaActual = Screen.REGISTER },
            onForgotPasswordClick = {}
        )

        Screen.REGISTER -> RegisterScreen(
            onRegisterClick = { pantallaActual = Screen.MAIN },
            onLoginClick = { pantallaActual = Screen.LOGIN }
        )

        Screen.MAIN -> MainScreen(
            onLogout = { pantallaActual = Screen.LOGIN }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppRoot()
}
