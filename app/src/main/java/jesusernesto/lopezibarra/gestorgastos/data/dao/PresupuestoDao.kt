package jesusernesto.lopezibarra.gestorgastos.data.dao

import androidx.room.*
import jesusernesto.lopezibarra.gestorgastos.data.entity.PresupuestoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PresupuestoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(presupuesto: PresupuestoEntity): Long

    @Update
    suspend fun actualizar(presupuesto: PresupuestoEntity)

    @Query("SELECT * FROM presupuesto WHERE idUsuario = :idUsuario AND mes = :mes AND anio = :anio LIMIT 1")
    suspend fun obtenerPresupuesto(idUsuario: Int, mes: Int, anio: Int): PresupuestoEntity?

    @Query("SELECT * FROM presupuesto WHERE idUsuario = :idUsuario AND mes = :mes AND anio = :anio LIMIT 1")
    fun obtenerPresupuestoFlow(idUsuario: Int, mes: Int, anio: Int): Flow<PresupuestoEntity?>
}
