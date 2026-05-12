package jesusernesto.lopezibarra.gestorgastos.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "deuda_grupo",
    foreignKeys = [
        ForeignKey(
            entity = GastoGrupoEntity::class,
            parentColumns = ["idGastoGrupo"],
            childColumns = ["idGastoGrupo"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["idUsuario"],
            childColumns = ["idUsuario"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("idGastoGrupo"), Index("idUsuario")]
)
data class DeudaGrupoEntity(
    @PrimaryKey(autoGenerate = true)
    val idDeudaGrupo: Int = 0,
    val idGastoGrupo: Int,
    val idUsuario: Int,
    val montoDeuda: Double,
    val pagado: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)