package com.wildan.githubusersubmission3.settings

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.wildan.githubusersubmission3.R
import com.wildan.githubusersubmission3.SplashActivity
import com.wildan.githubusersubmission3.dataObject.Functions.setToast
import java.util.*

class Reminder : BroadcastReceiver() {

    companion object {
        const val EXTRA_MESSAGE = "extra_message"
        const val REMINDER_ID = 1
    }

    override fun onReceive(context: Context, intent: Intent) {
        val title = context.resources.getString(R.string.app_name)
        val message = intent.getStringExtra(EXTRA_MESSAGE)
        showReminderNotification(context, title, message ?: "", REMINDER_ID)
    }

    private fun showReminderNotification(
        context: Context,
        title: String,
        message: String,
        notifId: Int
    ) {

        val CHANNEL_ID = "Channel_1"
        val CHANNEL_NAME = "Reminder channel"
        val notifyIntent = Intent(context, SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val notifyPendingIntent = PendingIntent.getActivity(
            context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notifSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24_black)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(notifSound)
            .setContentIntent(notifyPendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            builder.setChannelId(CHANNEL_ID)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(notifId, notification)

    }

    fun setRepeatingReminder(context: Context, message: String) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, Reminder::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)
        val time = Calendar.getInstance()
        time.set(Calendar.HOUR_OF_DAY, 9)
        time.set(Calendar.MINUTE, 0)
        time.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, REMINDER_ID, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            time.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        setToast(context.resources.getString(R.string.reminderset), context as FragmentActivity)
    }

    fun cancelReminder(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, Reminder::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, REMINDER_ID, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        setToast(context.resources.getString(R.string.reminderoff), context as FragmentActivity)
    }
}
