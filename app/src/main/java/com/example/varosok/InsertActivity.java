package com.example.varosok;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InsertActivity extends AppCompatActivity implements ListLoader.ListLoaderCallback {
    private EditText ujAdatFelveteleId;
    private EditText ujAdatFelveteleNev;
    private EditText ujAdatFelveteleOrszag;
    private EditText ujAdatFelveteleLakossag;
    private Button ujAdatFelveteleButton;
    private Button ujAdatVisszaButton;
    private ProgressBar ujAdatFelveteleProgressBar;
    private String url = "https://retoolapi.dev/xhsAsC/data";

    private void loadCityList() {
        ListLoader.ListLoaderclass listLoader = new ListLoader.ListLoaderclass(url, this);
        listLoader.execute();
    }

    @Override
    public void onListLoaded(List<City> cities, int lastId) {
        int newId = lastId + 1;
        ujAdatFelveteleId.setText(String.valueOf(newId));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        init();
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

                String idString = ujAdatFelveteleId.getText().toString();
                String nev = ujAdatFelveteleNev.getText().toString();
                String orszag = ujAdatFelveteleOrszag.getText().toString();
                String lakossagString = ujAdatFelveteleLakossag.getText().toString();

                boolean allFieldsSet = !idString.isEmpty() && !nev.isEmpty() && !orszag.isEmpty() && !lakossagString.isEmpty();

                ujAdatFelveteleButton.setEnabled(allFieldsSet);
            }
        };
        ujAdatFelveteleId.addTextChangedListener(textWatcher);
        ujAdatFelveteleNev.addTextChangedListener(textWatcher);
        ujAdatFelveteleOrszag.addTextChangedListener(textWatcher);
        ujAdatFelveteleLakossag.addTextChangedListener(textWatcher);

        String idString = ujAdatFelveteleId.getText().toString();
        String nev = ujAdatFelveteleNev.getText().toString();
        String orszag = ujAdatFelveteleOrszag.getText().toString();
        String lakossagString = ujAdatFelveteleLakossag.getText().toString();
        if (nev != null && orszag != null && lakossagString != null) {
            ujAdatFelveteleButton.setEnabled(true);
        }
        ujAdatFelveteleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idString = ujAdatFelveteleId.getText().toString();
                String nev = ujAdatFelveteleNev.getText().toString();
                String orszag = ujAdatFelveteleOrszag.getText().toString();
                String lakossagString = ujAdatFelveteleLakossag.getText().toString();
                Integer id = null;
                Integer lakossag = null;
                try {
                    id = Integer.parseInt(idString);
                    lakossag = Integer.parseInt(lakossagString);
                } catch (NumberFormatException e) {
                    Toast.makeText(InsertActivity.this, "Hibás formátum: Számot adj meg a 'Lakosság' mezőbe", Toast.LENGTH_SHORT).show();
                }
                if (lakossag != null) {
                    City city = new City(id, nev, orszag, lakossag);
                    Gson jsonConverter = new Gson();
                    RequestTask task = new RequestTask(url, "POST", jsonConverter.toJson(city));
                    task.execute();
                }
            }
        });
        ujAdatVisszaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsertActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private class RequestTask extends AsyncTask<Void, Void, Response> {
        String requestUrl;
        String requestType;
        String requestParams;

        public RequestTask(String requestUrl, String requestType, String requestParams) {
            this.requestUrl = requestUrl;
            this.requestType = requestType;
            this.requestParams = requestParams;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ujAdatFelveteleProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Response doInBackground(Void... voids) {
            Response response = null;
            try {
                response = RequestHandler.post(requestUrl,requestParams);
            } catch (IOException e) {
                Toast.makeText(InsertActivity.this,
                        e.toString(), Toast.LENGTH_SHORT).show();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            ujAdatFelveteleProgressBar.setVisibility(View.GONE);
            if (response.getResponseCode() >= 400) {
                Toast.makeText(InsertActivity.this,
                        "Hiba történt a kérés feldolgozása során", Toast.LENGTH_SHORT).show();
            }
                Toast.makeText(InsertActivity.this, "Sikeres hozzáadás", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(InsertActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
        }
    }
    private void init(){
        ujAdatFelveteleId = findViewById(R.id.ujAdatFelveteleId);
        ujAdatFelveteleNev = findViewById(R.id.ujAdatFelveteleNev);
        ujAdatFelveteleOrszag = findViewById(R.id.ujAdatFelveteleOrszag);
        ujAdatFelveteleLakossag = findViewById(R.id.ujAdatFelveteleLakossag);
        ujAdatFelveteleButton = findViewById(R.id.ujAdatFelveteleButton);
        ujAdatVisszaButton = findViewById(R.id.ujAdatVisszaButton);
        ujAdatFelveteleProgressBar = findViewById(R.id.ujAdatFelveteleProgressBar);

    }
}