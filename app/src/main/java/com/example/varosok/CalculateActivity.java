package com.example.varosok;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CalculateActivity extends AppCompatActivity implements ListLoader.ListLoaderCallback, RequestCallback{
    private EditText calculateReproductionRateEditText;
    private EditText calculateCurrentInfectedEditText;
    private EditText calculateIdEditText;
    private Button calculateButton;
    private static int id;
    private static float rate;
    private static float currentInfected;
    private static int lakossag;
    private String name;
    private String url = "https://retoolapi.dev/xhsAsC/data";

    private List<City> updatedCities = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
        init();
        loadCityList();
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             boolean isValid = checkInput();
             if (isValid) {
                 calculate();

             }
             else {
                 Toast.makeText(CalculateActivity.this,"Rossz adatmegadás",Toast.LENGTH_SHORT).show();
             }
            }
        });
    }

    private void calculate() {
        double osztas = lakossag / currentInfected;
        double eredmeny = Math.log(osztas) / Math.log(rate);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Total infection")
                .setMessage("The chosen city is: " + name + "\nPopulation is: " + lakossag + "\n\nTotal infection happens in  " + eredmeny + "generations")
                .setIcon(R.drawable.skull);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                Intent intent = new Intent(CalculateActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private boolean checkInput() {
        id = Integer.parseInt(String.valueOf(calculateIdEditText.getText().toString()));
        rate = Float.parseFloat(String.valueOf(calculateReproductionRateEditText.getText().toString()));
        currentInfected = Float.parseFloat(String.valueOf(calculateCurrentInfectedEditText.getText().toString()));
        for (City city : updatedCities) {
            if (city.getId() == id) {
                name = city.getName();
                lakossag = city.getPopulation();
            if(currentInfected >1 && currentInfected< lakossag*0.3) {
                if(rate > 1.1 && rate < 3) {
                    return true;
                }
                }
            }
        }
        return false;
    }
    private void loadCityList() {
        ListLoader.ListLoaderclass listLoader = new ListLoader.ListLoaderclass(url, this);
        listLoader.execute();
    }
    private void init(){
        calculateReproductionRateEditText = findViewById(R.id.calculateReproductionRateEditText);
        calculateCurrentInfectedEditText = findViewById(R.id.calculateCurrentInfectedEditText);
        calculateButton = findViewById(R.id.calculateButton);
        calculateIdEditText = findViewById(R.id.calculateIdEditText);
    }

    @Override
    public void onListLoaded(List<City> cities, int lastId) {
        updatedCities.addAll(cities);
        }

    @Override
    public void onSuccess(Response response, List<City> updatedCities) {

    }

    @Override
    public void onFailure(IOException e) {

    }
}