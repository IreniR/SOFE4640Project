package com.example.sofe4640ucourseproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter {

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    Context context;

    ArrayList<Message> chatMessages;

    private static final int SENT = 0;
    private static final int RECEIVE = 1;

    public MessageAdapter(Context context, ArrayList<Message> chatMessages){
        this.context = context;
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        context = parent.getContext();

        View view;
        if(viewType == SENT){
            view = LayoutInflater.from(context).inflate(R.layout.sent, parent, false);
            return new SenderMessageHolder(view);

        } else{
            view = LayoutInflater.from(context).inflate(R.layout.recieve, parent, false);
            return new ReceiverMessageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Message messages = chatMessages.get(position);

        if(holder.getClass() == SenderMessageHolder.class){
            SenderMessageHolder viewHolder = (SenderMessageHolder)holder;
            viewHolder.textMessageDescription.setText(messages.getMessage());
        }else{
            ReceiverMessageHolder viewHolder = (ReceiverMessageHolder)holder;
            viewHolder.textMessageDescription.setText(messages.getMessage());
        }
    }

    @Override
    public int getItemViewType(int position){
        Message message = chatMessages.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(message.getSenderId())){
            return SENT;
        }else{
            return RECEIVE;
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
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
