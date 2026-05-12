package jesusernesto.lopezibarra.gestorgastos.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import jesusernesto.lopezibarra.gestorgastos.data.dao.*
import jesusernesto.lopezibarra.gestorgastos.data.entity.*

@Database(
    entities = [
        UsuarioEntity::class,
        CategoriaEntity::class,
        MetodoPagoEntity::class,
        GrupoEntity::class,
        GastoGrupoEntity::class,
        GastoEntity::class,
        IngresoEntity::class,
        PresupuestoEntity::class,
        DetallePresupuestoEntity::class,
        AlertaEntity::class,
        UsuarioGrupoEntity::class,
        DeudaGrupoEntity::class,
        GastoFijoEntity::class
    ], version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun grupoDao(): GrupoDao
    abstract fun alertaDao(): AlertaDao
    abstract fun categoriaDao(): CategoriaDao
    abstract fun presupuestoDao(): PresupuestoDao
    abstract fun detallePresupuestoDao(): DetallePresupuestoDao
    abstract fun gastoFijoDao(): GastoFijoDao
    abstract fun gastoDao(): GastoDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gestorgastos_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
