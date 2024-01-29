package com.example.varosok;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class updateActivity extends AppCompatActivity {
    private TextView textView;
    private ProgressBar updateActivityProgressBar;
    private EditText updateActivityId;
    private EditText updateActivityNev;
    private EditText updateActivityOrszag;
    private EditText updateActivityLakossag;
    private Button updateActivityUpdateButton;
    private Button updateActivityBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        init();
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("id")){
        int id = intent.getIntExtra("id",0);

            textView.setText(String.valueOf(id));
        }

    }
    public void init(){
        textView = findViewById(R.id.textview);

    }
}