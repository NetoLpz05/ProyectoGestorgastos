package jesusernesto.lopezibarra.gestorgastos.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "usuario_grupo",
    primaryKeys = ["idUsuarioGrupo"],
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
            childColumns = ["idUsuario"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("idGrupo"), Index("idUsuario")]
)

data class UsuarioGrupoEntity(
    val idUsuarioGrupo: Int = 0,
    val idGrupo: Int,
    val idUsuario: Int,
    val fechaUnido: Long = System.currentTimeMillis()
)