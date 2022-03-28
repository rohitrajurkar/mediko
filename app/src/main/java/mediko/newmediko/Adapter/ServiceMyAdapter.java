package mediko.newmediko.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mediko.newmediko.R;
import mediko.newmediko.Utils.service;

public class ServiceMyAdapter extends FirebaseRecyclerAdapter<service, ServiceMyAdapter.MyViewHolder> {
    public ServiceMyAdapter(FirebaseRecyclerOptions<service> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull service model) {
        holder.service.setText(model.getService());
        String id = getRef(position).getKey();
        DatabaseReference dRef= FirebaseDatabase.getInstance().getReference().child("Services");
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dRef.child(user.getUid()).child(id).removeValue();
            }
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singal_service, parent, false);
        return new ServiceMyAdapter.MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView service;
        ImageView remove;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            service = itemView.findViewById(R.id.services);
            remove = itemView.findViewById(R.id.removeService);
        }
    }
}
