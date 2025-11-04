package com.example.davinciconnect.ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.example.davinciconnect.R;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "calendar_events_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        String eventDescription = intent.getStringExtra("event_description");
        if (eventDescription == null) {
            eventDescription = "Tienes un evento pendiente.";
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // En Android 8.0+ es OBLIGATORIO crear un canal
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Recordatorios de Calendario",
                    NotificationManager.IMPORTANCE_HIGH // <-- ¡Prioridad ALTA!
            );
            channel.setDescription("Notificaciones para los eventos del calendario");
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{0, 500, 250, 500});
            channel.setSound(defaultSoundUri, null);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC); // <-- ¡Hacerla PÚBLICA!
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm) // Icono estándar y seguro
                .setContentTitle("Recordatorio de Evento")
                .setContentText(eventDescription)
                .setPriority(NotificationCompat.PRIORITY_HIGH) // Prioridad ALTA para versiones antiguas de Android
                .setVibrate(new long[]{0, 500, 250, 500}) // Patrón de vibración
                .setSound(defaultSoundUri)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) // Visibilidad para versiones antiguas
                .setAutoCancel(true);

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
