package jesusernesto.lopezibarra.gestorgastos.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import jesusernesto.lopezibarra.gestorgastos.data.entity.UsuarioEntity

object SessionManager {
    //var usuarioActual: UsuarioEntity? = null
    var usuarioActual by mutableStateOf<UsuarioEntity?>(null)
    val estaLogueado get() = usuarioActual != null
    fun cerrarSesion() { usuarioActual = null }
}