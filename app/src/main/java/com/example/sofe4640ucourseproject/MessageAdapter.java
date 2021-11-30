package com.example.sofe4640ucourseproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter{

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    Context context;

    ArrayList<Message> chatMessagesArrayList;

    private static final int SENT = 1;
    private static final int RECEIVE = 2;

    public MessageAdapter(Context context, ArrayList<Message> chatMessages){
        this.context = context;
        this.chatMessagesArrayList = chatMessages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        context = parent.getContext();

        View view;
        if(viewType == SENT){
            view = LayoutInflater.from(context).inflate(R.layout.sent, parent, false);
            return new SenderMessageHolder(view);
        } else if(viewType == RECEIVE){
            view = LayoutInflater.from(context).inflate(R.layout.recieve, parent, false);
            return new ReceiverMessageHolder(view);
        }
        return null;
    }

    public void update(ArrayList<Message> messages){
        chatMessagesArrayList.clear();
        chatMessagesArrayList = messages;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Message messages = chatMessagesArrayList.get(position);

        if(holder.getClass() == SenderMessageHolder.class){
            SenderMessageHolder viewHolder = (SenderMessageHolder)holder;
            viewHolder.textMessageDescription.setText(messages.getMessage());
        } else if(holder.getClass() == ReceiverMessageHolder.class){
            ReceiverMessageHolder viewHolder = (ReceiverMessageHolder)holder;
            viewHolder.textMessageDescription.setText(messages.getMessage());

        }
    }

    @Override
    public int getItemViewType(int position){

        Message message = chatMessagesArrayList.get(position);
//        Toast.makeText(context, position+" :" +message.getSenderId(), Toast.LENGTH_SHORT).show();
        if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(message.getSenderId())){
            return SENT;
        }else{
            return RECEIVE;
        }
    }

    @Override
    public int getItemCount() {
        return chatMessagesArrayList.size();
    }

    class SenderMessageHolder extends RecyclerView.ViewHolder {

        TextView textMessageDescription;

        public SenderMessageHolder(@NonNull View itemView) {
            super(itemView);

            textMessageDescription = itemView.findViewById(R.id.sent_message_view);
        }
    }

    class ReceiverMessageHolder extends RecyclerView.ViewHolder {

        TextView textMessageDescription;

        public ReceiverMessageHolder(@NonNull View itemView) {
            super(itemView);

            textMessageDescription = itemView.findViewById(R.id.receive_message_view);
        }
    }
}
