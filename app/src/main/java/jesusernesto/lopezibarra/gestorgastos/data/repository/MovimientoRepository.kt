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

    suspend fun actualizarGasto(gasto: GastoEntity) {
        dao.updateGasto(gasto)
    }

    suspend fun actualizarIngreso(ingreso: IngresoEntity) {
        dao.updateIngreso(ingreso)
    }

    suspend fun eliminarGasto(gasto: GastoEntity) {
        dao.deleteGasto(gasto)
    }

    suspend fun eliminarIngreso(ingreso: IngresoEntity) {
        dao.deleteIngreso(ingreso)
    }

    fun obtenerGastosPorUsuario(idUsuario: Int): Flow<List<GastoEntity>> = dao.getGastosPorUsuario(idUsuario)
    fun obtenerIngresosPorUsuario(idUsuario: Int): Flow<List<IngresoEntity>> = dao.getIngresosPorUsuario(idUsuario)
    
    suspend fun obtenerGastoPorId(id: Int) = dao.getGastoPorId(id)
    suspend fun obtenerIngresoPorId(id: Int) = dao.getIngresoPorId(id)
}