package com.example.cainiaowu.ScanParcel;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cainiaowu.ParcelList.ParcelListPage;
import com.example.cainiaowu.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.Result;

import java.util.Calendar;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class ScanParcel extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView ScannerView;
    DatabaseReference shelfRef;
    Button bttnCheck;
    Button bttnClear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_parcel);

        //Scanner
        ScannerView = findViewById(R.id.zxScan);

        //ParcelListPage Button
        bttnCheck = findViewById(R.id.bttnCheck);
        bttnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Set the delete section details in ParcelListPage
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (MainActivity.shelfType.getText().charAt(0) == '8' && !(MainActivity.parcelInfo.getText().equals("NONE"))) {
                            ParcelListPage.shelfInfo.setText(MainActivity.shelfType.getText());
                            ParcelListPage.parcelInfo.setText(MainActivity.parcelInfo.getText());
                        }
                    }
                },2000);

                //On click go to ParcelListPage
                startActivity(new Intent(getApplicationContext(), ParcelListPage.class));
            }
        });

        //Go back to Main Activity
        bttnClear = findViewById(R.id.bttnClear);
        bttnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScannerView.setResultHandler(this);
        ScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScannerView.stopCamera();
    }

    @Override
    public void handleResult(final Result result) {
        //Pop-up result
        Toast.makeText(this, result.getText(), Toast.LENGTH_SHORT).show();

        //Firebase
        shelfRef = FirebaseDatabase.getInstance().getReference().child(((String) MainActivity.shelfType.getText()).split("-")[0]).child(((String) MainActivity.shelfType.getText()).split("-")[0] + "-" + ((String) MainActivity.shelfType.getText()).split("-")[1]);
        Log.d("create", String.valueOf(shelfRef));
        Log.d("key", (String) MainActivity.shelfType.getText());
        Log.d("value", result.getText());
        DatabaseReference shelf = shelfRef.child((String) MainActivity.shelfType.getText()).child(result.getText());
        shelf.setValue(true);

        //Show parcel scanned result in Main Activity
        MainActivity.parcelInfo.setText(result.getText());

        //Updating ParcelListPage (Conditions)
        if (!MainActivity.arrayList.contains(MainActivity.shelfType.getText() + ":    " + result.getText() + " - scanned by " + MainActivity.tvUsername.getText().toString())) {
            //Show last updated time in ParcelListPage
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                    FirebaseDatabase.getInstance().getReference().child("Time").setValue(mydate);
                }
            },4000);

            //Update ParcelListPage details
            MainActivity.arrayList.add(MainActivity.shelfType.getText() + ":    " + result.getText() + " - scanned by " + MainActivity.tvUsername.getText().toString());
            MainActivity.adapter = new ArrayAdapter<>(this, R.layout.list_textview, MainActivity.arrayList);

        }else{
            //Show that the parcel has already been scanned
            Toast.makeText(this, "Data scanned", Toast.LENGTH_SHORT).show();
        }

        //Resume camera
        ScannerView.resumeCameraPreview(ScanParcel.this);
    }
}
