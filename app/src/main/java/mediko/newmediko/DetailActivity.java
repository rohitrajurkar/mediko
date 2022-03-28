package mediko.newmediko;

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

import mediko.newmediko.Utility.NetworkChangeListener;

public class DetailActivity extends AppCompatActivity {

    TextView editPersonal, editClinic, editDocument, editDoctor, editClinicTime;
    TextView verifyPerson, verifyClinic, verifyDocument, verifyDoctorTiming, verifyClinicTime;
    ImageView checkPerson, checkClinic, checkDocument, checkDoctorTime, checkClinicTime;
    DatabaseReference mUserRef, mVerifyRef, mClinicRef, mDocumentRef, mDoctorRef, mClinicTRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    Button btnSubmit;

    String clinic, document, doctorTime, clinicTime, personal;
    ProgressDialog progressDialog;

    // NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        editPersonal = findViewById(R.id.editPersonal);
        editClinic = findViewById(R.id.editClinic);
        editDocument = findViewById(R.id.editDocument);
        editDoctor = findViewById(R.id.editDoctorTiming);
        verifyPerson = findViewById(R.id.verifyPersonal);
        verifyClinic = findViewById(R.id.verifyClinic);
        verifyDocument = findViewById(R.id.verifyDocument);
        verifyDoctorTiming = findViewById(R.id.verifyDoctorTiming);
        checkPerson = findViewById(R.id.checkPersonal);
        checkClinic = findViewById(R.id.checkClinic);
        checkDocument = findViewById(R.id.checkDocument);
        checkDoctorTime = findViewById(R.id.checkDoctorTime);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        btnSubmit = findViewById(R.id.btnSubmit);
        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Doctors");
        mVerifyRef = FirebaseDatabase.getInstance().getReference().child("DocVerify");
        mClinicRef = FirebaseDatabase.getInstance().getReference().child("Clinics");
        mDocumentRef = FirebaseDatabase.getInstance().getReference().child("Documents");
        mDoctorRef = FirebaseDatabase.getInstance().getReference().child("DoctorTiming");

        editPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailActivity.this, DocDetailActivity.class));
            }
        });
        editClinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailActivity.this, ClinicActivity.class));
            }
        });
        editDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailActivity.this, DocumentActivity.class));
            }
        });
        editDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailActivity.this, TimingActivity.class));
            }
        });

        mUserRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String username = snapshot.child("status").getValue().toString();
                    verifyPerson.setText(username);
                    checkPerson.setColorFilter(Color.GREEN);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailActivity.this, "" + error.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });
        mClinicRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String username = snapshot.child("status").getValue().toString();
                    verifyClinic.setText(username);
                    checkClinic.setColorFilter(Color.GREEN);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailActivity.this, "" + error.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });
        mDocumentRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String username = snapshot.child("status").getValue().toString();
                    verifyDocument.setText(username);
                    checkDocument.setColorFilter(Color.GREEN);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailActivity.this, "" + error.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });
        mDoctorRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String username = snapshot.child("status").getValue().toString();
                    verifyDoctorTiming.setText(username);
                    checkDoctorTime.setColorFilter(Color.GREEN);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailActivity.this, "" + error.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });

   /*     mDRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                     personal = snapshot.child("status").getValue().toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DetailActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        mCRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    clinic = snapshot.child("status").getValue().toString();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DetailActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        mDoRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    document = snapshot.child("status").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        mYRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    doctorTime = snapshot.child("status").getValue().toString();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DetailActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        mCtRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    clinicTime = snapshot.child("status").getValue().toString();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DetailActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
*/
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personal = verifyPerson.getText().toString();
                clinic = verifyClinic.getText().toString();
                document = verifyDocument.getText().toString();
                doctorTime = verifyDoctorTiming.getText().toString();

                if (!personal.equals("complete")) {
                    showError(verifyPerson, "Required Filed");
                    Toast.makeText(DetailActivity.this, "Complete Personal Detail", Toast.LENGTH_SHORT).show();
                } else if (!clinic.equals("complete")) {
                    showError(verifyClinic, "Required Filed");
                    Toast.makeText(DetailActivity.this, "Complete Clinic Detail", Toast.LENGTH_SHORT).show();
                } else if (!document.equals("complete")) {
                    showError(verifyDocument, "Required Filed");
                    Toast.makeText(DetailActivity.this, "Please Upload Documents", Toast.LENGTH_SHORT).show();
                } else if (!doctorTime.equals("complete")) {
                    showError(verifyDoctorTiming, "Required Filed");
                    Toast.makeText(DetailActivity.this, "Fill Your Timing", Toast.LENGTH_SHORT).show();
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
                                startActivity(new Intent(DetailActivity.this, AppActivity.class));
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

 /*  @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }*/


}