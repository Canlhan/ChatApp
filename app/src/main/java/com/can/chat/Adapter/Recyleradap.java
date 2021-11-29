package com.can.chat.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.can.chat.Getdata;
import com.can.chat.MainActivity;
import com.can.chat.databinding.RecylerRowBinding;
import com.can.chat.model.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.GenericArrayType;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Map;

public class Recyleradap  extends RecyclerView.Adapter<Recyleradap.Recyholder>
{
    private ArrayList<User> users;
    private FirebaseFirestore firestore;


    public Recyleradap(ArrayList<User> users) {
        this.users =users;
        firestore=FirebaseFirestore.getInstance();


    }

    @NonNull
    @Override
    public Recyholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecylerRowBinding binding=RecylerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new Recyholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Recyholder holder, int position) {

       holder.binding.textmessage.setText(users.get(position).message);
       holder.binding.textuser.setText(users.get(position).name);






    }

    @Override
    public int getItemCount() {

        return users.size();
    }

    public class Recyholder extends RecyclerView.ViewHolder{
        private RecylerRowBinding binding;
        public Recyholder(RecylerRowBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }


}
