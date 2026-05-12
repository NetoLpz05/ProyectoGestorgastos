package jesusernesto.lopezibarra.gestorgastos.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "grupo")
data class GrupoEntity(
    @PrimaryKey(autoGenerate = true)
    val idGrupo: Int = 0,
    val nombre: String,
    val tipo: String, //TU DECIDES SI USAS ENUM O NO WE
    val codigoInvitacion: String,
    val imagen: String,
    val createdAt: Long = System.currentTimeMillis()
)

