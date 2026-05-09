package jesusernesto.lopezibarra.gestorgastos.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "usuario",
    indices = [Index(value = ["email"], unique = true)]
)
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val idUsuario: Int = 0,
    val nombre: String,
    val apellido: String,
    val email: String,
    val contrasena: String,
    val fechaNacimiento: String,
    val genero: String,
    val biometriaActiva: String = "INACTIVA",
    val tema: String = "CLARO",
    val createdAt: Long = System.currentTimeMillis()
)