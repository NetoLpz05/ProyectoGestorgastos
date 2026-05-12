package jesusernesto.lopezibarra.gestorgastos.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import jesusernesto.lopezibarra.gestorgastos.data.dao.*
import jesusernesto.lopezibarra.gestorgastos.data.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    ], version = 2, exportSchema = false
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

                lateinit var instance: AppDatabase

                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gestorgastos_db"
                )
                    .addCallback(object : RoomDatabase.Callback() {

                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            CoroutineScope(Dispatchers.IO).launch {

                                val dao = instance.categoriaDao()

                                if (dao.contarCategorias() == 0) {

                                    dao.insertarTodas(
                                        listOf(
                                            CategoriaEntity(nombre = "🏠 Vivienda", predefinida = true),
                                            CategoriaEntity(nombre = "🎬 Entretenimiento", predefinida = true),
                                            CategoriaEntity(nombre = "🚗 Transporte", predefinida = true),
                                            CategoriaEntity(nombre = "🍔 Alimentación", predefinida = true),
                                            CategoriaEntity(nombre = "❤️ Salud", predefinida = true),
                                            CategoriaEntity(nombre = "📦 Otros", predefinida = true)
                                        )
                                    )
                                }
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
