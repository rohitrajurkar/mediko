package mediko.newmediko;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import mediko.newmediko.Utility.NetworkChangeListener;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView profile;
    private TextView name, specalization, experience, education, fees, gender, clinicProfile,
            sunday, monday, tuesday, wesnday, thursday, friday, saturaday;

    DatabaseReference mRef, tRef, mClinicRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String key;
    String user;
    ImageView editDocFees, editDocTime;
    EditText edDocFees;
    TextView save;
    ImageView back;
    Dialog dialog;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profile = findViewById(R.id.docProfile);
        back = findViewById(R.id.backProfile);
        name = findViewById(R.id.docNameProfile);
        specalization = findViewById(R.id.docSpeciProfile);
        experience = findViewById(R.id.profileExperience);
        education = findViewById(R.id.profileEducation);
        fees = findViewById(R.id.profileFees);
        gender = findViewById(R.id.profileGender);
        clinicProfile = findViewById(R.id.clinicProfile);
        sunday = findViewById(R.id.hosSunday);
        monday = findViewById(R.id.hosMonday);
        tuesday = findViewById(R.id.hosTuesday);
        wesnday = findViewById(R.id.hosWensday);
        thursday = findViewById(R.id.hosThurday);
        friday = findViewById(R.id.hosFirday);
        saturaday = findViewById(R.id.hosSaturday);


        editDocFees = findViewById(R.id.editdocFees);
        editDocTime = findViewById(R.id.editDocTime);
        save = findViewById(R.id.saveProfile);
        save.setVisibility(View.INVISIBLE);
        edDocFees = findViewById(R.id.edDocFees);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        user = mAuth.getCurrentUser().getUid();
        Intent intent = getIntent();
        key = intent.getStringExtra("otherUserId");
        mRef = FirebaseDatabase.getInstance().getReference().child("Doctors");
        tRef = FirebaseDatabase.getInstance().getReference().child("DoctorTiming");
        mClinicRef = FirebaseDatabase.getInstance().getReference().child("Clinics");
        dialog = new Dialog(ProfileActivity.this);
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
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String profileImage = snapshot.child("ProfileImage").getValue().toString();
                    String dName = snapshot.child("Name").getValue().toString();
                    String dspeci = snapshot.child("Specalization").getValue().toString();
                    String dexperi = snapshot.child("Experience").getValue().toString();
                    String deducation = snapshot.child("Qualification").getValue().toString();
                    String dgender = snapshot.child("Gender").getValue().toString();
                    //  String dfees = snapshot.child("Fees").getValue().toString();

                    Picasso.get().load(profileImage).into(profile);
                    name.setText(dName);
                    specalization.setText(dspeci);
                    experience.setText(dexperi);
                    education.setText(deducation);
                    //   gender.setText(dfees);
                    fees.setText(dgender);
                    //  edDocFees.setText(dfees);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mClinicRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String dfees = snapshot.child("Fees").getValue().toString();

                gender.setText("Rs " + dfees);
                edDocFees.setText(dfees);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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


        editDocFees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edDocFees.setVisibility(View.VISIBLE);
                save.setVisibility(View.VISIBLE);
                gender.setVisibility(View.INVISIBLE);
                editDocFees.setVisibility(View.INVISIBLE);

            }
        });
        clinicProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, ClinicInfoActivity.class));
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edFeesDoc = edDocFees.getText().toString();
                if (edFeesDoc.isEmpty()) {
                    edDocFees.setError("Required Field");
                } else {
                    dialog = new Dialog(ProfileActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_wait);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    HashMap hashMap = new HashMap();
                    hashMap.put("Fees", edFeesDoc);
                    mClinicRef.child(mUser.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            dialog.dismiss();
                            gender.setVisibility(View.VISIBLE);
                            editDocFees.setVisibility(View.VISIBLE);
                            edDocFees.setVisibility(View.INVISIBLE);
                            save.setVisibility(View.INVISIBLE);
                            Toast.makeText(ProfileActivity.this, "Your data is Updated..", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        editDocTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, DocTimeActivity.class));
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