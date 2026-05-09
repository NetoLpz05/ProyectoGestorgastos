package jesusernesto.lopezibarra.gestorgastos.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import jesusernesto.lopezibarra.gestorgastos.data.entity.AlertaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(alerta: AlertaEntity): Long

    @Update
    suspend fun actualizar(alerta: AlertaEntity)

    @Delete
    suspend fun eliminar(alerta: AlertaEntity)

    @Query("SELECT * FROM alerta WHERE idUsuario = :idUsuario")
    fun obtenerPorUsuario(idUsuario: Int): Flow<List<AlertaEntity>>

    @Query("SELECT * FROM alerta WHERE idPresupuesto = :idPresupuesto")
    fun obtenerPorPresupuesto(idPresupuesto: Int): Flow<List<AlertaEntity>>

    @Query("UPDATE alerta SET activa = :activa WHERE idAlerta = :id")
    suspend fun toggleActiva(id: Int, activa: Boolean)

    @Query("UPDATE alerta SET limiteAlerta = :limite WHERE idAlerta = :id")
    suspend fun actializarLimite(id: Int, limite: Float)

    @Query("SELECT * FROM alerta WHERE idUsuario = :idUsuario AND activa = 1")
    fun obtenerActivas(idUsuario: Int): Flow<List<AlertaEntity>>

}