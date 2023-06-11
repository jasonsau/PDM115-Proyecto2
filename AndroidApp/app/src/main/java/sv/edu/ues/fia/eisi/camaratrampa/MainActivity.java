package sv.edu.ues.fia.eisi.camaratrampa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String BROKER_URL = "tcp://192.168.1.10:1883";
    private static final String CLIENT_ID = "ciente12342";
    private MqttHandler mqttHandler;
    private MqttClient mqttClient;
    private Button btnSendMessage;
    private List<String> urls;
    private List<ImageEntity> imageEntities;
    private ControlDB controlDB;
    private RecyclerView recyclerView;

    private CardAdapter cardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        mqttHandler = new MqttHandler();
        mqttHandler.connect(BROKER_URL, CLIENT_ID);
        mqttClient = mqttHandler.getMqttClient();
        btnSendMessage = (Button) findViewById(R.id.btn_send_message);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
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

                });
        String valor = getIntent().getStringExtra("test");
        if(valor!=null) {
            Intent intent = new Intent(this, ImageViewActivity.class);
            intent.putExtra("url", valor);
            intent.putExtra("selection", "save");
            startActivity(intent);
        }

        btnSendMessage.setOnClickListener(view -> {
            publishMessage("camara_trampa", "Hola desde Android");
        });
        if(mqttClient!=null) {
            if(mqttClient.isConnected()) {
                handler(this);
                try {
                    mqttClient.subscribe("camara_trampa");
                } catch (MqttException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mqttHandler.disconnect();
    }

    private void publishMessage(String topic, String message) {
        mqttHandler.publish(topic, message);
    }

    public void handler(Context context) {
        mqttClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                System.out.println(cause);
                System.out.println("Connection lost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                runOnUiThread(() -> {
                    Toast.makeText(
                            context,
                            new String(message.toString() + " " + topic),
                            Toast.LENGTH_LONG
                    ).show();
                });
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(imageEntities != null) {
            System.out.println("Entra al if");
            imageEntities.clear();
            imageEntities.addAll(controlDB.selectImage());
            cardAdapter.setItems(imageEntities);
            recyclerView.setAdapter(cardAdapter);

        }
    }

    public void init() {
        controlDB = new ControlDB(this);
        controlDB.open();
        imageEntities = controlDB.selectImage();
        cardAdapter = new CardAdapter(this, imageEntities);
        recyclerView = findViewById(R.id.list_images);
        cardAdapter.setOnClickListener(view -> {
            Intent intent = new Intent(this, ImageViewActivity.class);
            intent.putExtra(
                    "url",
                    imageEntities.get(recyclerView.getChildAdapterPosition(view)).getUrlFoto()
            );
            intent.putExtra("selection", "view");
            startActivity(intent);
        });
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(cardAdapter);
    }
}