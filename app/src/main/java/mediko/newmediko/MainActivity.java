package mediko.newmediko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mediko.newmediko.Hospital.HosLoginActivity;
import mediko.newmediko.Hospital.HosStartActivity;

public class MainActivity extends AppCompatActivity {

    Button doc, hos;

    FirebaseAuth mAuth;
    TextView identify;
    DatabaseReference mRef;
    FirebaseUser user;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        doc = findViewById(R.id.btnDoc);
        hos = findViewById(R.id.btnHos);
        mAuth = FirebaseAuth.getInstance();
        identify = findViewById(R.id.identify);


        dialog = new Dialog(MainActivity.this);
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


        doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
        hos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HosLoginActivity.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mRef = FirebaseDatabase.getInstance().getReference().child("Users");
            user = FirebaseAuth.getInstance().getCurrentUser();
            mRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String str = snapshot.child("statue").getValue().toString();

                        if (str.equals("Hospital")) {
                            startActivity(new Intent(MainActivity.this, HosLoginActivity.class));
                            finish();

                        } else if (str.equals("Doctor")) {
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                        }
                        identify.setText(str);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }
}