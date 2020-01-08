package com.example.mapfb.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mapfb.prefs.MapFB_Prefs;
import com.example.mapfb.R;
import com.example.mapfb.models.UserModel;

public class LoginActivity extends AppCompatActivity {
    TextView txtTitle,errorTxt;
    EditText username,password;
    MapFB_Prefs mapFB_prefs = new MapFB_Prefs();
    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setInitials();
    }

    public void onClickLogin(View view) {
        if (!validateEmptyFields() && userType.equals("driver")){
            UserModel checkDriver = mapFB_prefs.loadDataObject(this,"driver");
            if (checkDriver.getUsername().equals(username.getText().toString()) && checkDriver.getPassword().equals(password.getText().toString())){
                errorTxt.setText("");
                Intent driver = new Intent(this,DriversMapsActivity.class);
                startActivity(driver);
            }else{
                errorTxt.setText("Authentication Error! Check Credentials");
            }
        }else if (!validateEmptyFields() && userType.equals("student")){
            UserModel checkStudent = mapFB_prefs.loadDataObject(this,"student");
            if (checkStudent.getUsername().equals(username.getText().toString()) && checkStudent.getPassword().equals(password.getText().toString())){
                errorTxt.setText("");
                Intent student = new Intent(this,BusLineSelectorActivity.class);
                startActivity(student);
            }else{
                errorTxt.setText("Authentication Error! Check Credentials");
            }
        }
    }

    private void setInitials(){
//        cast the views
        txtTitle = findViewById(R.id.title_txt);
        errorTxt = findViewById(R.id.error_txt);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        Intent dataIntent = getIntent();
        if (dataIntent.getExtras()!=null){
            if (dataIntent.getExtras().getString("user").equals("Driver")){
                txtTitle.setText("Login Driver!");
                userType = "driver";
            }else if (dataIntent.getExtras().getString("user").equals("Student")){
                txtTitle.setText("Login Student!");
                userType = "student";
            }
        }
    }

    private boolean validateEmptyFields(){
        if (username.getText().toString().isEmpty()) {
            username.setError("Enter Username!");
            return true;
        }
        else if (password.getText().toString().isEmpty()) {
            password.setError("Enter Password!");
            return true;
        }
        else {
            username.setError(null);
            password.setError(null);
            return false;
        }
    }
}
