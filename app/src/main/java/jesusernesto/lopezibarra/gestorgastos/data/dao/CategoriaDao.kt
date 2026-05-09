package jesusernesto.lopezibarra.gestorgastos.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import jesusernesto.lopezibarra.gestorgastos.data.entity.CategoriaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(categoria: CategoriaEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarTodas(categorias: List<CategoriaEntity>)

    @Query("SELECT * FROM categoria ORDER BY nombre ASC")
    fun obtenerTodas(): Flow<List<CategoriaEntity>>

    @Query("SELECT * FROM categoria WHERE predefinida = 1")
    fun obtenerPredefinidas(): Flow<List<CategoriaEntity>>

    @Query("SELECT * FROM categoria WHERE idCategoria = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): CategoriaEntity?

    @Query("SELECT * FROM categoria WHERE nombre = :nombre LIMIT 1")
    suspend fun obtenerPorNombre(nombre: String): CategoriaEntity?
}