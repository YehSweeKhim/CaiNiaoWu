package com.example.cainiaowu.LoginActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistrationPage extends AppCompatActivity {

    private static final String TAG = "Check";
    private EditText txtUsername;
    private ImageButton bttnCancelUsername;
    private EditText txtPassword;
    private ImageButton bttnCancelPassword;
    private EditText txtConfirmpassword;
    private ImageButton bttnCancelConfirmPassword;
    private Button bttnRegister;
    private TextView tvLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        txtUsername = findViewById(R.id.txtUsername);
        bttnCancelUsername = findViewById(R.id.bttnCancelUsername);
        txtPassword = findViewById(R.id.txtPassword);
        bttnCancelPassword = findViewById(R.id.bttnCancelPassword);
        txtConfirmpassword = findViewById(R.id.txtConfirmpassword);
        bttnCancelConfirmPassword = findViewById(R.id.bttnCancelConfirmPassword);
        bttnRegister = findViewById(R.id.bttnRegister);
        tvLogin = findViewById(R.id.tvLogin);

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

        //Confirm password
        bttnCancelConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtConfirmpassword.setText("");
            }
        });

        final TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        //Register button
        bttnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            txtUsername.addTextChangedListener(afterTextChangedListener);
            txtPassword.addTextChangedListener(afterTextChangedListener);
            txtConfirmpassword.addTextChangedListener(afterTextChangedListener);

            if (!isUsernameValid(txtUsername) || !isPasswordValid(txtPassword)) {
                Toast.makeText(getApplicationContext(),"Username invalid or logo_password too short", Toast.LENGTH_SHORT).show();
            }else {
                final String username = txtUsername.getText().toString();
                final String password = txtPassword.getText().toString();
                final String confirmPassword = txtConfirmpassword.getText().toString();

                final FirebaseOptions options = new FirebaseOptions.Builder()
                    .setApiKey("AIzaSyBdmK9vL24Nnl8om-OXw_BYaAPEeZQ5Xrs")
                    .setApplicationId("com.example.CaiNiaoWu")
                    .setDatabaseUrl("https://workers-at-cainiao.firebaseio.com/")
                    .build();

                DatabaseReference dataRef = FirebaseDatabase.getInstance("https://workers-at-cainiao.firebaseio.com/").getReference();
                dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(username)){
                            Toast.makeText(getApplicationContext(),"Username has been taken", Toast.LENGTH_LONG).show();
                        }else{
                            if (password.equals(confirmPassword)){
                                FirebaseApp workerData = FirebaseApp.initializeApp(getApplicationContext(), options, "Workers");
                                FirebaseDatabase workerDatabase = FirebaseDatabase.getInstance(workerData);
                                workerDatabase.getReference().child(username).setValue(password);
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        MainActivity.tvUsername.setText(username);
                                    }

                                },1000);
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            }else{
                                Toast.makeText(getApplicationContext(),"Passwords do not match",Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, databaseError.getMessage());
                    }
                });
            }
            }
        });

        //Login button
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginPage.class));
            }
        });
    }

    //A placeholder password validation check
    private boolean isUsernameValid(TextView username) {
        return username != null;
    }

    //A placeholder password validation check
    private boolean isPasswordValid(TextView password) {
        return password != null && password.length() > 5;
    }
}