package jesusernesto.lopezibarra.gestorgastos.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "ingreso",
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
data class IngresoEntity(
    @PrimaryKey(autoGenerate = true)
    val idIngreso: Int = 0,
    val idUsuario: Int,
    val monto: Double,
    val descripcion: String,
    val fecha: Long,
    val latitud: Float? = null,
    val longitud: Float? = null,
    val createdAt: Long = System.currentTimeMillis()
)
