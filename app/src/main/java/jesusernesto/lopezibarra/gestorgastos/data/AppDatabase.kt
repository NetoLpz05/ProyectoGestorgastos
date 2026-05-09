package jesusernesto.lopezibarra.gestorgastos.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import jesusernesto.lopezibarra.gestorgastos.data.dao.AlertaDao
import jesusernesto.lopezibarra.gestorgastos.data.dao.CategoriaDao
import jesusernesto.lopezibarra.gestorgastos.data.dao.GrupoDao
import jesusernesto.lopezibarra.gestorgastos.data.dao.UsuarioDao
import jesusernesto.lopezibarra.gestorgastos.data.entity.UsuarioEntity
import jesusernesto.lopezibarra.gestorgastos.data.entity.CategoriaEntity
import jesusernesto.lopezibarra.gestorgastos.data.entity.MetodoPagoEntity
import jesusernesto.lopezibarra.gestorgastos.data.entity.GrupoEntity
import jesusernesto.lopezibarra.gestorgastos.data.entity.GastoGrupoEntity
import jesusernesto.lopezibarra.gestorgastos.data.entity.GastoEntity
import jesusernesto.lopezibarra.gestorgastos.data.entity.IngresoEntity
import jesusernesto.lopezibarra.gestorgastos.data.entity.PresupuestoEntity
import jesusernesto.lopezibarra.gestorgastos.data.entity.DetallePresupuestoEntity
import jesusernesto.lopezibarra.gestorgastos.data.entity.AlertaEntity
import jesusernesto.lopezibarra.gestorgastos.data.entity.UsuarioGrupoEntity
import jesusernesto.lopezibarra.gestorgastos.data.entity.DeudaGrupoEntity


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
    ], version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun grupoDao(): GrupoDao
    abstract fun alertaDao(): AlertaDao
    abstract fun categoriaDao(): CategoriaDao

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
