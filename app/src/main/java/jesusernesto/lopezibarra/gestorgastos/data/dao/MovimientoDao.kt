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

    @Delete
    suspend fun editGasto(gasto: GastoEntity)

    @Delete
    suspend fun editIngreso(ingreso: IngresoEntity)

    @Query("SELECT * FROM gasto WHERE idUsuario = :idUsuario ORDER BY fecha DESC")
    fun getGastosPorUsuario(idUsuario: Int): Flow<List<GastoEntity>>

    @Query("SELECT * FROM ingreso WHERE idUsuario = :idUsuario ORDER BY fecha DESC")
    fun getIngresosPorUsuario(idUsuario: Int): Flow<List<IngresoEntity>>

    @Query("SELECT * FROM gasto WHERE idGasto = :id")
    suspend fun getGastoPorId(id: Int): GastoEntity?

    @Query("SELECT * FROM ingreso WHERE idIngreso = :id")
    suspend fun getIngresoPorId(id: Int): IngresoEntity?
}
