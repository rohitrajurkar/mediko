package mediko.newmediko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class PlashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plash);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(PlashActivity.this, MainActivity.class));
                finish();

            }
        };

        Handler handler = new Handler();
        handler.postDelayed(runnable, 5000);
    }
}