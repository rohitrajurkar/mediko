package mediko.newmediko.Hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mediko.newmediko.R;
import mediko.newmediko.Utility.NetworkChangeListener;

public class HosTimingActivity extends AppCompatActivity {

    private Switch monday, tuesday, wensday, thurday, friday, saturday, sunday;
    TextView save;
    private Spinner monday1, monday2, tuesday1, tuesday2, wensday1, wensday2, thurday1, thurday2, friday1, friday2, saturday1, saturday2, sunday1, sunday2;
    String et_monday1 = "Closed", et_monday2 = "Closed", et_tuesday = "Closed", et_tuesday1 = "Closed", et_wensday = "Closed", et_wensday1 = "Closed", et_thurday = "Closed", et_thurday1 = "Closed", et_friday = "Closed", et_friday1 = "Closed", et_saturday = "Closed", et_saturday1 = "Closed", et_sunday = "Closed", et_sunday1 = "Closed";

    Boolean isData = false;
    ImageView back;
    DatabaseReference dRef;
    FirebaseAuth fAuth;
    FirebaseUser mUser;
    ProgressDialog progressDialog;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hos_timing);
        back = findViewById(R.id.backClinicTime);
        monday = findViewById(R.id.switch1);
        tuesday = findViewById(R.id.switch2);
        wensday = findViewById(R.id.switch3);
        thurday = findViewById(R.id.switch4);
        friday = findViewById(R.id.switch5);
        saturday = findViewById(R.id.switch6);
        sunday = findViewById(R.id.switch7);
        save = findViewById(R.id.save_time);
        monday1 = findViewById(R.id.m_spinner);
        monday1.setVisibility(View.INVISIBLE);
        monday2 = findViewById(R.id.mo_spinner);
        monday2.setVisibility(View.INVISIBLE);
        tuesday1 = findViewById(R.id.spinner4);
        tuesday1.setVisibility(View.INVISIBLE);
        tuesday2 = findViewById(R.id.spinner5);
        tuesday2.setVisibility(View.INVISIBLE);
        wensday1 = findViewById(R.id.spinner2);
        wensday1.setVisibility(View.INVISIBLE);
        wensday2 = findViewById(R.id.spinner28);
        wensday2.setVisibility(View.INVISIBLE);
        thurday1 = findViewById(R.id.spinner29);
        thurday1.setVisibility(View.INVISIBLE);
        thurday2 = findViewById(R.id.spinner30);
        thurday2.setVisibility(View.INVISIBLE);
        friday1 = findViewById(R.id.spinner31);
        friday1.setVisibility(View.INVISIBLE);
        friday2 = findViewById(R.id.spinner32);
        friday2.setVisibility(View.INVISIBLE);
        saturday1 = findViewById(R.id.spinner33);
        saturday1.setVisibility(View.INVISIBLE);
        saturday2 = findViewById(R.id.spinner34);
        saturday2.setVisibility(View.INVISIBLE);
        sunday1 = findViewById(R.id.spinner35);
        sunday1.setVisibility(View.INVISIBLE);
        sunday2 = findViewById(R.id.spinner36);
        sunday2.setVisibility(View.INVISIBLE);


        fAuth = FirebaseAuth.getInstance();
        mUser = fAuth.getCurrentUser();
        dRef = FirebaseDatabase.getInstance().getReference().child("HospitalTiming");
        progressDialog = new ProgressDialog(this);

        List<String> categories = new ArrayList<>();

        categories.add(0, "12 am");
        categories.add("1 am");
        categories.add("2 am");
        categories.add("3 am");
        categories.add("4 am");
        categories.add("5 am");
        categories.add("6 am");
        categories.add("7 am");
        categories.add("8 am");
        categories.add("9 am");
        categories.add("10 am");
        categories.add("11 am");
        categories.add("12 pm");
        categories.add("1 pm");
        categories.add("2 pm");
        categories.add("3 pm");
        categories.add("4 pm");
        categories.add("5 pm");
        categories.add("6 pm");
        categories.add("7 pm");
        categories.add("8 pm");
        categories.add("9 pm");
        categories.add("10 pm");
        categories.add("11 pm");
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        monday1.setAdapter(dataAdapter);
        monday2.setAdapter(dataAdapter);
        tuesday1.setAdapter(dataAdapter);
        tuesday2.setAdapter(dataAdapter);
        wensday1.setAdapter(dataAdapter);
        wensday2.setAdapter(dataAdapter);
        thurday1.setAdapter(dataAdapter);
        thurday2.setAdapter(dataAdapter);
        friday1.setAdapter(dataAdapter);
        friday2.setAdapter(dataAdapter);
        saturday1.setAdapter(dataAdapter);
        saturday2.setAdapter(dataAdapter);
        sunday1.setAdapter(dataAdapter);
        sunday2.setAdapter(dataAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String et_mo = snapshot.child("Monday").getValue().toString();
                    String et_tu = snapshot.child("Tuesday").getValue().toString();
                    String et_we = snapshot.child("Wensday").getValue().toString();
                    String et_th = snapshot.child("Thursday").getValue().toString();
                    String et_fr = snapshot.child("Friday").getValue().toString();
                    String et_sa = snapshot.child("Saturday").getValue().toString();
                    String et_su = snapshot.child("Sunday").getValue().toString();
                    if (et_mo.equals("Closed to Closed")) {

                    } else {
                        monday.setChecked(true);
                    }
                    if (et_tu.equals("Closed to Closed")) {

                    } else {
                        tuesday.setChecked(true);
                    }
                    if (et_we.equals("Closed to Closed")) {

                    } else {
                        wensday.setChecked(true);
                    }
                    if (et_th.equals("Closed to Closed")) {

                    } else {
                        thurday.setChecked(true);
                    }
                    if (et_fr.equals("Closed to Closed")) {

                    } else {
                        friday.setChecked(true);
                    }
                    if (et_sa.equals("Closed to Closed")) {

                    } else {
                        saturday.setChecked(true);
                    }
                    if (et_su.equals("Closed to Closed")) {

                    } else {
                        sunday.setChecked(true);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        monday1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (parent.getItemAtPosition(i).equals("choose time")) {

                } else {
                    String item = parent.getItemAtPosition(i).toString();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        monday2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (parent.getItemAtPosition(i).equals("choose time")) {

                } else {
                    String item = parent.getItemAtPosition(i).toString();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tuesday1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (parent.getItemAtPosition(i).equals("choose time")) {

                } else {
                    String item = parent.getItemAtPosition(i).toString();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tuesday2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (parent.getItemAtPosition(i).equals("choose time")) {

                } else {
                    String item = parent.getItemAtPosition(i).toString();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        wensday1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (parent.getItemAtPosition(i).equals("choose time")) {

                } else {
                    String item = parent.getItemAtPosition(i).toString();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        wensday2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (parent.getItemAtPosition(i).equals("choose time")) {

                } else {
                    String item = parent.getItemAtPosition(i).toString();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        thurday1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (parent.getItemAtPosition(i).equals("choose time")) {

                } else {
                    String item = parent.getItemAtPosition(i).toString();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        thurday2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (parent.getItemAtPosition(i).equals("choose time")) {

                } else {
                    String item = parent.getItemAtPosition(i).toString();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        friday1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (parent.getItemAtPosition(i).equals("choose time")) {

                } else {
                    String item = parent.getItemAtPosition(i).toString();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        friday2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (parent.getItemAtPosition(i).equals("choose time")) {

                } else {
                    String item = parent.getItemAtPosition(i).toString();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        saturday1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (parent.getItemAtPosition(i).equals("choose time")) {

                } else {
                    String item = parent.getItemAtPosition(i).toString();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        saturday2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (parent.getItemAtPosition(i).equals("choose time")) {

                } else {
                    String item = parent.getItemAtPosition(i).toString();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sunday1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (parent.getItemAtPosition(i).equals("choose time")) {

                } else {
                    String item = parent.getItemAtPosition(i).toString();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sunday2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (parent.getItemAtPosition(i).equals("choose time")) {

                } else {
                    String item = parent.getItemAtPosition(i).toString();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        monday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (monday.isChecked()) {
                    monday1.setVisibility(View.VISIBLE);
                    monday2.setVisibility(View.VISIBLE);
                } else {
                    monday1.setVisibility(View.INVISIBLE);
                    monday2.setVisibility(View.INVISIBLE);
                }
            }
        });
        tuesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (tuesday.isChecked()) {

                    tuesday1.setVisibility(View.VISIBLE);
                    tuesday2.setVisibility(View.VISIBLE);
                } else {
                    tuesday1.setVisibility(View.INVISIBLE);
                    tuesday2.setVisibility(View.INVISIBLE);
                }

            }
        });
        wensday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (wensday.isChecked()) {
                    wensday1.setVisibility(View.VISIBLE);
                    wensday2.setVisibility(View.VISIBLE);
                } else {
                    wensday1.setVisibility(View.INVISIBLE);
                    wensday2.setVisibility(View.INVISIBLE);
                }
            }
        });
        thurday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (thurday.isChecked()) {
                    thurday1.setVisibility(View.VISIBLE);
                    thurday2.setVisibility(View.VISIBLE);
                } else {
                    thurday1.setVisibility(View.INVISIBLE);
                    thurday2.setVisibility(View.INVISIBLE);
                }
            }
        });
        friday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (friday.isChecked()) {
                    friday1.setVisibility(View.VISIBLE);
                    friday2.setVisibility(View.VISIBLE);
                } else {
                    friday1.setVisibility(View.INVISIBLE);
                    friday2.setVisibility(View.INVISIBLE);
                }
            }
        });
        saturday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (saturday.isChecked()) {
                    saturday1.setVisibility(View.VISIBLE);
                    saturday2.setVisibility(View.VISIBLE);
                } else {
                    saturday1.setVisibility(View.INVISIBLE);
                    saturday2.setVisibility(View.INVISIBLE);
                }
            }
        });
        sunday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (sunday.isChecked()) {
                    sunday1.setVisibility(View.VISIBLE);
                    sunday2.setVisibility(View.VISIBLE);
                } else {
                    sunday1.setVisibility(View.INVISIBLE);
                    sunday2.setVisibility(View.INVISIBLE);
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (monday.isChecked()) {
                    et_monday1 = monday1.getSelectedItem().toString();
                    et_monday2 = monday2.getSelectedItem().toString();
                    validData(et_monday1);
                    validData(et_monday2);
                }
                if (thurday.isChecked()) {

                    et_thurday = thurday1.getSelectedItem().toString();
                    et_thurday1 = thurday2.getSelectedItem().toString();
                    validData(et_thurday);
                    validData(et_thurday1);


                }
                if (tuesday.isChecked()) {

                    et_tuesday = tuesday1.getSelectedItem().toString();
                    et_tuesday1 = tuesday2.getSelectedItem().toString();
                    validData(et_tuesday);
                    validData(et_tuesday1);

                }
                if (wensday.isChecked()) {

                    et_wensday = wensday1.getSelectedItem().toString();
                    et_wensday1 = wensday2.getSelectedItem().toString();
                    validData(et_wensday);
                    validData(et_wensday1);

                }
                if (friday.isChecked()) {

                    et_friday = friday1.getSelectedItem().toString();
                    et_friday1 = friday2.getSelectedItem().toString();
                    validData(et_friday);
                    validData(et_friday1);

                }
                if (saturday.isChecked()) {

                    et_saturday = saturday1.getSelectedItem().toString();
                    et_saturday1 = saturday2.getSelectedItem().toString();
                    validData(et_saturday);
                    validData(et_saturday1);

                }
                if (sunday.isChecked()) {

                    et_sunday = sunday1.getSelectedItem().toString();
                    et_sunday1 = sunday2.getSelectedItem().toString();
                    validData(et_sunday);
                    validData(et_sunday1);

                }
                if (isData) {


                    Map<String, Object> data = new HashMap<>();
                    data.put("Monday", et_monday1 + " to " + et_monday2);
                    data.put("Tuesday", et_tuesday + " to " + et_tuesday1);
                    data.put("Wensday", et_wensday + " to " + et_wensday1);
                    data.put("Thursday", et_thurday + " to " + et_thurday1);
                    data.put("Friday", et_friday + " to " + et_friday1);
                    data.put("Saturday", et_saturday + " to " + et_saturday1);
                    data.put("Sunday", et_sunday + " to " + et_sunday1);
                    data.put("status", "complete");

                    dRef.child(mUser.getUid()).updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(HosTimingActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                }

            }

        });
    }

    private void validData(String check) {
        if (TextUtils.isEmpty(check)) {
            isData = false;
            Toast.makeText(this, "Empty credentials", Toast.LENGTH_SHORT).show();
        } else {
            isData = true;
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