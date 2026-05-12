package jesusernesto.lopezibarra.gestorgastos.data.viewModel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import jesusernesto.lopezibarra.gestorgastos.data.AppDatabase
import jesusernesto.lopezibarra.gestorgastos.data.SessionManager
import jesusernesto.lopezibarra.gestorgastos.data.entity.UsuarioEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UsuarioViewModel(app: Application) : AndroidViewModel(app) {

    private val dao = AppDatabase.getInstance(app).usuarioDao()

    private val _usuario = MutableStateFlow<UsuarioEntity?>(SessionManager.usuarioActual)
    val usuario: StateFlow<UsuarioEntity?> = _usuario

    private val _guardadoExitoso = MutableStateFlow(false)
    val guardadoExitoso: StateFlow<Boolean> = _guardadoExitoso

    fun actualizarPerfil(nombre: String, apellido: String, telefono: String) {
        val actual = _usuario.value ?: return
        viewModelScope.launch {
            val actualizado = actual.copy(nombre = nombre, apellido = apellido)
            dao.actualizar(actualizado)
            SessionManager.usuarioActual = actualizado
            _usuario.value = actualizado
            _guardadoExitoso.value = true
        }
    }

    fun resetGuardado() {
        _guardadoExitoso.value = false
    }
}