package jesusernesto.lopezibarra.gestorgastos.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import jesusernesto.lopezibarra.gestorgastos.data.entity.MetodoPagoEntity

@Dao
interface MetodoPagoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(metodoPago: MetodoPagoEntity): Long

    @Query("SELECT COUNT(*) FROM metodo_pago")
    suspend fun contarMP(): Int
}