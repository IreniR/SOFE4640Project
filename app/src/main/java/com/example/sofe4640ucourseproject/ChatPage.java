package com.example.sofe4640ucourseproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class ChatPage extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, LocationListener {

    String name, description, sender, image;
    TextView receiverName;

    EditText textDesc;
    ImageButton send_message_btn, back, menu_btn;

    RecyclerView chatMessagesDisplay;
    MessageAdapter messageAdapter;
    ArrayList<Message> chatList;
    ArrayList<Message> sent_messages_List;
    ArrayList<Message> receive_message_List;
    Message newMessage;

    FirebaseFirestore db;
    FirebaseStorage storage;

    private Uri filePath;
    LocationManager locationManager;

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static final int PICK_IMAGE = 1;
    Uri imageUri;
    Uri videoUri;

    public static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);

        receiverName = findViewById(R.id.receiverText);
        receiverName.setText(name);

        back = findViewById(R.id.backButton);
        textDesc = findViewById(R.id.messageBox);
        send_message_btn = findViewById(R.id.send_msg_btn);

        menu_btn = findViewById(R.id.menu_btn);
        final PopupMenu popupMenu = new PopupMenu(
                this, menu_btn
        );

        chatList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        chatMessagesDisplay = findViewById(R.id.chatRecyclerView);
        chatMessagesDisplay.setHasFixedSize(true);

        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setStackFromEnd(true);
        chatMessagesDisplay.setLayoutManager(manager);

        messageAdapter = new MessageAdapter(getApplicationContext(), chatList);
        chatMessagesDisplay.setAdapter(messageAdapter);
        getData();
        setChatHistory();
        chatList = new ArrayList<>();

        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        if (id == R.id.show_audio) {
                            storeVideo();
                        } else if (id == R.id.show_gallery) {
                            getVideo();
                        } else if (id == R.id.show_location) {
                            requestLocationPermission();
                        } else {
                            return ChatPage.super.onOptionsItemSelected(menuItem);
                        }
                        return false;
                    }
                });

                popupMenu.show();
            }
        });


        messageAdapter.notifyDataSetChanged();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatPage.this, HomePage.class));
            }
        });

        send_message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setChatHistory();

                if (textDesc.getText().toString().isEmpty()) {
                    Toast.makeText(ChatPage.this, "Cannot Send Empty Text", Toast.LENGTH_SHORT).show();
                } else {
                    setMessageDetails();
                    textDesc.getText().clear();
                    setChatHistory();
                    Toast.makeText(ChatPage.this, "Message Sent", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getVideo() {
        StorageReference videoRef = FirebaseStorage.getInstance().getReference("Files/" + "1638331972852.mp4");

        final long ONE_MEGABYTE = 1024 * 1024;
        videoRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                FileOutputStream fos = null;
                try {
                    fos = openFileOutput("test.mp4", Context.MODE_PRIVATE);
                    fos.write(bytes);
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(ChatPage.this, "Uploaded file", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }


    public void storeVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 5);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            videoUri = data.getData();
            System.out.println("Uploading");
            uploadvideo();
        }
    }

    private String getfiletype(Uri videouri) {
        ContentResolver r = getContentResolver();
        // get the file type ,in this case its mp4
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(r.getType(videouri));
    }

    private void uploadvideo() {
        System.out.println(videoUri);
        if (videoUri != null) {
            // save the selected video in Firebase storage
            final StorageReference reference = FirebaseStorage.getInstance().getReference("Files/" + System.currentTimeMillis() + "." + getfiletype(videoUri));
            reference.putFile(videoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful()) ;
                    // get the link of video
                    String downloadUri = uriTask.getResult().toString();
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Video");
                    HashMap<String, String> map = new HashMap<>();
                    map.put("videolink", downloadUri);
                    reference1.child("" + System.currentTimeMillis()).setValue(map);
                    // Video uploaded successfully
                    // Dismiss dialog
                    Toast.makeText(ChatPage.this, "Video Uploaded!!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Error, Image not uploaded
                    Toast.makeText(ChatPage.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void requestLocationPermission() {
        System.out.println("Location Permission");
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            getLocation();
        }
    }

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("MissingPermission")
    public void getLocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu, popup.getMenu());
        popup.show();
    }

    private void setAudio() {
    }

    private void setImage() {
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return false;
    }

    private void setChatHistory() {
        setChatHistoryReceive(); // show it as receiver but shown as sender
        setChatHistorySent();
    }

    private void setChatHistoryReceive() {

        FirebaseFirestore.getInstance().collection("Messages").document(name).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                receive_message_List = new ArrayList<>();

                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Map<String, Object> friendsMap = documentSnapshot.getData();

                        for (Object value : friendsMap.values()) {
                            Gson gson = new Gson();
                            String json = gson.toJson(value);
                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(json);

                                if (jsonObject.get("receiver").equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                                    try {
                                        newMessage = new Message(jsonObject.get("message").toString(), jsonObject.get("sender").toString(), dateFormat.parse(jsonObject.get("timestamp").toString()), jsonObject.get("location").toString());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }


                                    receive_message_List.add(newMessage);

                                    messageAdapter = new MessageAdapter(ChatPage.this, chatList);
                                    chatMessagesDisplay.setAdapter(messageAdapter);
                                    messageAdapter.notifyDataSetChanged();
                                    System.out.println("It gets the senders email");

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        chatList.addAll(receive_message_List);
                    }
                }
            }
        });
        messageAdapter.notifyDataSetChanged();

        chatMessagesDisplay.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        Message m = chatList.get(position);
                        System.out.println("Location: " + m.getLocationBoolaen());
                        if (m.getLocationBoolaen()) {
                            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + m.message);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
                        }
                    }
                })
        );
    }

    private void setChatHistorySent() {
        FirebaseFirestore.getInstance().collection("Messages").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                sent_messages_List = new ArrayList<>();
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Map<String, Object> friendsMap = documentSnapshot.getData();
                        for (Object value : friendsMap.values()) {
                            Gson gson = new Gson();
                            String json = gson.toJson(value);
                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(json);

                                if (jsonObject.get("receiver").equals(name)) {
                                    try {
                                        newMessage = new Message(jsonObject.get("message").toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail(), dateFormat.parse(jsonObject.get("timestamp").toString()), jsonObject.get("location").toString());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    sent_messages_List.add(newMessage);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        chatList.clear();
                        chatList.addAll(receive_message_List);
                        chatList.addAll(sent_messages_List);
                        Collections.sort(chatList, Comparator.comparing(Message::getTimestamp));
                        System.out.println(chatList);
                        messageAdapter = new MessageAdapter(ChatPage.this, chatList);
                        chatMessagesDisplay.setAdapter(messageAdapter);
                        messageAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        messageAdapter.notifyDataSetChanged();
    }

    private void setMessageDetails() {
        Map<String, Object> messageRec = new HashMap<>();
        Map<String, Object> message = new HashMap<>();
        UUID hash = UUID.randomUUID();
        message.put("message", textDesc.getText().toString());
        message.put("audio", "Sample audio");
        message.put("image", "sample image");
        message.put("receiver", name);
        message.put("location", false);
        message.put("sender", FirebaseAuth.getInstance().getCurrentUser().getEmail());
        Calendar cal = Calendar.getInstance();
        message.put("timestamp", dateFormat.format(cal.getTime()));

        messageRec.put(hash.toString(), message);
        db.collection("Messages").document(sender).set(messageRec, SetOptions.merge());
        messageAdapter.notifyDataSetChanged();
    }

    private void getData() {
        if (getIntent().hasExtra("myImage") && getIntent().hasExtra("description") && getIntent().hasExtra("receiver")) {
            name = getIntent().getStringExtra("receiver");
            description = getIntent().getStringExtra("description");
            image = getIntent().getStringExtra("myImage");
            sender = getIntent().getStringExtra("sender");
        } else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        messageAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        messageAdapter.notifyDataSetChanged();
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (Geocoder.isPresent()) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            List<Address> ls = null;
            try {
                ls = geocoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String city = ls.get(0).getLocality();
            String prov = ls.get(0).getAdminArea();
            String country = ls.get(0).getCountryName();
            String address = city + ", " + prov + ", " + country;

            addLocationAddress(address);
        }

    }

    public void addLocationAddress(String address) {
        Map<String, Object> messageRec = new HashMap<>();
        Map<String, Object> message = new HashMap<>();
        UUID hash = UUID.randomUUID();
        message.put("message", address);
        message.put("location", true);
        message.put("audio", "Sample audio");
        message.put("image", "sample image");
        message.put("receiver", name);
        message.put("sender", FirebaseAuth.getInstance().getCurrentUser().getEmail());
        Calendar cal = Calendar.getInstance();
        message.put("timestamp", dateFormat.format(cal.getTime()));

        messageRec.put(hash.toString(), message);
        db.collection("Messages").document(sender).set(messageRec, SetOptions.merge());
        messageAdapter.notifyDataSetChanged();
        setChatHistory();
    }
}