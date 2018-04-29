package cl.ubiobio.laboratorio4;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button registrar;
    private EditText run;
    private EditText nombres;
    private EditText apellidos;
    private EditText email;
    private EditText pass;
    private EditText nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registrar = findViewById(R.id.registrar);
        run = findViewById(R.id.run);
        nombres=findViewById(R.id.nombres);
        apellidos=findViewById(R.id.apellidos);
        email=findViewById(R.id.email);
        pass=findViewById(R.id.pas);
        nickname=findViewById(R.id.nick);
        registrar.setOnClickListener(this);
    }

    private void serviceWebRegister(final String run, final String nombres,final String apellidos,final String email,final String pass,final String nickname ){
        Log.d("LOG WS", "entre");
        String WS_URL = "http://servicioswebmoviles.hol.es/index.php/REGISTER_UBB";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(
                Request.Method.POST,
                WS_URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Log.d("LOG WS", response);
                        try {
                            JSONObject responseJson = new JSONObject(response);
                            Log.d("LOG WS", "Response: " + response);
                            /*Log.d("LOG WS", "email: " + responseJson.getJSONObject("data").getString("email"));
                            Log.d("LOG WS", "run: " + responseJson.getJSONObject("data").getString("run"));
                            Log.d("LOG WS", "apellidos: " + responseJson.getJSONObject("data").getString("apellidos"));
                            Log.d("LOG WS", "nickname: " + responseJson.getJSONObject("data").getString("nickname"));
                            Log.d("LOG WS", "pass: " + responseJson.getJSONObject("data").getString("pass"));

                            generateToast(responseJson.getString("info"));
                            if(responseJson.getBoolean("resp")){
                                //iniciar otra actividad.....
                            }else{
                            }*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("LOG WS", error.toString());
                generateToast("Error en el WEB Service");
            }
        }
        ){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("run",run);
                params.put("nombres",nombres);
                params.put("apellidos",apellidos);
                params.put("email",email);
                params.put("pass",pass);
                params.put("nickname",nickname);
                return params;
            }
        };
        requestQueue.add(request);
    }


    private void generateToast(String msg){
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        final String runtext = run.getText().toString();
        final String nombrestext = nombres.getText().toString();
        final String apellidostext = apellidos.getText().toString();
        final String emailtext = email.getText().toString();
        final String passtext = pass.getText().toString();
        final String nicktext = nickname.getText().toString();
        serviceWebRegister(runtext,nombrestext,apellidostext,emailtext,passtext,nicktext);
        Intent registrar = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(registrar);
    }
}
