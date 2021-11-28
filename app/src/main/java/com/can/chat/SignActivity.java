package com.can.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.can.chat.databinding.ActivitySignBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignActivity extends AppCompatActivity {
    private ActivitySignBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        user=auth.getCurrentUser();

        if(user!=null){
            Intent intent=new Intent(SignActivity.this,MainActivity.class);
            finish();
            startActivity(intent);
        }

    }

    public void signIn(View view){
        System.out.println("giriş");
        String pass=binding.editTextTextPassword.getText().toString();
        String name=binding.edittextname.getText().toString();
        auth.signInWithEmailAndPassword(name,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent intent=new Intent(SignActivity.this,MainActivity.class);
                finish();
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignActivity.this,e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });



    }
}