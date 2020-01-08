package com.example.mapfb.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.mapfb.R;


public class BusSelectedActivity extends AppCompatActivity {

    String busType;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_selected);

        title = findViewById(R.id.bus_type_title);
        Intent busSelectedIntent = getIntent();
        if (busSelectedIntent.getExtras() != null){
            if (busSelectedIntent.getExtras().getString("busType").equals("blue")) {
                title.setText("Blue Line");
                busType = "blue";
            } else if (busSelectedIntent.getExtras().getString("busType").equals("purple")) {
                title.setText("Purple Line");
                busType = "purple";
            } else if (busSelectedIntent.getExtras().getString("busType").equals("orange")) {
                title.setText("Orange Line");
                busType = "orange";
            } else if (busSelectedIntent.getExtras().getString("busType").equals("green")) {
                title.setText("Green Line");
                busType = "green";
            }
        }

    }

    public void onClickOpenClientActivity(View view) {
        Intent student = new Intent(this,ClientsMapsActivity.class);
        student.putExtra("busColor",busType);
        startActivity(student);
    }

    public void onClickPointStop(View view) {
        Intent student = new Intent(this,PointStopActivity.class);
        startActivity(student);
    }

    public void onClickPointTiming(View view) {
        Intent student = new Intent(this,PointTimingActivity.class);
        startActivity(student);
    }
}
