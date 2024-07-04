package com.example.supletoriomovil;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    Button btnBDD;
    TextView tvInfo;
    String respuesta="";
    double temp,feels_like,temp_min,temp_max,pressure,sea_level,grnd_level,humidity,temp_kf;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBDD = findViewById(R.id.btnBDD);
        tvInfo = findViewById(R.id.tvInfo);

        ConsumirAPI();

        btnBDD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CrearTabla();
            }
        });
    }

    public void ConsumirAPI(){
        String url="https://api.openweathermap.org/data/2.5/forecast?lat=-5.206535001080812&lon=-80.6186554254348&appid=bd5e378503939ddaee76f12ad7a97608";
        OkHttpClient cliente = new OkHttpClient();
        Request get = new Request.Builder()
                .url(url)
                .build();
        cliente.newCall(get).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) {
                try {
                    ResponseBody responseBody = response.body();
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }else{

                        respuesta=responseBody.string();

                        // A TRAVÉS DEL USO DE HILOS PARALELAMENTE A LA CONSULTA DEL SERVIDOR MOSTRAMOS LA RESPUESTA
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() throws RuntimeException {
                                try {
                                    JSONObject json=new JSONObject(respuesta);

                                    JSONObject jsonResponse = new JSONObject(respuesta);

                                    JSONArray lista = jsonResponse.getJSONArray("list");

                                    for (int i = 0; i < lista.length(); i++) {
                                        JSONObject weatherItem = lista.getJSONObject(i);
                                        String dtTxt = weatherItem.getString("dt_txt");

                                        if ("2024-07-04 09:00:00".equals(dtTxt)) {
                                            JSONObject main = weatherItem.getJSONObject("main");

                                            temp = main.getDouble("temp");
                                            feels_like = main.getDouble("feels_like");
                                            temp_min = main.getDouble("temp_min");
                                            temp_max = main.getDouble("temp_max");
                                            pressure = main.getDouble("pressure");
                                            sea_level = main.getDouble("sea_level");
                                            grnd_level = main.getDouble("grnd_level");
                                            humidity = main.getDouble("humidity");
                                            temp_kf = main.getDouble("temp_kf");
                                            MainActivity.this.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    tvInfo.setText("Temperatura: " + temp + "\n" +
                                                    "feels_like: " + feels_like + "\n" +
                                                    "temp_min: " + temp_min + "\n" +
                                                    "temp_max: " + temp_max + "\n" +
                                                    "pressure: " + pressure + "\n" +
                                                    "sea_level: " + sea_level + "\n" +
                                                    "grnd_level: " + grnd_level + "\n" +
                                                    "humidity: " + humidity + "\n" +
                                                    "temp_kf: " + temp_kf + "\n"

                                                    );
                                                }
                                            });
                                            break;
                                        }
                                    }
                                Log.i("data", respuesta);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });}
                    Log.i("data", responseBody.string());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void CrearTabla() {
        com.example.supletoriomovil.BDHelper admin = new com.example.supletoriomovil.BDHelper(this, "SupleMovil.db", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        if (temp != 0 && feels_like != 0 && temp_min != 0 && temp_max != 0 && temp_min != 0 && pressure != 0 && sea_level != 0 && grnd_level != 0 && humidity != 0 && temp_kf != 0 ) {
            ContentValues datosRegistrar = new ContentValues();
            datosRegistrar.put("tempe", temp);
            datosRegistrar.put("feels_like", feels_like);
            datosRegistrar.put("temp_min", temp_min);
            datosRegistrar.put("temp_max", temp_max);
            datosRegistrar.put("pressure", pressure);
            datosRegistrar.put("sea_level", sea_level);
            datosRegistrar.put("grnd_level", grnd_level);
            datosRegistrar.put("humidity", humidity);
            datosRegistrar.put("temp_kf", temp_kf);

            bd.insert("SupleMovil", null, datosRegistrar);

            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_LONG).show();
        }
    }
}