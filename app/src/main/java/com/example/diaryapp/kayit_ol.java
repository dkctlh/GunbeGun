package com.example.diaryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.UUID;

public class kayit_ol extends AppCompatActivity {
    EditText name,email,pass,phonen;
    String userId;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol);
        name
                = (EditText)findViewById(R.id.eposta2);
        email
                = (EditText)findViewById(R.id.adsoyad);
        pass
                = (EditText)findViewById(R.id.sifre2);
        phonen
                = (EditText)findViewById(R.id.telno);
        mAuth=FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        userId= UUID.randomUUID().toString();
    }
    public void onSignIn(View v){
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
    }
    public void signUp(View v){
        if(name.getText().toString().isEmpty() || name.getText().toString().length()<5)
        {
            Toast.makeText(this, "Lütfen geçerli bir isim giriniz.", Toast.LENGTH_SHORT).show();
        }else{
        if(email.getText().toString().isEmpty() || email.getText().toString().length()<9)
        {
            Toast.makeText(this, "Lütfen geçerli bir mail adresi giriniz.", Toast.LENGTH_SHORT).show();
        } else{
        if(phonen.getText().toString().isEmpty() || phonen.getText().toString().length()!=10)
        {
            Toast.makeText(this, "Lütfen telefon numaranızı başına 0 koymadan giriniz.", Toast.LENGTH_SHORT).show();
        }else{
        if(pass.getText().toString().isEmpty() || pass.getText().toString().length()<2)
        {
            Toast.makeText(this, "Şifreniz en az 7 karakter olmalıdır.", Toast.LENGTH_SHORT).show();
        }}}}
        mAuth.createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString()).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(kayit_ol.this, "Kayıt olma başarılı", Toast.LENGTH_SHORT).show();
                           writeNewUser(userId,name,email,phonen,pass);
                            Log.i("err","Hata ");
                            Intent i = new Intent(kayit_ol.this, MainActivity.class);
                            startActivity(i);
                        }else{
                            Toast.makeText(kayit_ol.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        }
    public void writeNewUser(String userId, EditText name, EditText email,EditText phonen,EditText pass) {
        User user = new User(name.getText().toString(), email.getText().toString(),
                phonen.getText().toString(),pass.getText().toString());
        Log.i("err","Hata ");

        mDatabase.child("users").child(userId).setValue(user);
    }

}