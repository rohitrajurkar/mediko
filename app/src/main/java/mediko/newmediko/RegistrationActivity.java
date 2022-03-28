package mediko.newmediko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import mediko.newmediko.Utility.NetworkChangeListener;

public class RegistrationActivity extends AppCompatActivity {

    private EditText numberR, councile, year;
    private ImageView back;
    private TextView save;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mRef;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        numberR = findViewById(R.id.regigtrationNumber);
        councile = findViewById(R.id.regigtrationConuncile);
        year = findViewById(R.id.regigtrationYear);

        back = findViewById(R.id.backRegiDetail);
        save = findViewById(R.id.saveRegiDetail);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("Registration Detail");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = numberR.getText().toString();
                String councileR = councile.getText().toString();
                String yearR = year.getText().toString();

                if (number.isEmpty()) {
                    numberR.setError("Required field");
                } else if (councileR.isEmpty()) {
                    councile.setError("Required field");
                } else if (yearR.isEmpty()) {
                    year.setError("Required field");
                } else {
                    HashMap hashMap = new HashMap();
                    hashMap.put("Registration Number", number);
                    hashMap.put("Registration Councile", councileR);
                    hashMap.put("Registration Year", yearR);

                    mRef.child(mUser.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(RegistrationActivity.this, DetailActivity.class);
                                intent.putExtra("Detail", number);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
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