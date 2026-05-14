package jesusernesto.lopezibarra.gestorgastos.screens.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import jesusernesto.lopezibarra.gestorgastos.data.AppDatabase
import jesusernesto.lopezibarra.gestorgastos.data.entity.UsuarioEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.security.MessageDigest

data class ForgotPasswordUiState(
    val usuarioEncontrado: Boolean = false,
    val cambioExitoso: Boolean = false,
    val cargando: Boolean = false,
    val error: String? = null
)

class ForgotpasswordViewModel (application: Application): AndroidViewModel(application){

    private val dao = AppDatabase.getInstance(application).usuarioDao()

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState

    private var usuarioAvtual: UsuarioEntity? = null

    fun buscarEmail(email: String){
        if (email.isBlank() || !email.contains("@")){
            _uiState.update { it.copy(error = "Correo electrónico inválido") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true, error = null) }
            val usuario = dao.buscarPorEmail(email)
            if (usuario == null){
                _uiState.update {
                    it.copy(cargando = false, error = "No existe una cuenta con ese correo")
                }
            }else{
                usuarioAvtual = usuario
                _uiState.update { it.copy(cargando = false, usuarioEncontrado = true) }
            }
        }
    }

    fun cambiarContraseña(nueva: String, confirmacion: String){
        if (nueva.length < 6){
            _uiState.update { it.copy(error = "La contraseña debe tener al menos 6 caracteres") }
            return
        }
        val usuario = usuarioAvtual ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true, error = null) }
            dao.actualizarContrasena(usuario.email, hashContraseña(nueva))
            _uiState.update { it.copy(cargando = false, cambioExitoso = true) }
        }
    }

    fun resetError(){
        _uiState.update { it.copy(error = null) }
    }

    private fun hashContraseña(contraseña: String): String{
        val bytes = MessageDigest.getInstance("SHA-256").digest(contraseña.toByteArray())
        return bytes.joinToString(""){ "%02x".format(it)}
    }

}