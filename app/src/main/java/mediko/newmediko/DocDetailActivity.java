package mediko.newmediko;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import mediko.newmediko.Utility.NetworkChangeListener;

public class DocDetailActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 101;
    ImageView back;
    CircleImageView profileImageView;
    TextView upload, docSave;
    EditText docName, docCity, docExperience, docQualification , docSpeciality;

    DatabaseReference mRef, sRef;
  //  TextView docSpecialization;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    StorageReference storageReference;
    Uri imageUri;
    ProgressDialog progressDialog;
    private String  gender = "";
    RadioGroup radioGroup;
    RadioButton male, female;
    TextView idCheck;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_detail);
        back = findViewById(R.id.backEditDoc);
        profileImageView = findViewById(R.id.docProfileImage);
        upload = findViewById(R.id.uploadeImage);
        docSave = findViewById(R.id.docSave);
        docName = findViewById(R.id.doctorName);
        docCity = findViewById(R.id.doctorCity);
        docSpeciality = findViewById(R.id.doctorSpeciality);
        docExperience = findViewById(R.id.doctorExperience);
        docQualification = findViewById(R.id.doctorQualification);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        idCheck = findViewById(R.id.textCheck);
        progressDialog = new ProgressDialog(this);

        Intent data = getIntent();
      //  send = data.getStringExtra("send");
      //  docSpecialization.setText(send);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("Doctors");
        storageReference = FirebaseStorage.getInstance().getReference().child("Profile");
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.clearCheck();

        sRef = FirebaseDatabase.getInstance().getReference().child("Speciality");
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
                    String doctorName = snapshot.child("Name").getValue().toString();
                    String doctorcity = snapshot.child("City").getValue().toString();
                    String doctorqualification = snapshot.child("Qualification").getValue().toString();
                    String doctorgender = snapshot.child("Gender").getValue().toString();
                    String doctorProfile = snapshot.child("ProfileImage").getValue().toString();
                    String doctorExperience = snapshot.child("Experience").getValue().toString();
                    String et_specalization = snapshot.child("Specalization").getValue().toString();
                    Picasso.get().load(doctorProfile).into(profileImageView);
                    imageUri = Uri.parse(doctorProfile);
                    docName.setText(doctorName);
                    docCity.setText(doctorcity);
                    docExperience.setText(doctorExperience);
                    docQualification.setText(doctorqualification);
                    docSpeciality.setText(et_specalization);
                    if (doctorgender.equals("Male")) {

                        male.setChecked(true);
                    } else if (doctorgender.equals("Female")) {

                        female.setChecked(true);
                    } else {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
       /* sRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    String et_speciality = snapshot.child("Specalization").getValue().toString();

                    docSpecialization.setText(et_speciality);
                }
            }
             @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        */

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        docSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                //Toast.makeText(DocDetailActivity.this, ""+radioSexButton.getText(), Toast.LENGTH_SHORT).show();
            }
        });

     /*   docSpecialization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DocDetailActivity.this, SpecalityActivity.class));
            }
        });

      */

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                if (rb != null) {
                    // Toast.makeText(DocDetailActivity.this,rb.getText(), Toast.LENGTH_SHORT).show();
                    gender = rb.getText().toString();
                }
            }
        });
      /*  docRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DocDetailActivity.this , RegistrationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

       */

    }


    private void saveData() {
        String name = docName.getText().toString();
        String city = docCity.getText().toString();
        String experience = docExperience.getText().toString();
        String qualification = docQualification.getText().toString();
        // String registration = docRegistration.getText().toString();
        String specit= docSpeciality.getText().toString();
        String otherUserId=mUser.getUid().toString();

        if (name.isEmpty()) {
            showError(docName, "Required field");
        } else if (city.isEmpty()) {
            showError(docCity, "Required field");
        } else if (gender.isEmpty()) {
            Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show();
        } else if (experience.isEmpty()) {
            showError(docExperience, "Required field");
        } else if (qualification.isEmpty()) {
            showError(docQualification, "Required field");
        } else if (imageUri == null) {
            Toast.makeText(this, "Please Upload your Image", Toast.LENGTH_SHORT).show();
        } else if(specit.isEmpty()){
            showError(docSpeciality, "Required field");
        }
        else {
            progressDialog.setTitle("Uploading..");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            storageReference.child(mUser.getUid()).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        storageReference.child(mUser.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                HashMap hashMap = new HashMap();
                                hashMap.put("Name", name);
                                hashMap.put("City", city);
                                hashMap.put("Specalization", specit);
                                hashMap.put("ProfileImage", uri.toString());
                                hashMap.put("Gender", gender);
                                hashMap.put("Experience", experience + " years");
                                hashMap.put("Qualification", qualification);
                                hashMap.put("OtherUserId" , otherUserId);
                                //  hashMap.put("Registration no", registration);
                                hashMap.put("status", "complete");

                                mRef.child(mUser.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        Toast.makeText(DocDetailActivity.this, "Personal Detail Updated..", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(DocDetailActivity.this, RegistrationActivity.class));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(DocDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        });
                    }
                }
            });
        }

    }

    private void showError(EditText input, String data) {
        input.setError(data);
        input.requestFocus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri);
        }
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
