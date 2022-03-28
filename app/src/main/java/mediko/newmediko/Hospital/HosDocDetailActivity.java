package mediko.newmediko.Hospital;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import mediko.newmediko.R;
import mediko.newmediko.Utility.NetworkChangeListener;

public class HosDocDetailActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 101;
    ImageView back;
    CircleImageView profileImageView;
    TextView upload, docSave;
    EditText docName, docCity, docExperience, docQualification, docFees, docSpeciality;

    DatabaseReference mRef, sRef , dRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    StorageReference storageReference;
    Uri imageUri;
    ProgressDialog progressDialog;
    private String gender = "";
    RadioGroup radioGroup;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hos_doc_detail);
        back = findViewById(R.id.backEditDoc);
        profileImageView = findViewById(R.id.docProfileImage);
        upload = findViewById(R.id.uploadeImage);
        docSave = findViewById(R.id.docSave);
        docName = findViewById(R.id.doctorName);
        docCity = findViewById(R.id.doctorCity);
        docExperience = findViewById(R.id.doctorExperience);
        docQualification = findViewById(R.id.doctorQualification);
        docSpeciality = findViewById(R.id.doctorSpeciality);
        progressDialog = new ProgressDialog(this);
        docFees = findViewById(R.id.doctorFees);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        dRef = FirebaseDatabase.getInstance().getReference().child("HosDoctors");
        mRef = FirebaseDatabase.getInstance().getReference().child("Doctors");
        storageReference = FirebaseStorage.getInstance().getReference().child("HosDocProfile");
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.clearCheck();
        sRef = FirebaseDatabase.getInstance().getReference().child("Speciality");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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


    }


    private void saveData() {
        String name = docName.getText().toString();
        String city = docCity.getText().toString();
        String experience = docExperience.getText().toString();
        String qualification = docQualification.getText().toString();
        String fess = docFees.getText().toString();
        String spec = docSpeciality.getText().toString();

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
        } else if (fess.isEmpty()) {
            showError(docFees, "Required field");
        } else if (imageUri.toString().isEmpty()) {
            Toast.makeText(this, "Please Upload your Image", Toast.LENGTH_SHORT).show();
        } else if (spec.isEmpty()) {
            showError(docSpeciality, "Required field");
        } else {
            progressDialog.setTitle("Uploading..");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            storageReference.child(mUser.getUid()).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {

                        String key = mRef.push().getKey();
                        String otherUser=mUser.getUid().toString();
                        storageReference.child(mUser.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                HashMap hashMap = new HashMap();
                                hashMap.put("Name", name);
                                hashMap.put("City", city);
                                hashMap.put("Specalization", spec);
                                hashMap.put("ProfileImage", uri.toString());
                                hashMap.put("Gender", gender);
                                hashMap.put("Experience", experience + " years");
                                hashMap.put("Qualification", qualification);
                                hashMap.put("Fees", "Rs " + fess);
                                hashMap.put("OtherUserId",otherUser);
                                hashMap.put("status", "complete");

                                mRef.child(key).updateChildren(hashMap);
                                dRef.child(mUser.getUid()).child(key).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(HosDocDetailActivity.this, HosDocTimeActivity.class);
                                            intent.putExtra("key", key);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(HosDocDetailActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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

