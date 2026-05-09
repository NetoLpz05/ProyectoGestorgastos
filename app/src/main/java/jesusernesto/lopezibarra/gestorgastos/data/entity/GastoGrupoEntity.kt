package jesusernesto.lopezibarra.gestorgastos.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "gasto_grupo",
    foreignKeys = [
        ForeignKey(
            entity = GrupoEntity::class,
            parentColumns = ["idGrupo"],
            childColumns = ["idGrupo"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["idUsuario"],
            childColumns = ["idUsuarioPago"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = CategoriaEntity::class,
            parentColumns = ["idCategoria"],
            childColumns = ["idCategoria"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("idGrupo"), Index("idUsuarioPago"), Index("idCategoria")]
)
data class GastoGrupoEntity(
    @PrimaryKey(autoGenerate = true)
    val idGastoGrupo: Int = 0,
    val idGrupo: Int,
    val idUsuarioPago: Int,
    val idCategoria: Int,
    val nombre: String,
    val monto: Float,
    val fecha: String,
    val createdAt: Long = System.currentTimeMillis()
)