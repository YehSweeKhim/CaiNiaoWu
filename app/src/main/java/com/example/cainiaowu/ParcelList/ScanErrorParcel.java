package com.example.cainiaowu.ParcelList;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cainiaowu.R;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class ScanErrorParcel extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView ScannerView;
    private Button bttnClear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_error_parcel);

        //Scanner
        ScannerView = findViewById(R.id.zxScan);

        //Go back to ParcelListPage
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
        //Pop-up result
        Toast.makeText(this, result.getText(), Toast.LENGTH_LONG).show();

        //Show parcel result in ParcelListPage
        ParcelListPage.parcelInfo.setText(result.getText());

        //Auto go back to ParcelListPage after scanning
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
