package jesusernesto.lopezibarra.gestorgastos.data.dao

import androidx.room.*
import jesusernesto.lopezibarra.gestorgastos.data.entity.GastoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GastoDao {
    @Insert
    suspend fun insertar(gasto: GastoEntity)

    @Query("SELECT * FROM gasto WHERE idUsuario = :idUsuario")
    fun obtenerPorUsuario(idUsuario: Int): Flow<List<GastoEntity>>

    @Query("SELECT * FROM gasto WHERE idUsuario = :idUsuario AND fecha BETWEEN :desde AND :hasta")
    fun obtenerPorRango(idUsuario: Int, desde: Long, hasta: Long): Flow<List<GastoEntity>>

    @Query("""
        SELECT SUM(monto) FROM gasto 
        WHERE idUsuario = :idUsuario AND idCategoria = :idCategoria 
        AND fecha LIKE :mesAnioPattern
    """)
    fun obtenerGastoTotalPorCategoria(idUsuario: Int, idCategoria: Int, mesAnioPattern: String): Flow<Double?>
}
