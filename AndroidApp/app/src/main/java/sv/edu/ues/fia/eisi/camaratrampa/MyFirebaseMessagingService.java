package sv.edu.ues.fia.eisi.camaratrampa;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService{

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        super.onMessageReceived(remoteMessage);

        String url = remoteMessage.getData().get("test");
        System.out.println(url);
        Intent intent = new Intent(this, ImageViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("selection", "save");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
