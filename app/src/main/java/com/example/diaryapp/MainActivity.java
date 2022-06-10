package com.example.diaryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    EditText email,pass;
    private FirebaseAuth mAuth;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        email
                = (EditText)findViewById(R.id.adsoyad);
        pass
                = (EditText)findViewById(R.id.sifre2);

    }

    @Override
    protected void onStart() {
        super.onStart();
        email.getText().clear();
        pass.getText().clear();
    }

    public void onSignUp(View v){
        Intent i=new Intent(this,kayit_ol.class);
        startActivity(i);
    }
    public void logIn(View v){
        mAuth.signInWithEmailAndPassword(email.getText().toString(),pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Giriş Başarılı", Toast.LENGTH_SHORT).show();
                    userId=mAuth.getUid();

                    Intent i=new Intent (MainActivity.this,home_page.class);
                    i.putExtra("userId",userId);
                    startActivity(i);
                }else {
                    Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




}

