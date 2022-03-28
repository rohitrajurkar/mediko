package mediko.newmediko.Hospital;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
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
import mediko.newmediko.R;
import mediko.newmediko.Utility.NetworkChangeListener;

public class HospitalActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 101;
    ImageView back;
    CircleImageView profileImageView;
    TextView upload, docSave;
    EditText hosName, hosCity, hosAddress, hosLocality, hosState, hosCountry, hosPincode, hosRegistration, hosNumber;
    DatabaseReference mRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    StorageReference storageReference;
    Uri imageUri;
    Dialog dialog;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);
        back = findViewById(R.id.backClinic);
        profileImageView = findViewById(R.id.hospitalLogo);
        upload = findViewById(R.id.uploadLogo);
        docSave = findViewById(R.id.hos_save);
        hosName = findViewById(R.id.hosName);
        hosCity = findViewById(R.id.hosCity);
        hosAddress = findViewById(R.id.hosAddress);
        hosState = findViewById(R.id.hosState);
        hosCountry = findViewById(R.id.hoscountry);
        hosPincode = findViewById(R.id.hosPincode);
        hosRegistration = findViewById(R.id.hosRegistration);
        hosNumber = findViewById(R.id.clinicNumber);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("Hospitals");
        storageReference = FirebaseStorage.getInstance().getReference().child("HospitalLogo");

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
                    String et_name = snapshot.child("Name").getValue().toString();
                    String et_city = snapshot.child("City").getValue().toString();
                    String et_address = snapshot.child("Address").getValue().toString();
                    String et_logo = snapshot.child("HospitalLogo").getValue().toString();
                    String et_state = snapshot.child("State").getValue().toString();
                    String et_phone = snapshot.child("Phone").getValue().toString();
                    String et_number = snapshot.child("Registration no").getValue().toString();
                    String et_pincode = snapshot.child("Pincode").getValue().toString();


                    Picasso.get().load(et_logo).into(profileImageView);
                    imageUri = Uri.parse(et_logo);
                    hosName.setText(et_name);
                    hosCity.setText(et_city);
                    hosAddress.setText(et_address);
                    hosState.setText(et_state);
                    hosPincode.setText(et_pincode);
                    hosRegistration.setText(et_number);
                    hosNumber.setText(et_phone);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE);
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
            }
        });


    }


    private void saveData() {
        //hosName, hosCity, hosAddress, hosLocality, hosState, hosCountry, hosPincode , hosRegistration
        String name = hosName.getText().toString();
        String city = hosCity.getText().toString();
        String address = hosAddress.getText().toString();
        String state = hosState.getText().toString();
        String country = hosCountry.getText().toString();
        String pincode = hosPincode.getText().toString();
        String registration = hosRegistration.getText().toString();
        String phone = hosNumber.getText().toString();


        if (name.isEmpty()) {
            showError(hosName, "Required field");
        } else if (phone.isEmpty()) {
            showError(hosNumber, "Required field");
        } else if (city.isEmpty()) {
            showError(hosCity, "Required field");
        } else if (address.isEmpty()) {
            showError(hosAddress, "Required field");
        } else if (state.isEmpty()) {
            showError(hosState, "Required field");
        } else if (pincode.isEmpty()) {
            showError(hosPincode, "Required field");
        } else if (registration.isEmpty()) {
            showError(hosRegistration, "Required field");
        } else if (imageUri == null) {
            Toast.makeText(this, "Please Upload your logo", Toast.LENGTH_SHORT).show();
            return;
        } else {
            dialog = new Dialog(HospitalActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_wait);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
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
                                hashMap.put("Address", address + "," + city + "," + state + "," + pincode);
                                hashMap.put("HospitalLogo", uri.toString());
                                hashMap.put("State", state);
                                hashMap.put("Phone", "+91" + phone);
                                hashMap.put("Registration no", registration);
                                hashMap.put("Pincode", pincode);
                                hashMap.put("status", "complete");

                                mRef.child(mUser.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        Toast.makeText(HospitalActivity.this, "Personal Detail Updated..", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(HospitalActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

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
