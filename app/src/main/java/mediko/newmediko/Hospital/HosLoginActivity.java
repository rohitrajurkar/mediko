package mediko.newmediko.Hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import mediko.newmediko.AppActivity;
import mediko.newmediko.LoginActivity;
import mediko.newmediko.MainActivity;
import mediko.newmediko.R;
import mediko.newmediko.Utility.NetworkChangeListener;

public class HosLoginActivity extends AppCompatActivity {

    TextInputLayout email, password;
    TextView forgetPassword, txtRegister, newMediko;
    Button login;
    FirebaseAuth mAuth;
    Dialog dialog;
    ImageView back;

    TextView textView;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hos_login);
        email = findViewById(R.id.docEmailLogin);
        password = findViewById(R.id.docPasswordLogin);
        forgetPassword = findViewById(R.id.forgetPassword);
        login = findViewById(R.id.docbtnLogin);
        mAuth = FirebaseAuth.getInstance();
        txtRegister = findViewById(R.id.registerPage);
        back = findViewById(R.id.backLogin);
        newMediko = findViewById(R.id.newUser);

        newMediko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HosLoginActivity.this, HosRegisterActivity.class));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String et_email = email.getEditText().getText().toString();
                String et_passwprd = password.getEditText().getText().toString();

                if (et_email.isEmpty()) {
                    showError(email, "Requied Field");
                } else if (et_passwprd.isEmpty()) {
                    showError(password, "Required Field");
                } else {
                    dialog = new Dialog(HosLoginActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_wait);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    mAuth.signInWithEmailAndPassword(et_email, et_passwprd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                dialog.dismiss();
                                Toast.makeText(HosLoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(HosLoginActivity.this, HosStartActivity.class));
                                finish();
                            } else {
                                dialog.dismiss();
                                Toast.makeText(HosLoginActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
                }
            }
        });
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email To Received Reset Link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(HosLoginActivity.this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(HosLoginActivity.this, "Sorry, this mail id is not registered with us, Please sign up!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });

                passwordResetDialog.create().show();

            }
        });
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HosLoginActivity.this, HosRegisterActivity.class));

            }
        });
        textView = findViewById(R.id.textViewLink);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }


    private void showError(TextInputLayout input, String data) {
        input.setError(data);
        input.requestFocus();
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
            if (user.isEmailVerified()) {
                Intent intent = new Intent(HosLoginActivity.this, HosStartActivity.class);
                startActivity(intent);
                finish();
            }else{

            }
        }
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}