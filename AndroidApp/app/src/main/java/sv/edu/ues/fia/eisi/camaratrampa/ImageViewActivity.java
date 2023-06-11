package sv.edu.ues.fia.eisi.camaratrampa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;

public class ImageViewActivity extends AppCompatActivity {
    ImageView imageView;

    private ControlDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        db = new ControlDB(this);

        String url = getIntent().getExtras().getString("url");
        String action = getIntent().getExtras().getString("selection");
        System.out.println("Valor de action");
        System.out.println(action);

        imageView = findViewById(R.id.my_image);

        if(url != null) {
            Picasso.get().load(url)
                    .error(R.drawable.ic_launcher_background)
                    .into(imageView);
            if(action != null && !action.equals("view")) {
                System.out.println("Insertando imagen");
                ImageEntity imageEntity = new ImageEntity();
                imageEntity.setUrlFoto(url);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    imageEntity.setFechaFoto(LocalDateTime.now().toString());
                    imageEntity.setNombreFoto("Foto de prueba" + LocalDateTime.now().toString());
                }
                db.open();
                db.insertarImage(imageEntity);
                db.close();
            }

        }
    }
}