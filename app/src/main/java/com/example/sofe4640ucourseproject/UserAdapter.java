package com.example.sofe4640ucourseproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    Context context;
    String names[];
    String description[];
    int[] images;
    private FirebaseAuth mAuth;

    public UserAdapter(Context ct, String s1[], String[] s2, int[] img){
        context = ct;
        names = s1;
        description = s2;
        images = img;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_layout, parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.NameTextview.setText(names[position]);
        holder.DescriptionTextView.setText(description[position]);
        holder.myimage.setImageResource(images[position]);
        String nam = names[position];
        String dis = description[position];
        int im = images[position];

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();

                Intent intent = new Intent(context, ChatPage.class);
                intent.putExtra("receiver",nam);
                intent.putExtra("description", dis);
                intent.putExtra("myImage", im);
                intent.putExtra("sender",currentUser.getEmail());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView NameTextview, DescriptionTextView;
        ImageView myimage;
        ConstraintLayout mainLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            NameTextview = itemView.findViewById(R.id.tv_name);
            DescriptionTextView = itemView.findViewById(R.id.tv_description);
            myimage = itemView.findViewById(R.id.my_image_View);
            mainLayout = itemView.findViewById(R.id.main_Layout);
        }
    }
}
