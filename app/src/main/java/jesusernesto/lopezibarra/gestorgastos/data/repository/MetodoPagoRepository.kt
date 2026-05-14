package jesusernesto.lopezibarra.gestorgastos.data.repository

import jesusernesto.lopezibarra.gestorgastos.data.dao.MetodoPagoDao
import jesusernesto.lopezibarra.gestorgastos.data.entity.MetodoPagoEntity
import kotlinx.coroutines.flow.Flow

class MetodoPagoRepository(private val dao: MetodoPagoDao) {
    suspend fun guardarMetodoPago(metodoPago: MetodoPagoEntity): Long {
        return dao.insert(metodoPago)
    }

    fun obtenerMetodosPagoUsuario(idUsuario: Int): Flow<List<MetodoPagoEntity>> {
        return dao.obtenerMetodosPagoUsuario(idUsuario)
    }
}