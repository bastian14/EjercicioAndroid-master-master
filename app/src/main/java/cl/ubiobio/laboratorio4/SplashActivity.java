package cl.ubiobio.laboratorio4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    private static int TIME_SPLASH = 3000;
    private SplashActivity _this;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        _this = this;

        sp = getSharedPreferences(getString(R.string.sp_id), MODE_PRIVATE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                //si existe usuario ir al pantalla principal MainActivity....

                if(sp.getString("nombre", "").trim().length() > 0){
                    Intent principal = new Intent(_this, MainActivity.class);
                    startActivity(principal);
                    finish();
                }else {
                    Intent inicarLogin = new Intent(_this, LoginActivity.class);
                    startActivity(inicarLogin);
                    finish();
                }
            }
        }, TIME_SPLASH);
    }
}
