package jesusernesto.lopezibarra.gestorgastos.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "detalle_presupuesto",
    foreignKeys = [
        ForeignKey(
            entity = PresupuestoEntity::class,
            parentColumns = ["idPresupuesto"],
            childColumns = ["idPresupuesto"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoriaEntity::class,
            parentColumns = ["idCategoria"],
            childColumns = ["idCategoria"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("idPresupuesto"), Index("idCategoria")]
)
data class DetallePresupuestoEntity(
    @PrimaryKey(autoGenerate = true)
    val idDetallePresupuesto: Int = 0,
    val idPresupuesto: Int,
    val idCategoria: Int,
    val montoLimite: Float
)