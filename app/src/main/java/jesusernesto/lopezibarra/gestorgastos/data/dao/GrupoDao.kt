package jesusernesto.lopezibarra.gestorgastos.data.dao

import androidx.room.*
import jesusernesto.lopezibarra.gestorgastos.data.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GrupoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarGrupo(grupo: GrupoEntity): Long

    @Delete
    suspend fun eliminarGrupo(grupo: GrupoEntity)

    @Query("SELECT * FROM grupo ORDER BY createdAt DESC")
    fun obtenerTodos(): Flow<List<GrupoEntity>>

    @Query("SELECT * FROM grupo WHERE idGrupo = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): GrupoEntity?

    @Query("SELECT * FROM grupo WHERE codigoInvitacion = :codigo LIMIT 1")
    suspend fun obtenerPorCodigo(codigo: String): GrupoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuarioGrupo(usuarioGrupo: UsuarioGrupoEntity): Long

    @Delete
    suspend fun eliminarUsuarioGrupo(usuarioGrupo: UsuarioGrupoEntity)

    @Query("SELECT * FROM usuario_grupo WHERE idGrupo = :idGrupo")
    fun obtenerMiembrosPorGrupo(idGrupo: Int): Flow<List<UsuarioGrupoEntity>>

    @Query("SELECT COUNT(*) FROM usuario_grupo WHERE idGrupo = :idGrupo")
    suspend fun contarMiembros(idGrupo: Int): Int

    @Query("""
        SELECT u.* FROM usuario u
        INNER JOIN usuario_grupo ug ON u.idUsuario = ug.idUsuario
        WHERE ug.idGrupo = :idGrupo
    """)
    fun obtenerUsuariosPorGrupo(idGrupo: Int): Flow<List<UsuarioEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarGastoGrupo(gasto: GastoGrupoEntity): Long

    @Delete
    suspend fun eliminarGastoGrupo(gasto: GastoGrupoEntity)

    @Query("SELECT * FROM gasto_grupo WHERE idGrupo = :idGrupo ORDER BY fecha DESC")
    fun obtenerGastosPorGrupo(idGrupo: Int): Flow<List<GastoGrupoEntity>>

    @Query("SELECT SUM(monto) FROM gasto_grupo WHERE idGrupo = :idGrupo")
    suspend fun totalGastosPorGrupo(idGrupo: Int): Float?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarDeuda(deuda: DeudaGrupoEntity): Long

    @Query("UPDATE deuda_grupo SET pagado = 1 WHERE idDeudaGrupo = :id")
    suspend fun marcarDeudaPagada(id: Int)

    @Query("SELECT * FROM deuda_grupo WHERE idGastoGrupo = :idGastoGrupo")
    fun obtenerDeudasPorGasto(idGastoGrupo: Int): Flow<List<DeudaGrupoEntity>>

    @Query("SELECT * FROM deuda_grupo WHERE idUsuario = :idUsuario AND pagado = 0")
    fun obtenerDeudasPendientesUsuario(idUsuario: Int): Flow<List<DeudaGrupoEntity>>
}