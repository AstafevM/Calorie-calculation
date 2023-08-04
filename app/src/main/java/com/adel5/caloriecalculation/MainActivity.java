package com.adel5.caloriecalculation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    public static float rashodOtdyha, osnovnoyObmen, obshiyObmen, proteins, fats, carbohydrates;
    private EditText ageInput, weightInput, heightInput, fatPercentInput, sleepInput, lightActivityInput, lightWorkInput, middleHardWorkInput, hardPhysicalWorkInput, veryHardPhysicalWorkInput;
    private Spinner genderInput, lifestyleInput;
    private final String[] genders = {" ", "мужчина", "женщина"}, lifestyles = {" ", "сидячий", "условно активный", "активный", "спортивный", "верхняя безопасная планка"};
    private AppCompatButton calculate;
    private FloatingActionButton resetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ageInput = findViewById(R.id.output_obshiy);
        weightInput = findViewById(R.id.input_weight);
        heightInput = findViewById(R.id.input_height);
        genderInput = findViewById(R.id.input_gender);
        fatPercentInput = findViewById(R.id.input_fats);
        lifestyleInput = findViewById(R.id.input_lifestyle);
        sleepInput = findViewById(R.id.input_sleep);
        lightActivityInput = findViewById(R.id.input_light_activity);
        lightWorkInput = findViewById(R.id.input_light_work);
        middleHardWorkInput = findViewById(R.id.input_middle_hard_work);
        hardPhysicalWorkInput = findViewById(R.id.input_hard_physical_work);
        veryHardPhysicalWorkInput = findViewById(R.id.input_very_hard_physical_work);

        calculate = findViewById(R.id.calcBtn);

        resetBtn = findViewById(R.id.reset_btn);

        CustomAdapter adapter = new CustomAdapter(this, R.drawable.spinner_dropdown, genders);
        genderInput.setAdapter(adapter);

        CustomAdapter adapter2 = new CustomAdapter(this, R.drawable.spinner_dropdown, lifestyles);
        lifestyleInput.setAdapter(adapter2);

        Intent intent = new Intent(this, OutputActivity.class);

        calculate.setOnClickListener(view -> {
            float age, height, weight, fatPercent, sleep, lightActivity, lightWork, middleHardWork, hardPhysicalWork, veryHardPhysicalWork;
            String gender = genderInput.getSelectedItem().toString(), lifestyle = lifestyleInput.getSelectedItem().toString();

            try {
                age = Float.parseFloat(ageInput.getText().toString());
                height = Float.parseFloat(heightInput.getText().toString());
                weight = Float.parseFloat(weightInput.getText().toString());
                fatPercent = Float.parseFloat(fatPercentInput.getText().toString());
                sleep = sleepInput.getText().toString().isEmpty() ? 0 : Float.parseFloat(sleepInput.getText().toString());
                lightActivity = lightActivityInput.getText().toString().isEmpty() ? 0 : Float.parseFloat(lightActivityInput.getText().toString());
                lightWork = lightWorkInput.getText().toString().isEmpty() ? 0 : Float.parseFloat(lightWorkInput.getText().toString());
                middleHardWork = middleHardWorkInput.getText().toString().isEmpty() ? 0 : Float.parseFloat(middleHardWorkInput.getText().toString());
                hardPhysicalWork = hardPhysicalWorkInput.getText().toString().isEmpty() ? 0 : Float.parseFloat(hardPhysicalWorkInput.getText().toString());
                veryHardPhysicalWork = veryHardPhysicalWorkInput.getText().toString().isEmpty() ? 0 : Float.parseFloat(veryHardPhysicalWorkInput.getText().toString());
            } catch (NumberFormatException exception) {
                Toast.makeText(this, "Введите корректное значение.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (gender.equals(" ") || lifestyle.equals(" ")) {
                Toast.makeText(this, "Введите корректное значение.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (sleep + lightActivity + lightWork + middleHardWork + hardPhysicalWork + veryHardPhysicalWork != 24) {
                Toast.makeText(this, "Дневная активность должна быть равна 24 часам.", Toast.LENGTH_LONG).show();
                return;
            }

            //начало расчётов
            float notFatPercent = 1 - (fatPercent / 100), //процент тощей массы
                  leanMass = weight * notFatPercent, //тощая масса
                  fatMass = weight - leanMass; //жировая масса

            switch (gender) { //расчёт основного обмена и жиров в зависимости от пола
                case "мужчина":
                    osnovnoyObmen = (float) (66.473 + (13.7516 * leanMass) + (5.0033 * height) - (6.755 * age));
                    fats = (float) (1.2 * leanMass * 0.7);
                    break;
                case "женщина":
                    osnovnoyObmen = (float) (655.0955 + (9.5634 * leanMass) + (1.8496 * height) - (4.6756 * age));
                    fats = (float) (1.2 * leanMass * 1);
                    break;
            }

            rashodOtdyha = osnovnoyObmen / 24;

            //расчёт общего обмена
            if (sleep + lightActivity + lightWork + middleHardWork + hardPhysicalWork + veryHardPhysicalWork == 24)
                obshiyObmen = (float) (sleep * rashodOtdyha + 1.4 * lightActivity * rashodOtdyha + 1.6 * lightWork * rashodOtdyha + 1.9 * middleHardWork * rashodOtdyha + 2.2 * hardPhysicalWork * rashodOtdyha + 2.5 * veryHardPhysicalWork * rashodOtdyha + 4.5 * fatMass);

            switch (lifestyle) { //расчёт белков
                case "сидячий":
                    proteins = (float) (1.1 * leanMass * 0.8);
                    break;
                case "условно активный":
                    proteins = (float) (1.1 * leanMass * 1.2);
                    break;
                case "активный":
                    proteins = (float) (1.1 * leanMass * 1.8);
                    break;
                case "спортивный":
                    proteins = (float) (1.1 * leanMass * 2);
                    break;
                case "верхняя безопасная планка":
                    proteins = (float) (1.1 * leanMass * 2.5);
                    break;
            }

            carbohydrates = (float) ((obshiyObmen - proteins * 4.2 - fats * 9.29) / 3.9); //расчёт углеводов

            startActivity(intent);
        });

        resetBtn.setOnClickListener(l -> {
            ageInput.setText("");               sleepInput.setText("");
            heightInput.setText("");            lightActivityInput.setText("");
            weightInput.setText("");            lightWorkInput.setText("");
            genderInput.setSelection(0);        middleHardWorkInput.setText("");
            lifestyleInput.setSelection(0);     hardPhysicalWorkInput.setText("");
            fatPercentInput.setText("");        veryHardPhysicalWorkInput.setText("");
        });
    }
}