package ch.epfl.sweng.melody.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import ch.epfl.sweng.melody.MainActivity;
import ch.epfl.sweng.melody.R;

/**
 * Created by maxwell on 02.11.17.
 */

public class NotificationHandler {
    private static final int NOTIFICATION_CODE = 10;
    public static void sendNotification(Context context, String message){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),R.mipmap.app_logo);
        int smallIcon = R.mipmap.app_logo;
        Uri notificationSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIFICATION_CODE, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setLargeIcon(icon)
                        .setSmallIcon(smallIcon)
                        .setContentTitle("Melody")
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(notificationSound)
                        .setContentIntent(pendingIntent);
        notificationManager.notify(0,builder.build());
    }
}
