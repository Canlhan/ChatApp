package com.can.chat;

import static java.util.concurrent.TimeUnit.SECONDS;

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
import android.os.CountDownTimer;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class MainActivity extends AppCompatActivity  {
    private ActivityMainBinding binding;
    public ArrayList<User> users=new ArrayList<>();;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseUser user;
    Recyleradap adapter;

    private IData getData;



    private static final String ONESIGNAL_APP_ID = "1a15c98e-b31a-449c-91a6-e0fde5198130";





    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        getData=new Getdata();

        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();



         adapter=new Recyleradap(users);
         LinearLayoutManager layout=new LinearLayoutManager(getApplicationContext());
         layout.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(layout);

        binding.recyclerView.setAdapter(adapter);





        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        String us=OneSignal.getDeviceState().getUserId();


        getData.getDataFromFirestore(users,binding,adapter);


        getData.addToDbUserDeviceId(this,us);

        }



    public  void sendMessage(View view)
    {
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


        String message=binding.editmessage.getText().toString();
         binding.editmessage.setText("");
         user=auth.getCurrentUser();
        String userEmail=user.getEmail();



        HashMap<String,Object> mesajlar=new HashMap<>();

        mesajlar.put("mesaj",message);
        mesajlar.put("email",userEmail);
        mesajlar.put("date", FieldValue.serverTimestamp());


        getData.todb(mesajlar);

        getData.getDataFromFirestore(users,binding,adapter);

        //onesignal

       getData.pushNotification(this,message);


        new CountDownTimer(6000, 1000) {

            public void onTick(long millisUntilFinished) {
                binding.sendbtn.setVisibility(View.INVISIBLE);


            }

            public void onFinish() {
                binding.sendbtn.setVisibility(View.VISIBLE);
            }
        }.start();






    }


}