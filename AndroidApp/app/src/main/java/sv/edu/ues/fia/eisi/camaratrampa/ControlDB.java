package sv.edu.ues.fia.eisi.camaratrampa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ControlDB {
    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public ControlDB(Context context) {
        this.context = context;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, "camaratrampa", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS imagen (id INTEGER PRIMARY KEY AUTOINCREMENT, nombreFoto TEXT, fechaFoto TEXT, urlFoto TEXT, idFoto TEXT)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE imagen");
            onCreate(db);
        }
    }

    public void open() {
        db = DBHelper.getWritableDatabase();
    }

    public void close() {
            DBHelper.close();
    }

    public Long insertarImage(ImageEntity imageEntity) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("nombreFoto", imageEntity.getNombreFoto());
        initialValues.put("fechaFoto", imageEntity.getFechaFoto());
        initialValues.put("urlFoto", imageEntity.getUrlFoto());
        return db.insert("imagen", null, initialValues);
    }

    public List<ImageEntity> selectImage() {
        Cursor cursor = db.rawQuery("SELECT * FROM imagen", null);
        List<ImageEntity> imageEntities = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                ImageEntity imageEntity = new ImageEntity();
                imageEntity.setIdFoto(cursor.getString(0));
                imageEntity.setNombreFoto(cursor.getString(1));
                imageEntity.setFechaFoto(cursor.getString(2));
                imageEntity.setUrlFoto(cursor.getString(3));
                imageEntities.add(imageEntity);
            } while (cursor.moveToNext());
        }
        return imageEntities;
    }
}
