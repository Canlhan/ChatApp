package com.can.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
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
import com.onesignal.OSInAppMessageAction;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationReceivedEvent;
import com.onesignal.OSPermissionObserver;
import com.onesignal.OSPermissionStateChanges;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity  {
    private ActivityMainBinding binding;
    public ArrayList<User> users=new ArrayList<>();;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseUser user;
    Recyleradap adapter;
    User userme;
    private  ArrayList<String> userKimlik=new ArrayList<>();


    private static final String ONESIGNAL_APP_ID = "1a15c98e-b31a-449c-91a6-e0fde5198130";





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
        // Enable verbose OneSignal logging to debug issues if needed.

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        String us=OneSignal.getDeviceState().getUserId();







        HashMap<String,String > userIds=new HashMap<>();
        UUID uu=UUID.randomUUID();
        userIds.put("userId",us);

        CollectionReference reference=firestore.collection("Player");


            System.out.println("metoda girdi");
            reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(error!=null){
                        Toast.makeText(MainActivity.this, " id hata", Toast.LENGTH_SHORT).show();
                    }

                    if(value!=null){

                        ArrayList<String> userId =new ArrayList<>();
                        for(DocumentSnapshot snapshot:value.getDocuments()){
                            Map<String, Object> userid= snapshot.getData();
                            String Id= (String)userid.get("userId");
                            userId.add(Id);

                            System.out.println(" reference addsnopshot user id: "+userId.size());

                        }
                        if(!userId.contains(us)){
                            reference.add(userIds);
                        }
                    }
                }

            });
        }











    public  void sendMessage(View view)
    {

        final boolean[] messageControl = {true};
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

        //onesignal

        CollectionReference reference=firestore.collection("Player");


        System.out.println("metoda girdi");
        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Toast.makeText(MainActivity.this, " id hata", Toast.LENGTH_SHORT).show();
                }

                if(value!=null){


                    for(DocumentSnapshot snapshot:value.getDocuments()){
                        Map<String, Object> userid= snapshot.getData();
                         String Id= (String)userid.get("userId");
                        //userId.add(Id);


                        if(!OneSignal.getDeviceState().getUserId().equals(Id)){
                            try {

                                System.out.println(" sendmessages: "+Id);
                                OneSignal.postNotification(
                                        new JSONObject("{'contents': {'en':'"+message+"'},'include_player_ids': ['"+Id+"']}") ,null);






                            }catch (Exception e){

                            }
                        }









                    }


                }
            }

        });



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




                }

            }
        });


    }

    private ArrayList<String> getUserIdFromDatabase(String player){

        return null;


    }

}