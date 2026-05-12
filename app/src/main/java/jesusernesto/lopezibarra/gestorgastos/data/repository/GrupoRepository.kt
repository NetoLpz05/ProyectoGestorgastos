package jesusernesto.lopezibarra.gestorgastos.data.repository

import jesusernesto.lopezibarra.gestorgastos.data.Grupo
import jesusernesto.lopezibarra.gestorgastos.data.dao.GrupoDao
import jesusernesto.lopezibarra.gestorgastos.data.entity.DeudaGrupoEntity
import jesusernesto.lopezibarra.gestorgastos.data.entity.GastoGrupoEntity
import jesusernesto.lopezibarra.gestorgastos.data.entity.GrupoEntity
import jesusernesto.lopezibarra.gestorgastos.data.entity.UsuarioEntity
import jesusernesto.lopezibarra.gestorgastos.data.entity.UsuarioGrupoEntity
import kotlinx.coroutines.flow.Flow
import kotlin.random.Random

sealed class GrupoResult{
    data class Exito(val grupo: GrupoEntity) : GrupoResult()
    data class Error(val mensaje: String) : GrupoResult()
}

class GrupoRepository(private val dao: GrupoDao){
    fun obtenerGrupos(): Flow<List<GrupoEntity>> = dao.obtenerTodos()

    suspend fun crearGrupo(
        nombre: String,
        tipo: String,
        imagen: String = ""
    ): GrupoResult{
        if (nombre.isBlank()) return GrupoResult.Error("El nombre no puede estar vacío")
        val codigo = generarCodigo()
        val grupo = GrupoEntity(
            nombre = nombre.trim(),
            tipo = tipo,
            codigoInvitacion = codigo,
            imagen = imagen
        )
        val id = dao.insertarGrupo(grupo)
        return GrupoResult.Exito(grupo.copy(idGrupo = id.toInt()))
    }

    suspend fun eliminarGrupo(grupo: GrupoEntity) = dao.eliminarGrupo(grupo)
    suspend fun buscarPorCodigo(codigo: String): GrupoEntity?=
        dao.obtenerPorCodigo(codigo.uppercase().trim())

    fun obtenerMiembros(idGrupo: Int): Flow<List<UsuarioEntity>> =
        dao.obtenerUsuariosPorGrupo(idGrupo)

    suspend fun agregarMiembro(idGrupo: Int, idUsuario: Int): GrupoResult{
        return try {
            val relacion = UsuarioGrupoEntity(idGrupo = idGrupo, idUsuario = idUsuario)
            dao.insertarUsuarioGrupo(relacion)
            val grupo = dao.obtenerPorId(idGrupo) ?:
            return GrupoResult.Error("Grupo no encontrado")
            GrupoResult.Exito(grupo)
        } catch (e: Exception){
            GrupoResult.Error("No se pudo agregar el miembro")
        }
    }

    suspend fun eliminarMiembro(usuarioGrupo: UsuarioGrupoEntity) =
        dao.eliminarUsuarioGrupo(usuarioGrupo)

    fun obtenerGastosGrupo(idGrupo: Int): Flow<List<GastoGrupoEntity>> =
        dao.obtenerGastosPorGrupo(idGrupo)

    suspend fun agregarGastoGrupo(
        idGrupo: Int,
        idUsuarioPago: Int,
        idCategoria: Int,
        nombre: String,
        monto: Double,
        fecha: Long,
        participantes: List<Int>
    ): GrupoResult {
        if (nombre.isBlank()) return GrupoResult.Error("El nombre no puede estar vacío")
        if (monto <= 0) return GrupoResult.Error("El monto debe ser mayor a 0")
        if (participantes.isEmpty()) return GrupoResult.Error("Debe haber al menos un participante")

        return try {
            val gasto = GastoGrupoEntity(
                idGrupo = idGrupo,
                idUsuarioPago = idUsuarioPago,
                idCategoria = idCategoria,
                nombre = nombre.trim(),
                monto = monto,
                fecha = fecha
            )
            val idGasto = dao.insertarGastoGrupo(gasto)

            val montoPorPersona = monto / participantes.size
            participantes
                .filter { it != idUsuarioPago }
                .forEach { idParticipante ->
                    val deuda = DeudaGrupoEntity(
                        idGastoGrupo = idGasto.toInt(),
                        idUsuario = idParticipante,
                        montoDeuda = montoPorPersona
                    )
                    dao.insertarDeuda(deuda)
                }

            val grupo = dao.obtenerPorId(idGrupo)
                ?: return GrupoResult.Error("Grupo no encontrado")
            GrupoResult.Exito(grupo)
        } catch (e: Exception) {
            GrupoResult.Error("No se pudo registrar el gasto")
        }
    }

    suspend fun totalGastos(idGrupo: Int): Float =
        dao.totalGastosPorGrupo(idGrupo) ?: 0f

    fun obtenerDeudasPendientes(idUsuario: Int): Flow<List<DeudaGrupoEntity>> =
        dao.obtenerDeudasPendientesUsuario(idUsuario)

    suspend fun marcarPagada(idDeuda: Int) = dao.marcarDeudaPagada(idDeuda)


    private fun generarCodigo(): String{
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..6).map { chars[Random.nextInt(chars.length)] }.joinToString("")
    }
}

