package com.example.cainiaowu.ParcelList;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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

import java.util.Calendar;

public class ParcelListPage extends AppCompatActivity {

    private static final String TAG = "Check";

    //Header
    private static TextView date;
    private Button bttnBack;

    //Delete section
    public static TextView shelfInfo;
    private Button scanShelf;
    public static TextView parcelInfo;
    private Button scanParcel;
    private Button bttnDelete;

    //ListView of parcel data scanned
    private static ListView listView;

    //Confirmation of parcel data
    private Button bttnConfirm;
    private LinearLayout linearLayout;
    private PopupWindow popupWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel_list);

        //Go back to Main Activity
        bttnBack = findViewById(R.id.bttnBack);
        bttnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Confirmation of parcel data
        bttnConfirm = findViewById(R.id.bttnConfirm);
        linearLayout = findViewById(R.id.linearLayout);
        bttnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.arrayList.clear();
                MainActivity.arrayList.add("Parcel logo_data has been updated by " + MainActivity.tvUsername.getText().toString());
                MainActivity.adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_textview, MainActivity.arrayList);
                listView.setAdapter(MainActivity.adapter);

                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View customView = inflater.inflate(R.layout.popup_screen,null);
                popupWindow = new PopupWindow(
                        customView,
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT
                );

                if(Build.VERSION.SDK_INT>=21) popupWindow.setElevation(5.0f);

                ImageButton closeButton = customView.findViewById(R.id.close_button);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });

                popupWindow.showAtLocation(linearLayout, Gravity.CENTER,0,0);
            }
        });


        //ListView of parcel data scanned
        listView = findViewById(R.id.parcelList);
        listView.setAdapter(MainActivity.adapter);

        //Last updated time
        date = findViewById(R.id.date);
        DatabaseReference time = FirebaseDatabase.getInstance().getReference().child("Time");
        time.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                date.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        });


        //Delete section - shelf
        shelfInfo = findViewById(R.id.shelfInfo);
        scanShelf = findViewById(R.id.scanShelf);
        scanShelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ScanErrorShelf.class));
            }
        });

        //Delete section - parcel
        parcelInfo = findViewById(R.id.parcelInfo);
        scanParcel = findViewById(R.id.scanParcel);
        scanParcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ScanErrorParcel.class));
            }
        });

        //Delete button
        bttnDelete = findViewById(R.id.bttnDelete);
        bttnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String shelf = shelfInfo.getText().toString();
                final String parcel = parcelInfo.getText().toString();
                if (shelf.charAt(0) == '8' && !(parcel == "NONE")) {

                    //Delete parcel
                    deleteParcel(shelf, parcel);

                    //Updating last updated timing
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                            ParcelListPage.date.setText(mydate);
                            FirebaseDatabase.getInstance().getReference().child("Time").setValue(mydate);
                        }
                    },2000);

                }else{
                    //Parcel not found in Firebase
                    Toast.makeText(getApplicationContext(),"Invalid logo_data", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void deleteParcel(final String shelf, final String parcel) {
        Log.d("shelf", String.valueOf(shelf));
        Log.d("parcel", String.valueOf(parcel));
        final DatabaseReference deleteParcel = FirebaseDatabase.getInstance().getReference().child(shelf.split("-")[0]).child(shelf.split("-")[0] + "-" + shelf.split("-")[1]).child(shelf);
        deleteParcel.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(parcel)){

                    //Remove parcel data from Firebase
                    deleteParcel.child(parcel).removeValue();

                    //Show that the parcel data has been deleted on the ListView (Conditions)
                    if (MainActivity.arrayList.contains(shelf + ":    " + parcel + " - scanned by " + MainActivity.tvUsername.getText().toString())){
                        int index = MainActivity.arrayList.indexOf(shelf + ":    " + parcel + " - scanned by " + MainActivity.tvUsername.getText().toString());
                        String editText = shelf + ":    " + parcel + " - scanned by " + MainActivity.tvUsername.getText().toString() + "  (DELETED)";
                        MainActivity.arrayList.set(index, editText);
                        MainActivity.adapter.notifyDataSetChanged();
                        listView.setAdapter(MainActivity.adapter);
                    }else{
                        MainActivity.arrayList.add(shelf + ":    " + parcel + " - DELETED by " + MainActivity.tvUsername.getText().toString());
                        MainActivity.adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_textview, MainActivity.arrayList);
                        listView.setAdapter(MainActivity.adapter);
                    }

                    //Prompt short pop-up
                    Toast.makeText(getApplicationContext(), "Parcel is deleted", Toast.LENGTH_LONG).show();

                } else{
                    //Prompt short pop-up
                    Toast.makeText(getApplicationContext(),"Parcel does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        });
    }
}
