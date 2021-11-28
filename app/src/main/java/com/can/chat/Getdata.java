package com.can.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.can.chat.Adapter.Recyleradap;
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

import java.util.ArrayList;
import java.util.Map;

public class Getdata
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
    public static   void todb(Map<String,Object> messages){
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

}
