package cl.ubiobio.laboratorio4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private TextInputLayout luser;
    private TextInputLayout lpass;
    private EditText user;
    private EditText pass;
    private Button register;

    private SharedPreferences sp;
    private SharedPreferences.Editor edit;
    private LoginActivity _this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        register = (Button)findViewById(R.id.register);
        register.setOnClickListener(this);

        sp = getSharedPreferences(getString(R.string.sp_id), MODE_PRIVATE);
        edit = sp.edit();

        _this = this;

        luser = findViewById(R.id.l_user);
        lpass = findViewById(R.id.l_pass);
        user = findViewById(R.id.user);
        pass = findViewById(R.id.pass);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

    }

    private void verificar(){
        boolean state = true;

        luser.setError(null);
        lpass.setError(null);

        final String user_str = user.getText().toString();
        final String pass_str = pass.getText().toString();

        if(user.getText().length()<=6){//restriccion user
            state = false;
            luser.setError("Tu nick o tu email debe tener más de 6 carácteres");
        }

        if(pass.getText().length()<=6){//restriccion pass
            state = false;
            lpass.setError("Tu contraseña debe tener más de 6 carácteres");
        }

        if(state==true/*state*/){
            login(user_str, pass_str);
        }
    }

    private void login(final String user, final String pass){
        //codigo de servicio web
        // si es exitoso guardar el las preferencias de usuario los datos, si no enviar un mensaje con Toast o
        /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

        Log.d("LOG WS", "entre");
        String WS_URL = "http://servicioswebmoviles.hol.es/index.php/LOGIN_UBB";
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
                            /*Log.d("LOG WS", "nombre: " + responseJson.getJSONObject("data").getString("nombres"));
                            Log.d("LOG WS", "email: " + responseJson.getJSONObject("data").getString("email"));
                            edit.putString("nombre",responseJson.getJSONObject("data").getString("nombres"));
                            edit.putString("email",responseJson.getJSONObject("data").getString("email"));
                            edit.commit();*/

                            if(responseJson.getBoolean("resp")){
                                //iniciar otra actividad.....
                                edit.putString("nombre",responseJson.getJSONObject("data").getString("nombres"));
                                edit.putString("email",responseJson.getJSONObject("data").getString("email"));
                                edit.commit();
                                startActivity(new Intent(_this, MainActivity.class));
                                _this.finish();

                            }else{
                                generateToast(responseJson.getString("info"));
                            }
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
                params.put("login",user);
                params.put("pass",pass);
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
        switch (v.getId()){
            case R.id.fab:
                verificar();
                break;

            case R.id.register:
                Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(register);
                break;
        }
    }
}
