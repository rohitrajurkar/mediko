package mediko.newmediko.Hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import mediko.newmediko.Adapter.FriendMyViewHolder;
import mediko.newmediko.R;
import mediko.newmediko.Utility.NetworkChangeListener;
import mediko.newmediko.Utils.Friends;

public class HosDoctorActivity extends AppCompatActivity {

    Button addDoc;
    ImageView addDoctor, serverImage, back;
    TextView serverText;
    DatabaseReference dRef, tRef, mRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    private RecyclerView recyclerView;
    Dialog dialog;
    FirebaseRecyclerOptions<Friends> options;
    FirebaseRecyclerAdapter<Friends, FriendMyViewHolder> adapter;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hos_doctor);
        addDoc = findViewById(R.id.btnAddDoc);
        addDoctor = findViewById(R.id.addDoctor);
        addDoctor.setVisibility(View.INVISIBLE);
        serverImage = findViewById(R.id.serverimage);
        serverText = findViewById(R.id.serverText);
        recyclerView = findViewById(R.id.recycleDoc);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        back = findViewById(R.id.backAppointment);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        dRef = FirebaseDatabase.getInstance().getReference().child("HosDoctors");
        tRef = FirebaseDatabase.getInstance().getReference().child("DoctorTiming");
        mRef = FirebaseDatabase.getInstance().getReference().child("Doctors");

        dialog = new Dialog(HosDoctorActivity.this);
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
                    addDoctor.setVisibility(View.VISIBLE);
                    addDoc.setVisibility(View.GONE);
                    serverImage.setVisibility(View.GONE);
                    serverText.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HosDoctorActivity.this, HosDocDetailActivity.class));
            }
        });
        addDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HosDoctorActivity.this, HosDocDetailActivity.class));
            }
        });

        LoadFriend("");

    }

    private void LoadFriend(String s) {
        Query query = dRef.child(mUser.getUid()).orderByChild("Name").startAt(s).endAt(s + "\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<Friends>().setQuery(query, Friends.class).build();
        adapter = new FirebaseRecyclerAdapter<Friends, FriendMyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FriendMyViewHolder holder, int position, @NonNull Friends model) {
                Picasso.get().load(model.getProfileImage()).into(holder.profileImage);
                holder.username.setText(model.getName());
                holder.profession.setText(model.getSpecialization());
                holder.experience.setText(model.getExperience());
                holder.fees.setText(model.getFees());


                String id = getRef(position).getKey();
                holder.deleteProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dRef.child(mUser.getUid()).child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                }
                            }
                        });
                        tRef.child(mUser.getUid()).child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                }
                            }
                        });
                        mRef.child(id).removeValue();
                        Toast.makeText(HosDoctorActivity.this, "Data Deleted...", Toast.LENGTH_SHORT).show();
                    }
                });
                holder.viewProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HosDoctorActivity.this, HosDocProfileActivity.class);
                        intent.putExtra("otherUserId", getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public FriendMyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singal_doctor, parent, false);
                return new FriendMyViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
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