package mediko.newmediko.Hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
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

import mediko.newmediko.R;
import mediko.newmediko.Utility.NetworkChangeListener;

public class HosStartActivity extends AppCompatActivity {

    ImageView profile, consult, helthfeed, patient, setting, reach, doctor;

    DatabaseReference mUserRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    TextView textVerify;

    Dialog dialog;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hos_start);
        profile = findViewById(R.id.docappProfile);
        consult = findViewById(R.id.docappConsult);
        helthfeed = findViewById(R.id.docappMedia);
        patient = findViewById(R.id.docappPatients);
        setting = findViewById(R.id.appSetting);
        reach = findViewById(R.id.docappReach);
        textVerify = findViewById(R.id.textVerify);
        doctor = findViewById(R.id.appdoctor);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Verify");

        dialog = new Dialog(HosStartActivity.this);
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
                Toast.makeText(HosStartActivity.this, "" + error.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verify = textVerify.getText().toString();
                if (verify.equals("complete")) {
                    Toast.makeText(HosStartActivity.this, "We are Verifying!", Toast.LENGTH_SHORT).show();
                } else if (verify.equals("done")) {
                    startActivity(new Intent(HosStartActivity.this, HosProfileActivity.class));
                } else {
                    startActivity(new Intent(HosStartActivity.this, HosDetailActivity.class));
                }
            }
        });
        consult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HosStartActivity.this, "Coming Soon.....", Toast.LENGTH_SHORT).show();
            }
        });
        helthfeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HosStartActivity.this, "Coming Soon.....", Toast.LENGTH_SHORT).show();
            }
        });
        patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HosStartActivity.this, HosPatientAppointActivity.class));
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HosStartActivity.this, HosSettingActivity.class));
            }
        });
        reach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Toast.makeText(HosStartActivity.this, "Coming Soon.....", Toast.LENGTH_SHORT).show();
            }
        });
        doctor.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                String verify = textVerify.getText().toString();
                if (verify.equals("complete")) {
                    Toast.makeText(HosStartActivity.this, "We are Verifying!", Toast.LENGTH_SHORT).show();
                } else if (verify.equals("done")) {
                    startActivity(new Intent(HosStartActivity.this, HosDoctorActivity.class));
                } else {
                    Toast.makeText(HosStartActivity.this, "Please add Hospital Details..!", Toast.LENGTH_SHORT).show();
                }


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
        dialog.dismiss();
    }
}