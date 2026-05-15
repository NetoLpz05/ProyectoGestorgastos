package jesusernesto.lopezibarra.gestorgastos.data.viewModel

import android.app.Application
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import jesusernesto.lopezibarra.gestorgastos.data.AppDatabase
import jesusernesto.lopezibarra.gestorgastos.data.SessionManager
import jesusernesto.lopezibarra.gestorgastos.data.entity.GastoEntity
import jesusernesto.lopezibarra.gestorgastos.data.entity.IngresoEntity
import jesusernesto.lopezibarra.gestorgastos.data.repository.MovimientoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed class MovimientoUI {
    abstract val id: Int
    abstract val monto: Double
    abstract val descripcion: String
    abstract val fecha: Long
    abstract val createdAt: Long
    abstract val categoriaNombre: String

    data class Gasto(
        val entity: GastoEntity,
        override val categoriaNombre: String
    ) : MovimientoUI() {
        override val id: Int get() = entity.idGasto
        override val monto: Double get() = entity.monto
        override val descripcion: String get() = entity.descripcion
        override val fecha: Long get() = entity.fecha
        override val createdAt: Long get() = entity.createdAt
    }

    data class Ingreso(
        val entity: IngresoEntity,
        override val categoriaNombre: String
    ) : MovimientoUI() {
        override val id: Int get() = entity.idIngreso
        override val monto: Double get() = entity.monto
        override val descripcion: String get() = entity.descripcion
        override val fecha: Long get() = entity.fecha
        override val createdAt: Long get() = entity.createdAt
    }
}

class MovimientoViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val repository = MovimientoRepository(db.movimientoDao())

    val categorias = db.categoriaDao().obtenerTodas()
    
    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess

    private val userIdFlow = snapshotFlow { SessionManager.usuarioActual?.idUsuario }

    @OptIn(ExperimentalCoroutinesApi::class)
    val todosLosMovimientos: Flow<List<MovimientoUI>> = userIdFlow.flatMapLatest { userId ->
        if (userId == null || userId == 0) {
            flowOf(emptyList())
        } else {
            combine(
                repository.obtenerGastosPorUsuario(userId),
                repository.obtenerIngresosPorUsuario(userId),
                categorias
            ) { gastos, ingresos, cats ->
                val lista = mutableListOf<MovimientoUI>()
                
                gastos.forEach { gasto ->
                    val cat = cats.find { it.idCategoria == gasto.idCategoria }?.nombre ?: "📦 Otros"
                    lista.add(MovimientoUI.Gasto(gasto, cat))
                }
                
                ingresos.forEach { ingreso ->
                    val cat = cats.find { it.idCategoria == ingreso.idCategoria }?.nombre ?: "💰 Ingreso"
                    lista.add(MovimientoUI.Ingreso(ingreso, cat))
                }
                
                lista.sortedByDescending { it.fecha }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

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
                        idCategoria = idCategoria,
                        idMetodoPago = idMetodoPago,
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
