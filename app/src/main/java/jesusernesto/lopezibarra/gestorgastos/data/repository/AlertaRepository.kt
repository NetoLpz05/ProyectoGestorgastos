package jesusernesto.lopezibarra.gestorgastos.data.repository

import jesusernesto.lopezibarra.gestorgastos.data.dao.AlertaDao
import jesusernesto.lopezibarra.gestorgastos.data.entity.AlertaEntity
import jesusernesto.lopezibarra.gestorgastos.dummy.Presupuesto
import kotlinx.coroutines.flow.Flow

class AlertaRepository (private val dao: AlertaDao){

    fun obtenerAlertas(idUsuario: Int): Flow<List<AlertaEntity>> =
        dao.obtenerPorUsuario(idUsuario)

    fun obtenerAlertasActivas(idUsuario: Int): Flow<List<AlertaEntity>> =
        dao.obtenerPorPresupuesto(idUsuario)

    suspend fun crearAlerta(
        idUsuario: Int,
        idPresupuesto: Int,
        limiteAlerta: Float
    ): AlertaEntity{
        val alerta = AlertaEntity(
            idUsuario = idUsuario,
            idPresupuesto = idPresupuesto,
            limiteAlerta = limiteAlerta,
            activa = true
        )
        val id = dao.insertar(alerta)
        return alerta.copy(idAlerta = id.toInt())
    }

    suspend fun toggleAlerta(id: Int, activa: Boolean) =
        dao.toggleActiva(id, activa)

    suspend fun actualizarLimite(id: Int, limite: Float) =
        dao.actializarLimite(id, limite)

    suspend fun eliminarAlerta(alerta: AlertaEntity) =
        dao.eliminar(alerta)

}