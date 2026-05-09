package jesusernesto.lopezibarra.gestorgastos.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categoria")
data class CategoriaEntity(
    @PrimaryKey(autoGenerate = true)
    val idCategoria: Int = 0,
    val nombre: String,
    val predefinida: Boolean = false
)
