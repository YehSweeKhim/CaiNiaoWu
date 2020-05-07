package com.example.cainiaowu.LoginActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cainiaowu.R;
import com.example.cainiaowu.ScanParcel.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginPage extends AppCompatActivity {

    private static final String TAG = "Check";
    private EditText txtUsername;
    private ImageButton bttnCancelUsername;
    private EditText txtPassword;
    private ImageButton bttnCancelPassword;
    private Button bttnLogin;
    private TextView tvForgotPassword;
    private TextView tvRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsername = findViewById(R.id.txtUsername);
        bttnCancelUsername = findViewById(R.id.bttnCancelUsername);
        txtPassword = findViewById(R.id.txtPassword);
        bttnCancelPassword = findViewById(R.id.bttnCancelPassword);
        bttnLogin = findViewById(R.id.bttnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvRegister = findViewById(R.id.tvRegister);

        //Username
        bttnCancelUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtUsername.setText("");
            }
        });

        //Password
        bttnCancelPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPassword.setText("");
            }
        });

        //Login button
        bttnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            final String username = txtUsername.getText().toString();
            final String password = txtPassword.getText().toString();

            DatabaseReference dataRef = FirebaseDatabase.getInstance("https://workers-at-cainiao.firebaseio.com/").getReference();
            dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChild(username)){
                        Toast.makeText(getApplicationContext(),"Username or Password does not exist", Toast.LENGTH_LONG).show();
                    } else if (dataSnapshot.child(username).getValue().equals(password)){
                        Log.d(TAG, dataSnapshot.child(username).getValue().toString());
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                MainActivity.tvUsername.setText(username);
                            }
                        },1000);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(),"Username or Password does not exist", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, databaseError.getMessage());
                }

            });
            }
        });

        //Forgot password
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgotPassword.class));
            }
        });

        //Register account
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegistrationPage.class));
            }
        });

    }
}
