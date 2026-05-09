package jesusernesto.lopezibarra.gestorgastos.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
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
    val monto: Float,
    val descripcion: String,
    val fecha: String,
    val latitud: Float? = null,
    val longitud: Float? = null,
    val createdAt: Long = System.currentTimeMillis()
)


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
    val idUsuario: Int,
    val tipo: String,
    val ultimosDigitos: Int? = null,
    val nombre: String? = null
)


@Entity(tableName = "categoria")
data class CategoriaEntity(
    @PrimaryKey(autoGenerate = true)
    val idCategoria: Int = 0,
    val nombre: String,
    val predefinida: Boolean = false
)


@Entity(
    tableName = "presupuesto",
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
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("idUsuario"), Index("idCategoria")]
)
data class PresupuestoEntity(
    @PrimaryKey(autoGenerate = true)
    val idPresupuesto: Int = 0,
    val idUsuario: Int,
    val mes: Int,
    val anio: Int,
    val nombre: String,
    val montoTotal: Float,
    val createdAt: Long = System.currentTimeMillis(),
    val idCategoria: Int? = null
)


@Entity(
    tableName = "detalle_presupuesto",
    foreignKeys = [
        ForeignKey(
            entity = PresupuestoEntity::class,
            parentColumns = ["idPresupuesto"],
            childColumns = ["idPresupuesto"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoriaEntity::class,
            parentColumns = ["idCategoria"],
            childColumns = ["idCategoria"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("idPresupuesto"), Index("idCategoria")]
)
data class DetallePresupuestoEntity(
    @PrimaryKey(autoGenerate = true)
    val idDetallePresupuesto: Int = 0,
    val idPresupuesto: Int,
    val idCategoria: Int,
    val montoLimite: Float
)


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
    val limiteAlerta: Float,
    val activa: Boolean = true
)

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


@Entity(tableName = "grupo")
data class GrupoEntity(
    @PrimaryKey(autoGenerate = true)
    val idGrupo: Int = 0,
    val nombre: String,
    val tipo: String,
    val codigoInvitacion: String,
    val imagen: String,
    val createdAt: Long = System.currentTimeMillis()
)


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
    val montoDeuda: Float,
    val pagado: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)