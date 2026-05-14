package jesusernesto.lopezibarra.gestorgastos.screens.income_expenses

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import jesusernesto.lopezibarra.gestorgastos.data.AppDatabase
import jesusernesto.lopezibarra.gestorgastos.data.SessionManager
import jesusernesto.lopezibarra.gestorgastos.data.entity.MetodoPagoEntity
import jesusernesto.lopezibarra.gestorgastos.data.enums.TipoMetodoPago
import jesusernesto.lopezibarra.gestorgastos.data.repository.MetodoPagoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

class MetodoPagoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MetodoPagoRepository by lazy {
        val dao = AppDatabase.getInstance(application).metodoPagoDao()
        MetodoPagoRepository(dao)
    }

    val metodosPago: StateFlow<List<MetodoPagoEntity>> = SessionManager.usuarioActual?.let {
        repository.obtenerMetodosPagoUsuario(it.idUsuario)
    }?.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList<MetodoPagoEntity>()
    ) ?: flowOf(emptyList<MetodoPagoEntity>()).stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList<MetodoPagoEntity>()
    )
    fun guardarTarjeta(
        nombre: String,
        numeroCompleto: String,
        esCredito: Boolean,
        onSuccess: () -> Unit
    ) {
        val usuario = SessionManager.usuarioActual ?: return
        val ultimos4 = if (numeroCompleto.length >= 4) {
            numeroCompleto.takeLast(4).toIntOrNull()
        } else null

        val tipo = if (esCredito) TipoMetodoPago.TARJETA_CREDITO else TipoMetodoPago.TARJETA_DEBITO

        viewModelScope.launch {
            val nuevaTarjeta = MetodoPagoEntity(
                idUsuario = usuario.idUsuario,
                tipo = tipo,
                ultimosDigitos = ultimos4,
                nombre = nombre
            )
            repository.guardarMetodoPago(nuevaTarjeta)
            onSuccess()
        }
    }
}