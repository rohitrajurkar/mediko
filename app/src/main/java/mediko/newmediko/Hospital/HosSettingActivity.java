package mediko.newmediko.Hospital;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import mediko.newmediko.R;
import mediko.newmediko.SettingActivity;
import mediko.newmediko.Utility.EmailActivity;
import mediko.newmediko.Utility.NetworkChangeListener;

public class HosSettingActivity extends AppCompatActivity {

    TextView txtAccount, txtNotification, rate,invite;
    ImageView back, sendEmail;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hos_setting);
        txtAccount = findViewById(R.id.account);
        txtNotification = findViewById(R.id.notification);
        back = findViewById(R.id.settingBack);
        sendEmail = findViewById(R.id.sendEmail);
        rate = findViewById(R.id.rate);
        invite = findViewById(R.id.invite);
        rate.setMovementMethod(LinkMovementMethod.getInstance());
        invite.setMovementMethod(LinkMovementMethod.getInstance());

        txtAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HosSettingActivity.this, HosAccountActivity.class));
            }
        });
        txtNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HosSettingActivity.this, HosNotificationActivity.class));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HosSettingActivity.this, EmailActivity.class));
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