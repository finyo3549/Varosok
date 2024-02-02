package com.example.varosok;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class updateActivity extends AppCompatActivity implements ListLoader.ListLoaderCallback, RequestCallback {
    private ProgressBar updateActivityProgressBar;
    private EditText updateActivityId;
    private EditText updateActivityNev;
    private EditText updateActivityOrszag;
    private EditText updateActivityLakossag;
    private Button updateActivityUpdateButton;
    private Button updateActivityBackButton;
    private String url = "https://retoolapi.dev/xhsAsC/data";
    private List<City> updatedCities = new ArrayList<>();
    private static int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        init();
        getVariableFromIntent();
        loadCityList();
        TextWatcher textWatcher = null;

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                String idString = updateActivityId.getText().toString();
                String nev = updateActivityNev.getText().toString();
                String orszag = updateActivityOrszag.getText().toString();
                String lakossagString = updateActivityLakossag.getText().toString();

                boolean allFieldsSet = !idString.isEmpty() && !nev.isEmpty() && !orszag.isEmpty() && !lakossagString.isEmpty();

                updateActivityUpdateButton.setEnabled(allFieldsSet);
            }
        };
        updateActivityId.addTextChangedListener(textWatcher);
        updateActivityNev.addTextChangedListener(textWatcher);
        updateActivityOrszag.addTextChangedListener(textWatcher);
        updateActivityLakossag.addTextChangedListener(textWatcher);

        String idString = updateActivityId.getText().toString();
        String nev = updateActivityNev.getText().toString();
        String orszag = updateActivityOrszag.getText().toString();
        String lakossagString = updateActivityLakossag.getText().toString();
        if (nev != null && orszag != null && lakossagString != null) {
            updateActivityUpdateButton.setEnabled(true);
        }
        updateActivityUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer lakossag = null;
                String idString = updateActivityId.getText().toString();
                String nev = updateActivityNev.getText().toString();
                String orszag = updateActivityOrszag.getText().toString();
                String lakossagString = updateActivityLakossag.getText().toString();
                lakossag = Integer.parseInt(lakossagString);

                City city = new City(id, nev, orszag, lakossag);
                Gson jsonConverter = new Gson();
                RequestTask task = new RequestTask(url + "/" + id, "PUT", jsonConverter.toJson(city),(RequestCallback) updateActivity.this,updateActivity.this);
                task.execute();
            }
        });
        updateActivityBackButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(updateActivity.this, ListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getVariableFromIntent() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("id")) {
            id = intent.getIntExtra("id", 0);
            updateActivityId.setText(String.valueOf(id));
        }
    }

    private void loadCityList() {
        ListLoader.ListLoaderclass listLoader = new ListLoader.ListLoaderclass(url, this);
        listLoader.execute();
    }

    public void init() {
        updateActivityId = findViewById(R.id.updateActivityId);
        updateActivityProgressBar = findViewById(R.id.updateActivityProgressBar);
        updateActivityNev = findViewById(R.id.updateActivityNev);
        updateActivityOrszag = findViewById(R.id.updateActivityOrszag);
        updateActivityLakossag = findViewById(R.id.updateActivityLakossag);
        updateActivityUpdateButton = findViewById(R.id.updateActivityUpdateButton);
        updateActivityBackButton = findViewById(R.id.updateActivityBackButton);

    }

    @Override
    public void onListLoaded(List<City> cities, int lastId) {
        updatedCities.addAll(cities);

        for (City city : updatedCities) {
            if (city.getId() == id) {
                updateActivityNev.setText(city.getName());
                updateActivityOrszag.setText(city.getOrszag());
                updateActivityLakossag.setText(String.valueOf(city.getPopulation()));
                break;
            }
        }
    }

    @Override
    public void onSuccess(Response response, List<City> updatedCities) {
        Intent intent = new Intent(updateActivity.this,ListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFailure(IOException e) {

    }
}