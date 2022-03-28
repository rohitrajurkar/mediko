package mediko.newmediko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

public class ClinicActivity extends AppCompatActivity {

    ImageView back;
    TextView save, currentLocation;
    EditText clinicName, clinicAddress, clinicLocality, clinicCity, clinicState,
            clinicPincode, clinicNumber, clinicFees;
    DatabaseReference mClinicRef,mRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    LinearLayout linearLayout;
    ProgressDialog progressDialog;
    // private FusedLocationProviderClient fusedLocationProviderClient;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic);
        back = findViewById(R.id.backClinic);
        save = findViewById(R.id.clinic_save);
        clinicName = findViewById(R.id.clinicName);
        clinicAddress = findViewById(R.id.clinicAddress);
        clinicLocality = findViewById(R.id.clinicLocality);

        clinicState = findViewById(R.id.clinicState);
        clinicPincode = findViewById(R.id.clinicPincode);
        clinicNumber = findViewById(R.id.clinicNumber);
        clinicFees = findViewById(R.id.clinicFees);
        progressDialog = new ProgressDialog(this);

        //  fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mClinicRef = FirebaseDatabase.getInstance().getReference().child("Clinics");
        mRef = FirebaseDatabase.getInstance().getReference().child("Doctors");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveClinicData();
            }
        });
        mClinicRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String ct_clinicName = snapshot.child("Name").getValue().toString();
                    String ct_clinicAddress = snapshot.child("Address").getValue().toString();
                    String ct_clinicpincode = snapshot.child("Pincode").getValue().toString();
                    String ct_clinicLocality = snapshot.child("Locality").getValue().toString();
                    String ct_clinicFess = snapshot.child("Fees").getValue().toString();
                    String ct_clinicState = snapshot.child("State").getValue().toString();
                    String ct_clinicNumber = snapshot.child("Clinic Number").getValue().toString();

                    clinicName.setText(ct_clinicName);
                    clinicAddress.setText(ct_clinicAddress);
                    clinicFees.setText(ct_clinicFess);
                    clinicNumber.setText(ct_clinicNumber);
                    clinicLocality.setText(ct_clinicLocality);
                    clinicState.setText(ct_clinicState);
                    clinicPincode.setText(ct_clinicpincode);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void saveClinicData() {
        String name = clinicName.getText().toString();
        // String city = clinicCity.getText().toString();
        String address = clinicAddress.getText().toString();
        String locality = clinicLocality.getText().toString();
        String state = clinicState.getText().toString();
        String pincode = clinicPincode.getText().toString();
        String number = clinicNumber.getText().toString();
        String fees = clinicFees.getText().toString();

        if (name.isEmpty()) {
            showError(clinicName, "Required field");
        } else if (address.isEmpty()) {
            showError(clinicAddress, "Required field");
        } else if (locality.isEmpty()) {
            showError(clinicLocality, "Required field");
        } else if (state.isEmpty()) {
            showError(clinicState, "Required field");
        } else if (pincode.isEmpty()) {
            showError(clinicPincode, "Required field");
        } else if (number.isEmpty()) {
            showError(clinicNumber, "Required field");
        } else if (number.length() != 10) {
            showError(clinicNumber, "Please enter valid number");
        }
        if (fees.isEmpty()) {
            showError(clinicFees, "Required field");
        } else {
            progressDialog.setTitle("Uploading..");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            HashMap hashMap = new HashMap();
            hashMap.put("Hospital Name", name);
            hashMap.put("Fees","Rs "+ fees);
            mRef.child(mUser.getUid()).updateChildren(hashMap);

            // hashMap.put("City", city);
            hashMap.put("Address", address);
            hashMap.put("Locality", locality);
            hashMap.put("State", state);
            hashMap.put("Pincode", pincode);
            hashMap.put("Clinic Number", number);

            hashMap.put("status", "complete");

            mClinicRef.child(mUser.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ClinicActivity.this, "Clinic Detail is updated..", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(ClinicActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void showError(EditText input, String data) {
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