package mediko.newmediko.Hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import mediko.newmediko.DivisionActivity;
import mediko.newmediko.PhoneActivity;
import mediko.newmediko.R;
import mediko.newmediko.Utility.NetworkChangeListener;

public class HosRegisterActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    boolean isEmailValid, isPhoneValid, isPasswordValid, isNamevalid;
    TextInputLayout emailError, phoneError, passError, nameError;
    EditText email, password, phone, name;
    Button signUp;
    ImageView back;
    ProgressDialog progressDialog;
    FirebaseAuth fAuth;
    FirebaseUser user;
    DatabaseReference mRef;
    Dialog dialog;
    TextView textView;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hos_register);
        emailError = findViewById(R.id.docEmailLogin);
        nameError = findViewById(R.id.docNameLogin);
        passError = findViewById(R.id.docPasswordLogin);
        phoneError = findViewById(R.id.docPhoneRegisster);
        email = findViewById(R.id.rEmail);
        password = findViewById(R.id.rPassword);
        phone = findViewById(R.id.rPhone);
        name = findViewById(R.id.rName);
        signUp = findViewById(R.id.docbtnRegister);
        back = findViewById(R.id.docBackLogin);
        progressDialog = new ProgressDialog(this);
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateUser();
            }
        });

    }

    private void CreateUser() {
        String et_email = email.getText().toString();
        String et_password = password.getText().toString();
        String et_phone = phone.getText().toString();
        String et_name = phone.getText().toString();

        // Check for a valid email address.
        if (et_name.isEmpty()) {
            nameError.setError("Please enter your name.");
        } else if (email.getText().toString().isEmpty()) {
            emailError.setError(getResources().getString(R.string.email_error));
            isEmailValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            emailError.setError(getResources().getString(R.string.error_invalid_email));
            isEmailValid = false;
        } else {
            isEmailValid = true;
            emailError.setErrorEnabled(false);
        }

        // Check for a valid phone number.
        if (phone.getText().toString().isEmpty()) {
            phoneError.setError(getResources().getString(R.string.phone_error));
            isPhoneValid = false;
        } else if (phone.getText().length() != 10) {
            phoneError.setError("Please enter valid number");
            isPhoneValid = false;

        } else {
            isPhoneValid = true;
            phoneError.setErrorEnabled(false);
        }

        // Check for a valid password.
        if (password.getText().toString().isEmpty()) {
            passError.setError(getResources().getString(R.string.password_error));
            isPasswordValid = false;
        } else if (password.getText().length() < 6) {
            passError.setError(getResources().getString(R.string.error_invalid_password));
            isPasswordValid = false;
        } else {
            isPasswordValid = true;
            passError.setErrorEnabled(false);
        }

        if (isEmailValid && isPhoneValid && isPasswordValid) {
            dialog = new Dialog(HosRegisterActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_wait);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            // proceed with the registration of the user
            fAuth.createUserWithEmailAndPassword(et_email, et_password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    fAuth.getCurrentUser().sendEmailVerification();
                    // send the user to verify the phone
                    HashMap hashMap = new HashMap();
                    hashMap.put("statue", "Hospital");
                    hashMap.put("email", et_email);
                    hashMap.put("name", et_name);
                    hashMap.put("phone", "+91" + et_phone);

                    mRef.child(fAuth.getCurrentUser().getUid()).updateChildren(hashMap);
                    Intent phone = new Intent(HosRegisterActivity.this, DivisionActivity.class);
                    startActivity(phone);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(HosRegisterActivity.this, "Error !" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        textView = findViewById(R.id.textViewLink);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

    }

    private void showError(TextInputLayout field, String s) {
        field.setError(s);
        field.requestFocus();
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