package jesusernesto.lopezibarra.gestorgastos.data.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import jesusernesto.lopezibarra.gestorgastos.data.AppDatabase
import jesusernesto.lopezibarra.gestorgastos.data.SessionManager
import jesusernesto.lopezibarra.gestorgastos.data.entity.GastoEntity
import jesusernesto.lopezibarra.gestorgastos.data.entity.IngresoEntity
import jesusernesto.lopezibarra.gestorgastos.data.repository.MovimientoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovimientoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MovimientoRepository
    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess

    init {
        val dao = AppDatabase.getInstance(application).movimientoDao()
        repository = MovimientoRepository(dao)
    }

    fun guardarMovimiento(
        isGasto: Boolean,
        monto: Float,
        descripcion: String,
        fecha: String,
        idCategoria: Int,
        idMetodoPago: Int,
        ubicacion: String?,
        fotoUri: String?
    ) {
        val idUsuario = SessionManager.usuarioActual?.idUsuario ?: return

        viewModelScope.launch {
            try {
                if (isGasto) {
                    val gasto = GastoEntity(
                        idUsuario = idUsuario,
                        idCategoria = idCategoria,
                        idMetodoPago = idMetodoPago,
                        monto = monto,
                        descripcion = descripcion,
                        fecha = fecha,
                        nombreUbicacion = ubicacion,
                        fotoRecibo = fotoUri
                    )
                    repository.guardarGasto(gasto)
                } else {
                    val ingreso = IngresoEntity(
                        idUsuario = idUsuario,
                        monto = monto,
                        descripcion = descripcion,
                        fecha = fecha,
                        nombreUbicacion = ubicacion,
                        fotoRecibo = fotoUri
                    )
                    repository.guardarIngreso(ingreso)
                }
                _saveSuccess.value = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun resetSaveSuccess() {
        _saveSuccess.value = false
    }
}
