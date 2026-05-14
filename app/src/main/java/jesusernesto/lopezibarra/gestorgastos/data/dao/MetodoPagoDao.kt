package jesusernesto.lopezibarra.gestorgastos.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import jesusernesto.lopezibarra.gestorgastos.data.entity.MetodoPagoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MetodoPagoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(metodoPago: MetodoPagoEntity): Long

    @Query("SELECT COUNT(*) FROM metodo_pago")
    suspend fun contarMP(): Int

    @Query("SELECT * FROM metodo_pago WHERE idUsuario IS NULL OR idUsuario = :idUsuario")
    fun obtenerMetodosPagoUsuario(idUsuario: Int): Flow<List<MetodoPagoEntity>>
}