package mediko.newmediko.Hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import mediko.newmediko.R;
import mediko.newmediko.Utility.NetworkChangeListener;

public class HosDetailActivity extends AppCompatActivity {

    TextView editHospital, editDocument, editTime;
    TextView verifyHospital, verifyDocument, verifyTime;
    ImageView checkHospital, checkDocument, checkTime;
    DatabaseReference mUserRef, mVerifyRef, mDocumentRef, mClinicTRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    Button btnSubmit;

    String clinic, document, doctorTime, clinicTime, personal;
    ProgressDialog progressDialog;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hos_detail);
        editHospital = findViewById(R.id.editHospital);
        editDocument = findViewById(R.id.editDocument);
        editTime = findViewById(R.id.editTime);
        verifyHospital = findViewById(R.id.verifyHospital);
        verifyDocument = findViewById(R.id.verifyDocument);
        verifyTime = findViewById(R.id.verifyTime);
        checkHospital = findViewById(R.id.checkHospital);
        checkDocument = findViewById(R.id.checkDocument);
        checkTime = findViewById(R.id.checkTime);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        btnSubmit = findViewById(R.id.btnSubmit);
        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Hospitals");
        mVerifyRef = FirebaseDatabase.getInstance().getReference().child("Verify");
        mDocumentRef = FirebaseDatabase.getInstance().getReference().child("HosDocument");
        mClinicTRef = FirebaseDatabase.getInstance().getReference().child("HospitalTiming");

        editHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HosDetailActivity.this, HospitalActivity.class));
            }
        });

        editDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HosDetailActivity.this, HosUploadActivity.class));
            }
        });

        editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HosDetailActivity.this, HosTimingActivity.class));
            }
        });


        mUserRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String username = snapshot.child("status").getValue().toString();
                    verifyHospital.setText(username);
                    checkHospital.setColorFilter(Color.GREEN);

                } else {
                    checkHospital.setColorFilter(Color.GRAY);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HosDetailActivity.this, "" + error.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });

        mDocumentRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String username = snapshot.child("status").getValue().toString();
                    verifyDocument.setText(username);
                    checkDocument.setColorFilter(Color.GREEN);

                } else {
                    checkDocument.setColorFilter(Color.GRAY);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HosDetailActivity.this, "" + error.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });

        mClinicTRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String username = snapshot.child("status").getValue().toString();
                    verifyTime.setText(username);
                    checkTime.setColorFilter(Color.GREEN);

                } else {
                    checkTime.setColorFilter(Color.GRAY);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HosDetailActivity.this, "" + error.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personal = verifyHospital.getText().toString();
                document = verifyDocument.getText().toString();
                clinicTime = verifyTime.getText().toString();
                if (!personal.equals("complete")) {
                    showError(verifyHospital, "Required Filed");
                    Toast.makeText(HosDetailActivity.this, "Complete Personal Detail", Toast.LENGTH_SHORT).show();
                } else if (!document.equals("complete")) {
                    showError(verifyDocument, "Required Filed");
                    Toast.makeText(HosDetailActivity.this, "Please Upload Documents", Toast.LENGTH_SHORT).show();
                } else if (!clinicTime.equals("complete")) {
                    showError(verifyTime, "Required Filed");
                    Toast.makeText(HosDetailActivity.this, "Fill Clinic Time", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.setTitle("Thank you..");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    HashMap hashMap = new HashMap();
                    hashMap.put("status", "complete");
                    mVerifyRef.child(mUser.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(HosDetailActivity.this, HosStartActivity.class));
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }

    private void showError(TextView input, String data) {
        input.setError(data);
        input.requestFocus();

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