package jesusernesto.lopezibarra.gestorgastos.data.dao

import androidx.room.*
import jesusernesto.lopezibarra.gestorgastos.data.entity.MetodoPagoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MetodoPagoDao {
    @Query("SELECT * FROM metodo_pago WHERE idUsuario = :idUsuario OR idUsuario IS NULL")
    fun obtenerPorUsuario(idUsuario: Int): Flow<List<MetodoPagoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(metodoPago: MetodoPagoEntity): Long

    @Delete
    suspend fun eliminar(metodoPago: MetodoPagoEntity)

    @Query("SELECT * FROM metodo_pago WHERE idMetodoPago = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): MetodoPagoEntity?

    @Query("SELECT COUNT(*) FROM metodo_pago")
    suspend fun contarMP(): Int
}