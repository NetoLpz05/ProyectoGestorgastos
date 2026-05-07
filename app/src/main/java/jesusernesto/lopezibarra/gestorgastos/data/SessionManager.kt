package jesusernesto.lopezibarra.gestorgastos.data

import jesusernesto.lopezibarra.gestorgastos.data.entity.UsuarioEntity

object SessionManager {
    var usuarioActual: UsuarioEntity? = null
    val estaLogueado get() = usuarioActual != null
    fun cerrarSesion() { usuarioActual = null }
}