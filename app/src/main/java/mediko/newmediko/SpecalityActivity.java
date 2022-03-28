package mediko.newmediko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import mediko.newmediko.Utils.model;

public class SpecalityActivity extends AppCompatActivity {


    TextView cardiologist, dietitian, endocrinologist, epidemiologist, physician, gastroenterologist, internal,
            maxillofacial, Nephrologist, Neurologist, Neurosurgeon, Obstetrician, Oncologist,
            Ophthalmologist, Orthopedic, Otolaryngologist, Pathologist, Pediatrician, Plastic, Psychiatrist, Pulmonologist,
            Radiologist, Urologist, Ayurvedic, Homeopathic, Nutritionist, Physiotherapist;
    DatabaseReference mRef, dRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    LinearLayout linearSpeciality, linearLayout1;
    private RecyclerView recyclerView;
    String user;
    String usernumber;


    MyAdapter adapter;
    TextView btnUpload;
    String send = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specality);

        setTitle("Search here....");

        linearSpeciality = findViewById(R.id.linearSpeciality);
        linearSpeciality.setVisibility(View.GONE);
        linearLayout1 = findViewById(R.id.linearSpeciality1);
        cardiologist = findViewById(R.id.specialityCardiologist);
        dietitian = findViewById(R.id.specialityDietitian);
        endocrinologist = findViewById(R.id.specialityEndocrinologist);
        epidemiologist = findViewById(R.id.specialityEpidemiologist);
        physician = findViewById(R.id.specialityPhysician);
        gastroenterologist = findViewById(R.id.specialityGastroenterologist);
        internal = findViewById(R.id.specialityInternal);
        maxillofacial = findViewById(R.id.specialityMaxillofacial);
        Nephrologist = findViewById(R.id.specialityNephrologist);
        Neurologist = findViewById(R.id.specialityNeurologist);
        Neurosurgeon = findViewById(R.id.specialityNeurosurgeon);
        Obstetrician = findViewById(R.id.specialityObstetrician);
        Oncologist = findViewById(R.id.specialityOncologist);
        Ophthalmologist = findViewById(R.id.specialityOphthalmologist);
        Orthopedic = findViewById(R.id.specialityOrthopedic);
        Otolaryngologist = findViewById(R.id.specialityOtolaryngologist);
        Pathologist = findViewById(R.id.specialityPathologist);
        Pediatrician = findViewById(R.id.specialityPediatrician);
        Plastic = findViewById(R.id.specialityPlastic);
        Psychiatrist = findViewById(R.id.specialityPsychiatrist);
        Pulmonologist = findViewById(R.id.specialityPulmonologist);
        Radiologist = findViewById(R.id.specialityRadiologist);
        Urologist = findViewById(R.id.specialityUrologist);
        Ayurvedic = findViewById(R.id.sprcialityAurvedic);
        Homeopathic = findViewById(R.id.specialityHomeopathic);
        Nutritionist = findViewById(R.id.specialityNutritionist);
        Physiotherapist = findViewById(R.id.specialityPhysiotherapist);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        user = mAuth.getCurrentUser().getUid();
        mRef = FirebaseDatabase.getInstance().getReference().child("Speciality");
        dRef = FirebaseDatabase.getInstance().getReference().child("Doctors");

        btnUpload = findViewById(R.id.btnUpload);


        cardiologist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                UploadData("Cardiologist", cardiologist);

            }
        });
        dietitian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadData("Dietitian/Dietician", dietitian);
            }
        });
        endocrinologist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadData("Endocrinologist", endocrinologist);
            }
        });
        epidemiologist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadData("Epidemiologist", epidemiologist);
            }
        });
        physician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadData("General Physician", physician);
            }
        });
        gastroenterologist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadData("Gastroenterologist", gastroenterologist);
            }
        });
        internal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadData("Internal Medicine Specialist", internal);
            }
        });
        maxillofacial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadData("Maxillofacial Surgeon", maxillofacial);
            }
        });
        Nephrologist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadData("Nephrologist", Nephrologist);
            }
        });
        Neurologist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadData("Neurologist", Neurologist);
            }
        });
        Neurosurgeon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadData(" Neurosurgeon", Neurosurgeon);
            }
        });
        Obstetrician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadData("Obstetrician/Gynecologist", Obstetrician);
            }
        });
        Oncologist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadData("Oncologist", Oncologist);
            }
        });
        Ophthalmologist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadData("Ophthalmologist", Ophthalmologist);
            }
        });
        Orthopedic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadData("Orthopedic Surgeon", Orthopedic);
            }
        });
        Otolaryngologist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadData("Otolaryngologist (ENT)", Otolaryngologist);
            }
        });
        Pathologist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadData("Pathologist", Pathologist);
            }
        });
        Pediatrician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadData("Pediatrician", Pediatrician);
            }
        });
        Plastic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadData("Plastic Surgeon", Plastic);
            }
        });
        Psychiatrist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadData("Psychiatrist", Psychiatrist);
            }
        });
        Pulmonologist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadData("Pulmonologist", Pulmonologist);
            }
        });
        Radiologist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadData("Radiologist", Radiologist);
            }
        });
        Urologist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadData("Urologist", Urologist);
            }
        });
        Ayurvedic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadData("Ayurvedic Practitioner", Ayurvedic);
            }
        });
        Homeopathic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadData("Homeopathic Doctor", Homeopathic);
            }
        });

        Nutritionist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadData("Nutritionist", Nutritionist);
            }
        });
        Physiotherapist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadData("Physiotherapist", Physiotherapist);
            }
        });

        recyclerView = findViewById(R.id.specialityRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<model> options = new FirebaseRecyclerOptions.Builder<model>().setQuery(mRef.child(user), model.class).build();

        adapter = new MyAdapter(options);
        recyclerView.setAdapter(adapter);

    /*    if (usernumber.equals("3"))
        {
            linearLayout1.setVisibility(View.GONE);
        }else
        {
            linearLayout1.setVisibility(View.VISIBLE);
        } */
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap hashMap = new HashMap();
                hashMap.put("Specalization", send);
                dRef.child(mUser.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Intent intent = new Intent(SpecalityActivity.this, RegistrationActivity.class);
                        startActivity(intent);
                    }
                });

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    private void UploadData(String s, TextView data) {
        HashMap hashMap = new HashMap();
        hashMap.put("Specalization", s);

        send = s;

        mRef.child(mUser.getUid()).push().updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    data.setVisibility(View.GONE);
                } else {
                    data.setVisibility(View.VISIBLE);
                }
            }
        });
    }

}
