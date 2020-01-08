package com.example.mapfb.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mapfb.R;

public class BusLineSelectorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_line_selector);
    }

    public void onClickBlueLine(View view) {
        Intent main  = new Intent(this,BusSelectedActivity.class);
        main.putExtra("busType","blue");
        startActivity(main);
    }

    public void onClickPurpleLine(View view) {
        Intent main  = new Intent(this,BusSelectedActivity.class);
        main.putExtra("busType","purple");
        startActivity(main);
    }

    public void onClickOrangeLine(View view) {
        Intent main  = new Intent(this,BusSelectedActivity.class);
        main.putExtra("busType","orange");
        startActivity(main);
    }

    public void onClickGreenLine(View view) {
        Intent main  = new Intent(this,BusSelectedActivity.class);
        main.putExtra("busType","green");
        startActivity(main);
    }
}
