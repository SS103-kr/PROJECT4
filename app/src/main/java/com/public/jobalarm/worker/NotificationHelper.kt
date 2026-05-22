package com.jobalarm.worker

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.jobalarm.MainActivity
import com.jobalarm.R
import com.jobalarm.util.Constants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor() {

    fun show(context: Context, orgName: String, title: String, recrutPbancSn: String) {
        val deeplink = Uri.parse("${Constants.DEEPLINK_URI_BASE}$recrutPbancSn")
        val contentIntent = Intent(Intent.ACTION_VIEW, deeplink, context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            recrutPbancSn.hashCode(),
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, Constants.NOTIF_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setContentTitle("[$orgName] 새 채용공고")
            .setContentText(title)
            .setStyle(NotificationCompat.BigTextStyle().bigText(title))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setGroup(Constants.NOTIF_GROUP_KEY)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val summary = NotificationCompat.Builder(context, Constants.NOTIF_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setContentTitle(context.getString(R.string.notif_channel_name))
            .setStyle(NotificationCompat.InboxStyle())
            .setGroup(Constants.NOTIF_GROUP_KEY)
            .setGroupSummary(true)
            .setAutoCancel(true)
            .build()

        val nm = NotificationManagerCompat.from(context)
        try {
            nm.notify(recrutPbancSn.hashCode(), notification)
            nm.notify(Constants.NOTIF_GROUP_KEY.hashCode(), summary)
        } catch (_: SecurityException) {
            // POST_NOTIFICATIONS permission not granted; silently drop.
        }
    }
}
