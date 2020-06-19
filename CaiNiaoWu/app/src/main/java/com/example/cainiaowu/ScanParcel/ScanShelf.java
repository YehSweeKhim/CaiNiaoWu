package com.example.cainiaowu.ScanParcel;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cainiaowu.R;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class ScanShelf extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView ScannerView;
    Button bttnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_shelf);

        //Scanner
        ScannerView = findViewById(R.id.zxScan);

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
    public void handleResult(final Result result) {
        //Show results in Main Activity (Conditions)
        if ((result.getText().charAt(0)) == '8'){
            //Pop-up result
            Toast.makeText(this, result.getText(), Toast.LENGTH_LONG).show();

            //show result in Main Activity
            MainActivity.shelfType.setText(result.getText());

        }else{
            //Pop-up invalid result
            Toast.makeText(this, "Shelf does not exist", Toast.LENGTH_SHORT).show();

            //Show invalid result in Main Activity
            MainActivity.shelfType.setText("Invalid stand");
        }

        //Auto go back to Main Activity after scanning
        onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScannerView.setResultHandler(this);
        ScannerView.startCamera();
    }
}
