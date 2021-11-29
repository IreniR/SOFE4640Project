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

    private static final int SENT = 0;
    private static final int RECEIVE = 1;

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

    //maybe this isn't right... doesnt go to here
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Message messages = chatMessagesArrayList.get(position);

//        switch (holder.getItemViewType()){
//            case SENT:
//                ((SenderMessageHolder) holder).bind(messages);
//            case RECEIVE:
//                ((ReceiverMessageHolder) holder).bind(messages);
//        }
        if(holder.getClass() == SenderMessageHolder.class){
            SenderMessageHolder viewHolder = (SenderMessageHolder)holder;
            viewHolder.textMessageDescription.setText(messages.getMessage());
            Toast.makeText(context, messages.getMessage(), Toast.LENGTH_SHORT).show();
        } else{
            ReceiverMessageHolder viewHolder = (ReceiverMessageHolder)holder;
            viewHolder.textMessageDescription.setText(messages.getMessage());
        }
    }

    @Override
    public int getItemViewType(int position){
        Message message = chatMessagesArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(message.getSenderId())){
            //when current user is sender
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
        void bind(Message message){
            textMessageDescription.setText(message.getMessage());
        }
    }

    class ReceiverMessageHolder extends RecyclerView.ViewHolder {

        TextView textMessageDescription;

        public ReceiverMessageHolder(@NonNull View itemView) {
            super(itemView);

            textMessageDescription = itemView.findViewById(R.id.receive_message_view);
        }
        void bind(Message message){
            textMessageDescription.setText(message.getMessage());
        }
    }
}
