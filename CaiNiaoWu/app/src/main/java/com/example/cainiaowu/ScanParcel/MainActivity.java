package com.example.cainiaowu.ScanParcel;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cainiaowu.LoginActivity.LoginPage;
import com.example.cainiaowu.ParcelList.ParcelListPage;
import com.example.cainiaowu.R;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    public static TextView tvUsername;
    public static TextView shelfType;
    private Button bttnScanShelf;
    public static TextView parcelInfo;
    private Button bttnScanParcel;
    private Button bttnBack;
    public static ArrayList<String> arrayList;
    public static ArrayAdapter<String> adapter;
    private Button dataStored;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //tvUsername
        tvUsername = findViewById(R.id.tvUsername);

        //Shelf
        shelfType = findViewById(R.id.shelfType);
        bttnScanShelf = findViewById(R.id.bttnScanShelf);
        bttnScanShelf.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity((new Intent(getApplicationContext(), ScanShelf.class)));
            }
        }));

        //Parcel
        parcelInfo = findViewById(R.id.parcelInfo);
        bttnScanParcel = findViewById(R.id.bttnScanParcel);
        bttnScanParcel.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ScanParcel.class));
            }
        }));

        //Back press to Login Activity
        bttnBack = findViewById(R.id.bttnBack);
        bttnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginPage.class));
            }
        });

        //ListView storage
        final String[] items = {};
        arrayList = new ArrayList<>(Arrays.asList(items));

        //ParcelListPage Button
        dataStored = findViewById(R.id.dataStored);
        dataStored.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (shelfType.getText().charAt(0) == '8' && !(parcelInfo.getText().equals("NONE"))) {
                            ParcelListPage.shelfInfo.setText(shelfType.getText());
                            ParcelListPage.parcelInfo.setText(parcelInfo.getText());
                        }
                    }
                },2000);
                startActivity(new Intent(getApplicationContext(), ParcelListPage.class));
            }
        }));
    }
}
