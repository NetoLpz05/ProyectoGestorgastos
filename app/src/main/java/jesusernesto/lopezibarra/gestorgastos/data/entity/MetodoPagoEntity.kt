package jesusernesto.lopezibarra.gestorgastos.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import jesusernesto.lopezibarra.gestorgastos.data.enums.TipoMetodoPago

@Entity(
    tableName = "metodo_pago",
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
data class MetodoPagoEntity(
    @PrimaryKey(autoGenerate = true)
    val idMetodoPago: Int = 0,
    val idUsuario: Int? = null,
    val tipo: TipoMetodoPago? = null,
    val ultimosDigitos: Int? = null,
    val nombre: String? = null
)