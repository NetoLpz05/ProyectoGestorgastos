package jesusernesto.lopezibarra.gestorgastos.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "alerta",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["idUsuario"],
            childColumns = ["idUsuario"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PresupuestoEntity::class,
            parentColumns = ["idPresupuesto"],
            childColumns = ["idPresupuesto"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("idUsuario"), Index("idPresupuesto")]
)
data class AlertaEntity(
    @PrimaryKey(autoGenerate = true)
    val idAlerta: Int = 0,
    val idUsuario: Int,
    val idPresupuesto: Int,
    val limiteAlerta: Double,
    val activa: Boolean = true
)