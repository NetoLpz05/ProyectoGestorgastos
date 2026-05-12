package jesusernesto.lopezibarra.gestorgastos.data.dao

import androidx.room.*
import jesusernesto.lopezibarra.gestorgastos.data.entity.GastoEntity
import jesusernesto.lopezibarra.gestorgastos.data.entity.IngresoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovimientoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGasto(gasto: GastoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngreso(ingreso: IngresoEntity)

    @Query("SELECT * FROM gasto ORDER BY createdAt DESC")
    fun getAllGastos(): Flow<List<GastoEntity>>

    @Query("SELECT * FROM ingreso ORDER BY createdAt DESC")
    fun getAllIngresos(): Flow<List<IngresoEntity>>
}
