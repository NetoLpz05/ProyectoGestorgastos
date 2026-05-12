package jesusernesto.lopezibarra.gestorgastos.screens.budget

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import jesusernesto.lopezibarra.gestorgastos.data.AppDatabase
import jesusernesto.lopezibarra.gestorgastos.data.SessionManager
import jesusernesto.lopezibarra.gestorgastos.data.entity.*
import jesusernesto.lopezibarra.gestorgastos.data.repository.PresupuestoConDetalles
import jesusernesto.lopezibarra.gestorgastos.data.repository.PresupuestoRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class BudgetViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)
    private val repository: PresupuestoRepository by lazy {
        PresupuestoRepository(
            db.presupuestoDao(),
            db.detallePresupuestoDao(),
            db.gastoFijoDao(),
            db.categoriaDao()
        )
    }

    private val _mesActual = MutableStateFlow(Calendar.getInstance().get(Calendar.MONTH) + 1)
    private val _anioActual = MutableStateFlow(Calendar.getInstance().get(Calendar.YEAR))

    val categorias: StateFlow<List<CategoriaEntity>> = repository.obtenerCategorias()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val presupuestoActual: StateFlow<PresupuestoConDetalles?> = combine(_mesActual, _anioActual) { mes, anio ->
        Pair(mes, anio)
    }.flatMapLatest { (mes, anio) ->
        val userId = SessionManager.usuarioActual?.idUsuario ?: -1
        if (userId != -1) {
            repository.obtenerPresupuestoCompleto(userId, mes, anio)
        } else {
            flowOf(null)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Actualización reactiva en BudgetViewModel.kt
    val gastosPorCategoria: StateFlow<Map<Int, Double>> = combine(_mesActual, _anioActual, categorias) { mes, anio, cats ->
        Triple(mes, anio, cats)
    }.flatMapLatest { (mes, anio, cats) ->
        val userId = SessionManager.usuarioActual?.idUsuario ?: return@flatMapLatest flowOf(emptyMap())
        val mesAnioPattern = "%/${String.format("%02d", mes)}/$anio"

        val flows = cats.map { cat ->
            db.gastoDao().obtenerGastoTotalPorCategoria(userId, cat.idCategoria, mesAnioPattern)
                .map { total -> cat.idCategoria to (total ?: 0.0) }
        }

        if (flows.isEmpty()) flowOf(emptyMap())
        else combine(flows) { it.toMap() }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    private val _gastosFijos = MutableStateFlow<List<GastoFijoEntity>>(emptyList())
    val gastosFijos: StateFlow<List<GastoFijoEntity>> = _gastosFijos

    fun cargarGastosFijos(idPresupuesto: Int) {
        viewModelScope.launch {
            _gastosFijos.value = repository.obtenerGastosFijos(idPresupuesto)
        }
    }

    fun guardarPresupuesto(
        ingresoMensual: Double,
        gastosFijos: List<GastoFijoEntity>,
        detalles: List<DetallePresupuestoEntity>
    ) {
        val userId = SessionManager.usuarioActual?.idUsuario ?: return
        viewModelScope.launch {
            repository.guardarPresupuestoCompleto(
                idUsuario = userId,
                mes = _mesActual.value,
                anio = _anioActual.value,
                ingresoMensual = ingresoMensual,
                gastosFijos = gastosFijos,
                detalles = detalles
            )
        }
    }
}
