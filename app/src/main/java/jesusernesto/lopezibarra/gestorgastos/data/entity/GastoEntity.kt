package jesusernesto.lopezibarra.gestorgastos.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "gasto",
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
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = MetodoPagoEntity::class,
            parentColumns = ["idMetodoPago"],
            childColumns = ["idMetodoPago"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = GastoGrupoEntity::class,
            parentColumns = ["idGastoGrupo"],
            childColumns = ["idGastoGrupo"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("idUsuario"),
        Index("idCategoria"),
        Index("idMetodoPago"),
        Index("idGastoGrupo")
    ]
)
data class GastoEntity(
    @PrimaryKey(autoGenerate = true)
    val idGasto: Int = 0,
    val idUsuario: Int,
    val idCategoria: Int,
    val idMetodoPago: Int,
    val monto: Float,
    val descripcion: String,
    val fecha: String,
    val latitud: Float? = null,
    val longitud: Float? = null,
    val fotoRecibo: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val idGastoGrupo: Int? = null,
    val esGrupal: Boolean = false
)