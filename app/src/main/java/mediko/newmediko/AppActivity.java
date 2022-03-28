package mediko.newmediko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mediko.newmediko.Utility.NetworkChangeListener;

public class AppActivity extends AppCompatActivity {

    ImageView profile, consult, helthfeed, patient, setting, reach, notification, nointernet;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    ConstraintLayout constraintLayout;
    DatabaseReference mUserRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    TextView textVerify;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        profile = findViewById(R.id.docappProfile);
        consult = findViewById(R.id.docappConsult);
        helthfeed = findViewById(R.id.docappMedia);
        patient = findViewById(R.id.docappPatients);
        setting = findViewById(R.id.appSetting);
        reach = findViewById(R.id.docappReach);
        textVerify = findViewById(R.id.textVerify);
        notification = findViewById(R.id.appNotification);
        constraintLayout = findViewById(R.id.appConsLayout);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("DocVerify");

        dialog = new Dialog(AppActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_wait);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 3000);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppActivity.this, NotificationActivity.class));
            }
        });

        mUserRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String city = snapshot.child("status").getValue().toString();
                    textVerify.setText(city);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AppActivity.this, "" + error.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verify = textVerify.getText().toString();
                if (verify.equals("complete")) {
                    Toast.makeText(AppActivity.this, "We are verifying!", Toast.LENGTH_SHORT).show();
                } else if (verify.equals("done")) {
                    startActivity(new Intent(AppActivity.this, ProfileActivity.class));
                } else {
                    startActivity(new Intent(AppActivity.this, DetailActivity.class));
                }
            }
        });
        consult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AppActivity.this, "Coming Soon.....", Toast.LENGTH_SHORT).show();
            }
        });
        helthfeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AppActivity.this, "Coming Soon.....", Toast.LENGTH_SHORT).show();
                // startActivity(new Intent(AppActivity.this, RegistrationActivity.class));
            }
        });
        patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppActivity.this, PatientsAppointmentActivity.class));
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppActivity.this, SettingActivity.class));
            }
        });
        reach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AppActivity.this, "Coming Soon.....", Toast.LENGTH_SHORT).show();

                // startActivity(new Intent(AppActivity.this, SpecalityActivity.class));
            }
        });

    }


    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }


}