package ba.unsa.etf.rma.cineaste

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LatestService : Service() {
    private var wakeLock: PowerManager.WakeLock? = null
    private var serviceStarted = false

    override fun onBind(intent: Intent): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        startForeground(Constants.NOTIFICATION_ID_LATEST_MOVIE, createNotification())
    }

    private fun createNotification(): Notification {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(NotificationChannel(
            Constants.CHANNEL_ID_LATEST_MOVIE,
            "Latest movie notifications channel",
            NotificationManager.IMPORTANCE_HIGH
        ).let {
            it.description = "Latest movie service channel"
            it.enableLights(true)
            it.lightColor = Color.RED
            it.enableVibration(true)
            it.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            it
        })

        val builder = Notification.Builder(this, Constants.CHANNEL_ID_LATEST_MOVIE)
        return builder
            .setContentTitle("Finding latest movie")
            .setSmallIcon(android.R.drawable.ic_popup_sync)
            .setTicker("Movie")
            .build()
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        startService()
        return START_STICKY
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun startService() {
        if (serviceStarted) return
        serviceStarted = true

        wakeLock = (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
            newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "LatestMovieService::lock").apply { acquire() }
        }

        GlobalScope.launch(Dispatchers.IO) {
            while (serviceStarted) {
                launch(Dispatchers.IO) {
                    when (val result = TmdbApiCalls.latestMovieRequest()) {
                        is Result.Success<Movie> -> latestDone(result.data)
                        else -> Log.e("TAG67", "Latest error")
                    }
                }

                delay(3600000)
            }
        }
    }

    private fun latestDone(movie: Movie) {
        val intent = Intent(this, LatestActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("movie", movie)
        }

        val pendingIntent = PendingIntent
            .getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(baseContext, Constants.CHANNEL_ID_LATEST_MOVIE).apply{
            setSmallIcon(android.R.drawable.stat_notify_sync)
            setContentTitle("New movie found")
            setContentText(movie.title)
            setContentIntent(pendingIntent)
            setOngoing(false)
            build()
        }

        with(NotificationManagerCompat.from(applicationContext)) {
            if (ActivityCompat.checkSelfPermission(baseContext, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) return

            notify(Constants.NOTIFICATION_ID_LATEST_MOVIE, notification.build())
        }
    }
}