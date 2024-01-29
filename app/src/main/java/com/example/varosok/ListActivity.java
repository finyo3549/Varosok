package com.example.varosok;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListActivity extends AppCompatActivity implements RequestCallback{


    public ListView listView_List;
    private Button backButtonListActivity;
    private Button modositButton_list;
    private EditText editTextId;
    private String url = "https://retoolapi.dev/xhsAsC/data";
    private List<City> cities = new ArrayList<>();
    private LinearLayout linearLayoutForm;
    private ProgressBar progressBar;

    @Override
    public void onSuccess(Response response, List<City> updatedCities) {
        progressBar.setVisibility(View.GONE);
        if(linearLayoutForm.getVisibility() == View.VISIBLE) {
            backButtonListActivity.setVisibility(View.VISIBLE);
        }

        cities.clear();
        cities.addAll(updatedCities);

        if (listView_List.getAdapter() != null) {
            ((CityAdapter) listView_List.getAdapter()).notifyDataSetChanged();
        }
    }

    @Override
    public void onFailure(IOException e) {
        progressBar.setVisibility(View.GONE);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        init();
        progressBar.setVisibility(View.VISIBLE);
        RequestTask task = new RequestTask(url, "GET",(RequestCallback) ListActivity.this,this);
        task.execute();
    }

    public void init() {
        listView_List = findViewById(R.id.listView_List);
        listView_List.setAdapter(new CityAdapter());
        backButtonListActivity = findViewById(R.id.backButtonListActivity);
        progressBar = findViewById(R.id.progressBar);
        linearLayoutForm = findViewById(R.id.linearLayoutForm);
        editTextId = findViewById(R.id.editTextId);
    }

    public class CityAdapter extends ArrayAdapter<City> {
        public CityAdapter() {
            super(ListActivity.this, R.layout.cities_list, cities);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //inflater létrehozása
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.cities_list, parent, false);

            TextView cityName = view.findViewById(R.id.cityName);
            Button modositButton_cities_list = view.findViewById(R.id.modositButton_cities_list);
            Button torolButton = view.findViewById(R.id.torolButton);
            City actualCity = cities.get(position);
            cityName.setText(actualCity.getName());
            editTextId.setText(String.valueOf(actualCity.getId()));


            modositButton_cities_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = Integer.parseInt(editTextId.getText().toString());
                    Intent intent = new Intent(ListActivity.this,updateActivity.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                    finish();
                }
            });
            torolButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestTask task = new RequestTask(url, "DELETE", String.valueOf(actualCity.getId()), (RequestCallback) ListActivity.this,ListActivity.this);
                    task.execute();
                }
            });
            backButtonListActivity.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ListActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            return view;
        }
    }
}