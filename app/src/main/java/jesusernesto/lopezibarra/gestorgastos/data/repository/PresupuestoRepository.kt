package jesusernesto.lopezibarra.gestorgastos.data.repository

import jesusernesto.lopezibarra.gestorgastos.data.dao.*
import jesusernesto.lopezibarra.gestorgastos.data.entity.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class PresupuestoRepository(
    private val presupuestoDao: PresupuestoDao,
    private val detallePresupuestoDao: DetallePresupuestoDao,
    private val gastoFijoDao: GastoFijoDao,
    private val categoriaDao: CategoriaDao
) {
    fun obtenerPresupuestoCompleto(idUsuario: Int, mes: Int, anio: Int): Flow<PresupuestoConDetalles?> {
        return presupuestoDao.obtenerPresupuestoFlow(idUsuario, mes, anio).flatMapLatest { presupuesto ->
            if (presupuesto == null) return@flatMapLatest flowOf(null)
            
            detallePresupuestoDao.obtenerPorPresupuestoFlow(presupuesto.idPresupuesto).map { detalles ->
                PresupuestoConDetalles(presupuesto, detalles)
            }
        }
    }

    suspend fun guardarPresupuestoCompleto(
        idUsuario: Int,
        mes: Int,
        anio: Int,
        ingresoMensual: Double,
        gastosFijos: List<GastoFijoEntity>,
        detalles: List<DetallePresupuestoEntity>
    ) {
        val presupuestoExistente = presupuestoDao.obtenerPresupuesto(idUsuario, mes, anio)
        val idPresupuesto = if (presupuestoExistente != null) {
            val actualizado = presupuestoExistente.copy(ingresoMensual = ingresoMensual)
            presupuestoDao.actualizar(actualizado)
            presupuestoExistente.idPresupuesto
        } else {
            presupuestoDao.insertar(PresupuestoEntity(idUsuario = idUsuario, mes = mes, anio = anio, ingresoMensual = ingresoMensual)).toInt()
        }

        // Limpiar y guardar gastos fijos
        gastoFijoDao.eliminarPorPresupuesto(idPresupuesto)
        gastoFijoDao.insertarTodos(gastosFijos.map { it.copy(idPresupuesto = idPresupuesto) })

        // Limpiar y guardar detalles (presupuestos por categoría)
        detallePresupuestoDao.eliminarPorPresupuesto(idPresupuesto)
        detallePresupuestoDao.insertarTodos(detalles.map { it.copy(idPresupuesto = idPresupuesto) })
    }
    
    suspend fun obtenerGastosFijos(idPresupuesto: Int) = gastoFijoDao.obtenerPorPresupuesto(idPresupuesto)
    
    // Eliminado el modificador 'suspend' porque devuelve un Flow
    fun obtenerCategorias(): Flow<List<CategoriaEntity>> = categoriaDao.obtenerTodas()
}

data class PresupuestoConDetalles(
    val presupuesto: PresupuestoEntity,
    val detalles: List<DetallePresupuestoEntity>
)
