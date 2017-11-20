package ch.epfl.sweng.melody.database;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ch.epfl.sweng.melody.notification.NotificationHandler;


/**
 * Created by maxwell on 01.11.17.
 */

public class MelodyFirebaseMessagingService extends FirebaseMessagingService {

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        NotificationHandler.sendNotification(this, remoteMessage.getNotification().getBody());
    }
}

