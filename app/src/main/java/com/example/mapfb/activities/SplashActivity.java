package com.example.mapfb.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.mapfb.prefs.MapFB_Prefs;
import com.example.mapfb.R;
import com.example.mapfb.models.UserModel;

public class SplashActivity extends AppCompatActivity {

    MapFB_Prefs mapFB_prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        saveInitialUserData();
        moveToOptions();
    }

    private void moveToOptions(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent main = new Intent(SplashActivity.this,OptionsActivity.class);
                main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(main);
            }
        },3000);
    }

    private void saveInitialUserData(){
        mapFB_prefs = new MapFB_Prefs();

        UserModel driver = new UserModel("driver1","driver12345");
        UserModel student = new UserModel("student1","student12345");

        mapFB_prefs.saveUserModel(this,"driver",driver);
        mapFB_prefs.saveUserModel(this,"student",student);
    }
}
