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
import java.text.SimpleDateFormat
import java.util.Locale

class MovimientoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MovimientoRepository

    val categorias = AppDatabase
        .getInstance(application)
        .categoriaDao()
        .obtenerTodas()
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
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val fechaLong = try {
                    dateFormat.parse(fecha)?.time ?: System.currentTimeMillis()
                } catch (e: Exception) {
                    System.currentTimeMillis()
                }

                if (isGasto) {
                    val gasto = GastoEntity(
                        idUsuario = idUsuario,
                        idCategoria = idCategoria,
                        idMetodoPago = idMetodoPago,
                        monto = monto.toDouble(),
                        descripcion = descripcion,
                        fecha = fechaLong,
                        nombreUbicacion = ubicacion,
                        fotoRecibo = fotoUri
                    )
                    repository.guardarGasto(gasto)
                } else {
                    val ingreso = IngresoEntity(
                        idUsuario = idUsuario,
                        monto = monto.toDouble(),
                        descripcion = descripcion,
                        fecha = fechaLong,
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
