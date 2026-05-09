package jesusernesto.lopezibarra.gestorgastos.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "presupuesto",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["idUsuario"],
            childColumns = ["idUsuario"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoriaEntity::class,
            parentColumns = ["idCategoria"],
            childColumns = ["idCategoria"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("idUsuario"), Index("idCategoria")]
)
data class PresupuestoEntity(
    @PrimaryKey(autoGenerate = true)
    val idPresupuesto: Int = 0,
    val idUsuario: Int,
    val mes: Int,
    val anio: Int,
    val nombre: String,
    val montoTotal: Float,
    val createdAt: Long = System.currentTimeMillis(),
    val idCategoria: Int? = null
)