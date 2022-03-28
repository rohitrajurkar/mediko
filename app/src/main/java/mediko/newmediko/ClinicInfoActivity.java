package mediko.newmediko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mediko.newmediko.Utility.NetworkChangeListener;

public class ClinicInfoActivity extends AppCompatActivity {

    TextView txtName, txtPhone, txtAddress;
    ImageView back;
    DatabaseReference dRef;

    TextView sunday, monday, tuesday, wesnday, thursday, friday, saturaday;
    DatabaseReference tRef, mRef;
    FirebaseAuth mAuth;
    ImageView editTime;
    FirebaseUser mUser;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_info);

        txtName = findViewById(R.id.profClinicName);
        txtPhone = findViewById(R.id.proClinicNumber);
        txtAddress = findViewById(R.id.profClinicAddress);
        sunday = findViewById(R.id.hosSunday);
        monday = findViewById(R.id.hosMonday);
        tuesday = findViewById(R.id.hosTuesday);
        wesnday = findViewById(R.id.hosWensday);
        thursday = findViewById(R.id.hosThurday);
        friday = findViewById(R.id.hosFirday);
        saturaday = findViewById(R.id.hosSaturday);
        editTime = findViewById(R.id.editDocTime);

        back = findViewById(R.id.backClinic);
        dRef = FirebaseDatabase.getInstance().getReference().child("Clinics");
        mRef = FirebaseDatabase.getInstance().getReference().child("Doctors");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        tRef = FirebaseDatabase.getInstance().getReference().child("ClinicTiming");

        dRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("Name").getValue().toString();
                    String phone = snapshot.child("Clinic Number").getValue().toString();
                    String address = snapshot.child("Address").getValue().toString();

                    txtName.setText(name);
                    txtAddress.setText(address);
                    txtPhone.setText(phone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ClinicInfoActivity.this, ClinicTimingActivity.class));
            }
        });
        tRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String esonday = snapshot.child("Sunday").getValue().toString();
                    String emonday = snapshot.child("Monday").getValue().toString();
                    String etuesday = snapshot.child("Tuesday").getValue().toString();
                    String ewensday = snapshot.child("Wensday").getValue().toString();
                    String ethurday = snapshot.child("Thursday").getValue().toString();
                    String efriday = snapshot.child("Friday").getValue().toString();
                    String esaturday = snapshot.child("Saturday").getValue().toString();

                    sunday.setText(esonday);
                    monday.setText(emonday);
                    tuesday.setText(etuesday);
                    wesnday.setText(ewensday);
                    thursday.setText(ethurday);
                    friday.setText(efriday);
                    saturaday.setText(esaturday);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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