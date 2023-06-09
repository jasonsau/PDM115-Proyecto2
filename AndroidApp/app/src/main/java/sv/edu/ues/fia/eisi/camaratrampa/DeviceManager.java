package sv.edu.ues.fia.eisi.camaratrampa;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DeviceManager {

    public static void registerDevice(String token, Context context){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://192.168.1.2:8001/Registrar.php";

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            Integer id = jsonObject.getInt("id");
                            if("success".equals(code)) {
                                context.getSharedPreferences("SP_FILE", 0)
                                        .edit()
                                        .putString("DEVICEID", token)
                                        .commit();
                                if(id!=0) {
                                    context.getSharedPreferences("SP_FILE", 0)
                                            .edit()
                                            .putInt("ID", id)
                                            .commit();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                        Toast.makeText(
                                context,
                                "Error al registrar el dispositivo",
                                Toast.LENGTH_LONG
                        )
                        .show();

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("Device", token);
                if(context.getSharedPreferences("SP_FILE", 0).getInt("DEVICEID", 0) != 0){
                    Integer val = context.getSharedPreferences("SP_FILE", 0)
                            .getInt("DEVICEID", 0);
                    params.put("id", val.toString());
                }
                return params;
            }
        };


        queue.add(stringRequest);

    }

}
