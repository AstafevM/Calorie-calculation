package com.adel5.caloriecalculation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

public class OutputActivity extends AppCompatActivity {

    private EditText osnovnoyObmenOutput, obshiyObmenOutput, proteinsOutput, fatsOutput, carbohydratesOutput;
    private ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        osnovnoyObmenOutput = findViewById(R.id.output_osnovnoy);
        obshiyObmenOutput = findViewById(R.id.output_obshiy);
        proteinsOutput = findViewById(R.id.output_proteins);
        fatsOutput = findViewById(R.id.output_fats);
        carbohydratesOutput = findViewById(R.id.output_carbohydrates);

        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(l -> finish());
    }

    @Override
    protected void onStart() {
        super.onStart();
        osnovnoyObmenOutput.setText(String.valueOf(Math.round(MainActivity.osnovnoyObmen)));
        obshiyObmenOutput.setText(String.valueOf(Math.round(MainActivity.obshiyObmen)));
        proteinsOutput.setText(String.valueOf(Math.round(MainActivity.proteins)));
        fatsOutput.setText(String.valueOf(Math.round(MainActivity.fats)));
        carbohydratesOutput.setText(String.valueOf(Math.round(MainActivity.carbohydrates)));
    }
}