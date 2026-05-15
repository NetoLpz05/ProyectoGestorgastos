package jesusernesto.lopezibarra.gestorgastos.data.repository

import jesusernesto.lopezibarra.gestorgastos.data.dao.MovimientoDao
import jesusernesto.lopezibarra.gestorgastos.data.entity.GastoEntity
import jesusernesto.lopezibarra.gestorgastos.data.entity.IngresoEntity
import kotlinx.coroutines.flow.Flow

class MovimientoRepository(private val dao: MovimientoDao) {

    suspend fun guardarGasto(gasto: GastoEntity) {
        dao.insertGasto(gasto)
    }

    suspend fun guardarIngreso(ingreso: IngresoEntity) {
        dao.insertIngreso(ingreso)
    }

    fun obtenerGastos(): Flow<List<GastoEntity>> = dao.getAllGastos()
    fun obtenerIngresos(): Flow<List<IngresoEntity>> = dao.getAllIngresos()
}