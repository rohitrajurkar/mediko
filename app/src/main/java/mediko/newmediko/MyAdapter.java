package mediko.newmediko;

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
import com.google.firebase.database.FirebaseDatabase;

import mediko.newmediko.Utils.model;


public class MyAdapter extends FirebaseRecyclerAdapter<model, MyAdapter.MyViewHolder> {

    public MyAdapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singal_seciality, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull model model) {

        holder.txtSpeciality.setText(model.getSpecalization());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Speciality").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(getRef(position).getKey()).removeValue();

            }
        });
    }


//Intent intent = new Intent(SpecalityActivity.this, RegistrationActivity.class);
//                startActivity(intent);

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView delete;
        TextView txtSpeciality;

        public MyViewHolder(View view) {
            super(view);
            delete = (ImageView) view.findViewById(R.id.imageRemove);
            txtSpeciality = (TextView) view.findViewById(R.id.txtSpecialitytxt);
        }
    }
}
