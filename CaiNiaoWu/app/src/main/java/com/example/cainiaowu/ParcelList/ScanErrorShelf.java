package com.example.cainiaowu.ParcelList;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cainiaowu.R;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class ScanErrorShelf extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView ScannerView;
    Button bttnClear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_error_shelf);

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

        //Show shelf result in ParcelListPage
        ParcelListPage.shelfInfo.setText(result.getText());

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
