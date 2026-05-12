package jesusernesto.lopezibarra.gestorgastos.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "gasto_fijo",
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
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("idPresupuesto"), Index("idCategoria")]
)
data class GastoFijoEntity(

    @PrimaryKey(autoGenerate = true)
    val idGastoFijo: Int = 0,

    val nombre: String,

    val monto: Double,

    val idCategoria: Int? = null,

    val idPresupuesto: Int
)