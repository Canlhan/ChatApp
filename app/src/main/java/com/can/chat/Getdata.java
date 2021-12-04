package com.can.chat;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.can.chat.Adapter.Recyleradap;
import com.can.chat.databinding.ActivityMainBinding;
import com.can.chat.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Getdata implements IData
{
    private static FirebaseFirestore firestore=FirebaseFirestore.getInstance();;
    /*
   private  static ArrayList<User> users=new ArrayList<>();;
   private static Recyleradap adapter=new Recyleradap(users);
   */
    /*
    public static ArrayList<User> getData(){
        CollectionReference reference=firestore.collection("Messages");



        reference.orderBy("date", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                users.clear();
                for(DocumentSnapshot snapshot:queryDocumentSnapshots.getDocuments()){

                    Map<String,Object> data=snapshot.getData();

                    String email= (String) data.get("email");
                    String message=(String) data.get("mesaj");
                    User user1=new User(email,message);
                    users.add(user1);

                    System.out.println("GET DATA : "+users.size());



                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        return users;

    }
*/
    public    void todb(Map<String,Object> messages){
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
    public  void pushNotification(Context context,String message)
    {
        CollectionReference reference=firestore.collection("Player");


        System.out.println("metoda girdi");
        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Toast.makeText(context.getApplicationContext(), " id hata", Toast.LENGTH_SHORT).show();
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
                                        new JSONObject("{'contents':" +
                                                " {'en':'"+message+"'},'include_player_ids': ['"+Id+"']}"
                                                +
                                                "{\"image\": "+R.drawable.ic_stat_onesignal_default+"}") ,null);






                            }catch (Exception e){

                            }
                        }
                      }


                }
            }

        });
    }

    public  void getDataFromFirestore(ArrayList<User> users, ActivityMainBinding binding,Recyleradap adapter){
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

    @Override
    public void addToDbUserDeviceId(Context context,String us) {
        HashMap<String,String > userIds=new HashMap<>();
        UUID uu=UUID.randomUUID();
        userIds.put("userId",us);
        CollectionReference reference=firestore.collection("Player");


        System.out.println("metoda girdi");
        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Toast.makeText(context.getApplicationContext(), " id hata", Toast.LENGTH_SHORT).show();
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

}
