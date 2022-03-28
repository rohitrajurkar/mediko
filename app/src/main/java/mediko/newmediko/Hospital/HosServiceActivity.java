package mediko.newmediko.Hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import mediko.newmediko.R;

public class HosServiceActivity extends AppCompatActivity {

    EditText service;
    TextView save;
    FirebaseAuth fAuth;
    FirebaseUser mUser;
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hos_service);

        service = findViewById(R.id.enterService);
        save = findViewById(R.id.btnservice);
        fAuth = FirebaseAuth.getInstance();
        mUser = fAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("Services");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ser = service.getText().toString();

                if (ser.isEmpty()) {
                    service.setError("Required field.");
                } else {
                    String key = mRef.push().getKey();
                    HashMap hashMap = new HashMap();
                    hashMap.put("Service", ser);

                    mRef.child(mUser.getUid()).child(key).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }
}