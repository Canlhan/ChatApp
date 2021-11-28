package com.can.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;
import android.view.autofill.AutofillValue;
import android.widget.Toast;

import com.can.chat.Adapter.Recyleradap;
import com.can.chat.databinding.ActivityMainBinding;
import com.can.chat.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    public ArrayList<User> users=new ArrayList<>();;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseUser user;
    Recyleradap adapter;
    User userme;





    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);


        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();



         adapter=new Recyleradap(users);
         LinearLayoutManager layout=new LinearLayoutManager(getApplicationContext());
         layout.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(layout);

        binding.recyclerView.setAdapter(adapter);




        getDataFromFirestore();



    }
    public  void sendMessage(View view)
    {

        String message=binding.editmessage.getText().toString();
         binding.editmessage.setText("");
         user=auth.getCurrentUser();
        String userEmail=user.getEmail();



        HashMap<String,Object> mesajlar=new HashMap<>();

        mesajlar.put("mesaj",message);
        mesajlar.put("email",userEmail);
        mesajlar.put("date", FieldValue.serverTimestamp());


        todb(mesajlar);

        getDataFromFirestore();






    }
    private  void todb(HashMap<String, Object> messages){
        firestore.collection("Messages")
                .add(messages).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                System.out.println("eklendi");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void getDataFromFirestore(){
        CollectionReference reference=firestore.collection("Messages");



        reference.orderBy("date", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                users.clear();
                for(DocumentSnapshot snapshot:value.getDocuments()){

                    Map<String,Object> data=snapshot.getData();

                    String email= (String) data.get("email");
                    String message=(String) data.get("mesaj");

                    User user1=new User(email,message);
                    users.add(user1);
                    binding.recyclerView.smoothScrollToPosition(users.size()-1);
                    adapter.notifyDataSetChanged();
                    System.out.println("GET DATA : "+users.size());



                }

            }
        });


    }
}