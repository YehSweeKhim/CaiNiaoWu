package com.example.cainiaowu.LoginActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgotPassword extends AppCompatActivity {

    private static final String TAG = "Check";
    private EditText txtUsername;
    private ImageButton bttnCancelUsername;
    private EditText txtNewPassword;
    private ImageButton bttnCancelNewPassword;
    private EditText txtConfirmNewPassword;
    private ImageButton bttnCancelConfirmNewPassword;
    private Button bttnChangePassword;
    private TextView tvLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        txtUsername = findViewById(R.id.txtUsername);
        bttnCancelUsername = findViewById(R.id.bttnCancelUsername);
        txtNewPassword = findViewById(R.id.txtNewPassword);
        bttnCancelNewPassword = findViewById(R.id.bttnCancelNewPassword);
        txtConfirmNewPassword = findViewById(R.id.txtConfirmNewPassword);
        bttnCancelConfirmNewPassword = findViewById(R.id.bttnCancelConfirmNewPassword);
        bttnChangePassword = findViewById(R.id.bttnChangePassword);
        tvLogin = findViewById(R.id.tvLogin);

        //Username
        bttnCancelUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtUsername.setText("");
            }
        });

        //New password
        bttnCancelNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtNewPassword.setText("");
            }
        });

        //Confirm new password
        bttnCancelConfirmNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtConfirmNewPassword.setText("");
            }
        });

        //Confirm password button
        bttnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            DatabaseReference dataRef = FirebaseDatabase.getInstance("https://workers-at-cainiao.firebaseio.com/").getReference();
            dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final String username = txtUsername.getText().toString();
                    final String newPassword = txtNewPassword.getText().toString();
                    final String confirmNewPassword = txtConfirmNewPassword.getText().toString();

                    if (dataSnapshot.hasChild(username)){
                        if (newPassword.equals(confirmNewPassword)){
                            FirebaseDatabase workerDatabase = FirebaseDatabase.getInstance("https://workers-at-cainiao.firebaseio.com/");
                            workerDatabase.getReference().child(username).setValue(newPassword);
                            startActivity(new Intent(getApplicationContext(), LoginPage.class));
                        }else{
                            Toast.makeText(getApplicationContext(),"Passwords do not match", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Username does not exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, databaseError.getMessage());
                }
            });
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
}
