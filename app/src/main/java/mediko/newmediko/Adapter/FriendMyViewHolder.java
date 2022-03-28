package mediko.newmediko.Adapter;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import mediko.newmediko.R;

public class FriendMyViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView profileImage;
    public TextView username;
    public TextView profession;
    public TextView experience;
    public TextView fees;
    public Button viewProfile;
    public Button deleteProfile;

    public FriendMyViewHolder(@NonNull View itemView) {
        super(itemView);
        profileImage = itemView.findViewById(R.id.viewProfileImage);
        username = itemView.findViewById(R.id.viewdoctorName);
        profession = itemView.findViewById(R.id.viewSpeshalst);
        viewProfile = itemView.findViewById(R.id.btnViewProfile);
        deleteProfile = itemView.findViewById(R.id.docDelete);
        experience = itemView.findViewById(R.id.docexperoi);
        fees = itemView.findViewById(R.id.docfessview);
    }
}

