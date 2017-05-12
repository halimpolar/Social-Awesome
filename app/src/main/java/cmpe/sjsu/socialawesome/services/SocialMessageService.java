package cmpe.sjsu.socialawesome.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import cmpe.sjsu.socialawesome.StartActivity;

/**
 * Created by lam on 5/11/17.
 */

public class SocialMessageService extends FirebaseMessagingService {
    private static final String TAG = SocialMessageService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "The message receive: " + remoteMessage.getData());

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(android.R.mipmap.sym_def_app_icon)
                        .setContentTitle(remoteMessage.getData().get("title"))
                        .setContentText(remoteMessage.getData().get("body"));

        Intent intent = new Intent(this, StartActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        builder.setContentIntent(pIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        builder.setAutoCancel(true);
        mNotificationManager.notify(0, builder.build());
    }
}
