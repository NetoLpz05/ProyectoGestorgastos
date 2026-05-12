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
        )
    ],
    indices = [Index("idUsuario")]
)

data class PresupuestoEntity(

    @PrimaryKey(autoGenerate = true)
    val idPresupuesto: Int = 0,
    val idUsuario: Int,
    val mes: Int,
    val anio: Int,
    val ingresoMensual: Double,
    val createdAt: Long = System.currentTimeMillis()
)