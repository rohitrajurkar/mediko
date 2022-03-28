package mediko.newmediko.Hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import mediko.newmediko.Adapter.ServiceMyAdapter;
import mediko.newmediko.R;
import mediko.newmediko.Utility.NetworkChangeListener;
import mediko.newmediko.Utils.service;

public class HosProfileActivity extends AppCompatActivity {

    CircleImageView hosProfile;
    TextView hosName, hosAddress, hosCity, sunday, monday, tuesday, wesnday, thursday, friday, saturaday,
            hosNumber, save;
    EditText inputNumber;
    ImageView editNumber, editTime, addServices, back;
    DatabaseReference hRef, tRef, mRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private RecyclerView recyclerView;
    ServiceMyAdapter adapter;

    Dialog dialog;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hos_profile);
        hosProfile = findViewById(R.id.docProfile);
        hosName = findViewById(R.id.docNameProfile);
        hosAddress = findViewById(R.id.hosAddressProfile);
        hosCity = findViewById(R.id.docSpeciProfile);
        sunday = findViewById(R.id.hosSunday);
        monday = findViewById(R.id.hosMonday);
        tuesday = findViewById(R.id.hosTuesday);
        wesnday = findViewById(R.id.hosWensday);
        thursday = findViewById(R.id.hosThurday);
        friday = findViewById(R.id.hosFirday);
        saturaday = findViewById(R.id.hosSaturday);
        hosNumber = findViewById(R.id.hosNumberProfile);
        inputNumber = findViewById(R.id.hosInputNumber);
        editNumber = findViewById(R.id.editPhone);
        editTime = findViewById(R.id.editDocTime);
        addServices = findViewById(R.id.addService);
        back = findViewById(R.id.backHosProfile);
        save = findViewById(R.id.saveProfile);
        save.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        dialog = new Dialog(HosProfileActivity.this);
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

        hRef = FirebaseDatabase.getInstance().getReference().child("Hospitals");
        tRef = FirebaseDatabase.getInstance().getReference().child("HospitalTiming");
        mRef = FirebaseDatabase.getInstance().getReference().child("Services");
        recyclerView = findViewById(R.id.recycleService);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<service> options = new FirebaseRecyclerOptions.Builder<service>().setQuery(mRef.
                child(mUser.getUid()), service.class).build();

        adapter = new ServiceMyAdapter(options);
        recyclerView.setAdapter(adapter);


        hRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String profileImageUrl = snapshot.child("HospitalLogo").getValue().toString();
                    String hCity = snapshot.child("City").getValue().toString();
                    String address = snapshot.child("Address").getValue().toString();
                    String phone = snapshot.child("Phone").getValue().toString();
                    String name = snapshot.child("Name").getValue().toString();


                    Picasso.get().load(profileImageUrl).into(hosProfile);
                    hosCity.setText(hCity);
                    hosName.setText(name);
                    hosAddress.setText(address);
                    hosNumber.setText(phone);
                    inputNumber.setText(phone);

                } else {
                    Toast.makeText(HosProfileActivity.this, "Data not exits", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HosProfileActivity.this, "" + error.getMessage().toString(), Toast.LENGTH_SHORT).show();

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

                    if (esonday.equals("Closed to Closed")) {
                        sunday.setText("Closed");
                    } else {
                        sunday.setText(esonday);
                    }
                    if (emonday.equals("Closed to Closed")) {
                        monday.setText("Closed");
                    } else {
                        monday.setText(emonday);
                    }
                    if (etuesday.equals("Closed to Closed")) {
                        tuesday.setText("Closed");
                    } else {
                        tuesday.setText(etuesday);
                    }
                    if (ewensday.equals("Closed to Closed")) {
                        wesnday.setText("Closed");
                    } else {
                        wesnday.setText(ewensday);
                    }
                    if (ethurday.equals("Closed to Closed")) {
                        thursday.setText("Closed");
                    } else {
                        thursday.setText(ethurday);
                    }
                    if (efriday.equals("Closed to Closed")) {
                        friday.setText("Closed");
                    } else {
                        friday.setText(efriday);
                    }
                    if (esaturday.equals("Closed to Closed")) {
                        saturaday.setText("Closed");
                    } else {
                        saturaday.setText(esaturday);
                    }


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
        editNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputNumber.setVisibility(View.VISIBLE);
                save.setVisibility(View.VISIBLE);
                editNumber.setVisibility(View.INVISIBLE);
                hosNumber.setVisibility(View.INVISIBLE);

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String etPhone = inputNumber.getText().toString();
                if (etPhone.isEmpty()) {
                    inputNumber.setError("Please enter number");
                } else {
                    HashMap data = new HashMap();
                    data.put("Phone", etPhone);
                    hRef.child(mUser.getUid()).updateChildren(data).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            inputNumber.setVisibility(View.INVISIBLE);
                            save.setVisibility(View.INVISIBLE);
                            editNumber.setVisibility(View.VISIBLE);
                            hosNumber.setVisibility(View.VISIBLE);
                            Toast.makeText(HosProfileActivity.this, "data updated...", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(HosProfileActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HosProfileActivity.this, HosTimingActivity.class));
            }
        });
        addServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HosProfileActivity.this, HosServiceActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}