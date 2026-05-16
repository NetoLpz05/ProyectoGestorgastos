package jesusernesto.lopezibarra.gestorgastos.data.Notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import jesusernesto.lopezibarra.gestorgastos.data.AppDatabase
import kotlinx.coroutines.flow.first
import java.util.Calendar

class PresupuestoCheckWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val prefs = context.getSharedPreferences("alertas_config", Context.MODE_PRIVATE)

            val alertasHabilitadas = prefs.getBoolean("habilitadas", true)
            if (!alertasHabilitadas) return Result.success()

            val umbral = prefs.getFloat("umbral", 0.8f)
            val idUsuario = prefs.getInt("id_usuario_actual", -1)
            if (idUsuario == -1) return Result.success()

            val db = AppDatabase.getInstance(context)
            val cal = Calendar.getInstance()
            val mes = cal.get(Calendar.MONTH) + 1
            val anio = cal.get(Calendar.YEAR)

            val presupuesto = db.presupuestoDao().obtenerPresupuesto(idUsuario, mes, anio)
                ?: return Result.success()

            val detalles = db.detallePresupuestoDao()
                .obtenerPorPresupuesto(presupuesto.idPresupuesto)
            if (detalles.isEmpty()) return Result.success()

            val listaCategorias = db.categoriaDao().obtenerTodas().first()

            val cal2 = Calendar.getInstance()
            cal2.set(anio, mes - 1, 1, 0, 0, 0)
            cal2.set(Calendar.MILLISECOND, 0)
            val inicio = cal2.timeInMillis
            cal2.set(Calendar.DAY_OF_MONTH, cal2.getActualMaximum(Calendar.DAY_OF_MONTH))
            cal2.set(Calendar.HOUR_OF_DAY, 23); cal2.set(Calendar.MINUTE, 59)
            cal2.set(Calendar.SECOND, 59); cal2.set(Calendar.MILLISECOND, 999)
            val fin = cal2.timeInMillis

            detalles.forEach { detalle ->
                val limite = detalle.montoLimite
                if (limite <= 0.0) return@forEach

                val gastado = db.gastoDao()
                    .obtenerGastoTotalPorCategoria(idUsuario, detalle.idCategoria, inicio, fin)
                    .first() ?: 0.0

                val porcentaje = (gastado / limite).toFloat()
                val nombreCat = listaCategorias
                    .find { it.idCategoria == detalle.idCategoria }
                    ?.nombre ?: "Categoría"

                val notifIdAlerta   = idUsuario * 1000 + detalle.idCategoria
                val notifIdExcedido = idUsuario * 1000 + detalle.idCategoria + 500

                when {
                    gastado > limite -> {
                        val yaNotificado = prefs.getBoolean("excedido_$notifIdExcedido", false)
                        if (!yaNotificado) {
                            Notificationhelper.enviarAlertaExcedido(
                                context   = context,
                                categoria = nombreCat,
                                gastado   = gastado,
                                limite    = limite,
                                notifId   = notifIdExcedido
                            )
                            prefs.edit()
                                .putBoolean("excedido_$notifIdExcedido", true)
                                .apply()
                        }
                    }
                    porcentaje >= umbral -> {
                        val yaNotificado = prefs.getBoolean("alerta_$notifIdAlerta", false)
                        if (!yaNotificado) {
                            Notificationhelper.enviarAlertaPresupuesto(
                                context         = context,
                                categoria       = nombreCat,
                                porcentajeUsado = (porcentaje * 100).toInt(),
                                gastado         = gastado,
                                limite          = limite,
                                notifId         = notifIdAlerta
                            )
                            prefs.edit()
                                .putBoolean("alerta_$notifIdAlerta", true)
                                .apply()
                        }
                    }
                    else -> {
                        prefs.edit()
                            .remove("alerta_$notifIdAlerta")
                            .remove("excedido_$notifIdExcedido")
                            .apply()
                    }
                }
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}