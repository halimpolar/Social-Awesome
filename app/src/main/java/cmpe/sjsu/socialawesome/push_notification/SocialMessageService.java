package cmpe.sjsu.socialawesome.push_notification;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by lam on 5/11/17.
 */

public class SocialMessageService extends FirebaseMessagingService {
    private static final String TAG = SocialMessageService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "The message receive: " + remoteMessage.getData());
    }
}
