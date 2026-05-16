package jesusernesto.lopezibarra.gestorgastos.data.Notifications

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object NotificationScheduler {

    private const val WORK_NAME = "presupuesto_check"

    fun programar(context: Context) {
        val request = PeriodicWorkRequestBuilder<PresupuestoCheckWorker>(
            repeatInterval = 6,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(false)
                    .build()
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    fun cancelar(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
    }

    fun ejecutarAhora(context: Context) {
        val request = androidx.work.OneTimeWorkRequestBuilder<PresupuestoCheckWorker>()
            .build()
        WorkManager.getInstance(context).enqueue(request)
    }
}
