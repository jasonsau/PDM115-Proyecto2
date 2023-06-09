package sv.edu.ues.fia.eisi.camaratrampa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if(!task.isSuccessful()){
                            Log.w(
                                    ContentValues.TAG,
                                    "Fetching FCM registration token failed",
                                    task.getException()
                            );
                            return;
                        }

                        String token = task.getResult();
                        String tokenGuardado = getSharedPreferences("SP_FILE", 0)
                                .getString("DEVICEID", null);
                        if(token != null) {
                            if(tokenGuardado== null || !tokenGuardado.equals(token)){
                                DeviceManager.registerDevice(token, MainActivity.this);
                                Toast.makeText(
                                        MainActivity.this,
                                        "Dispositivo registrado" + token,
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }

                    }
                });
    }
}