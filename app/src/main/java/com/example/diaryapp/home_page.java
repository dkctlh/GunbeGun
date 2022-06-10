package com.example.diaryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class home_page extends AppCompatActivity {
String userId;
RecyclerView recyclerView,recyclerView2;
DatabaseReference database,database2;
MyAdapter myadapter;
LockedAdapter lockedadapter;
ArrayList<ListofDiary> list;
ArrayList<ListofLockedDiary> lockedlist;
TextView tümü,kilitli,empty;
private FirebaseAuth mAuth;
int c=0,cl=0;
    AlertDialog.Builder dialogBuilder1;
    AlertDialog dialog1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        userId= getIntent().getExtras().getString("userId");
        recyclerView=findViewById(R.id.ani_list);
        recyclerView2=findViewById(R.id.locked_list);
        database= FirebaseDatabase.getInstance().getReference("diarys").child("diarys");
        database2=FirebaseDatabase.getInstance().getReference("diarys").child("locked_diarys");
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        tümü=findViewById(R.id.tümü);
        kilitli=findViewById(R.id.Kilitli);
        mAuth = FirebaseAuth.getInstance();
        list = new ArrayList<>();
        lockedlist=new ArrayList<>();
        myadapter =new MyAdapter(home_page.this,list);
        lockedadapter=new LockedAdapter(home_page.this,lockedlist);
        recyclerView2.setAdapter(lockedadapter);
        recyclerView.setAdapter(myadapter);
        empty=findViewById(R.id.empty);
        database2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ListofLockedDiary listofLockedDiary =dataSnapshot.getValue(ListofLockedDiary.class);
                    if (listofLockedDiary.getUid2().equals(userId)){
                        lockedlist.add(listofLockedDiary);
                        c++;

                    }
                    if(c == 0){

                        empty.setVisibility(View.VISIBLE);
                    }
                    lockedadapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ListofDiary listofDiary=dataSnapshot.getValue(ListofDiary.class);
                    if(listofDiary.getUid2().equals(userId)){
                        list.add(listofDiary);
                        c++;
                     }
                }
                myadapter.notifyDataSetChanged();
                if(c == 0){
                    empty.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    findViewById(R.id.logout).setOnClickListener(v -> {
        showPopUp();
    });


    }
    public void onClick(View v){
        Intent i=new Intent(this,ani_ekle.class);
        i.putExtra("userId",userId);
        startActivity(i);
    }
    public void lockedDiaryV(View v){
        recyclerView.setVisibility(View.INVISIBLE);
        recyclerView2.setVisibility(View.VISIBLE);
        tümü.setTextColor(Color.parseColor("#C5C5C7"));
        kilitli.setTextColor(Color.parseColor("#F56937"));

    }
    public void unlockedDiaryV(View v){
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView2.setVisibility(View.INVISIBLE);
        kilitli.setTextColor(Color.parseColor("#C5C5C7"));
        tümü.setTextColor(Color.parseColor("#F56937"));
    }
    private void showPopUp(){
        dialogBuilder1 =new AlertDialog.Builder(this);
        final View popUpView1=getLayoutInflater().inflate(R.layout.logout_popup,null);
        dialogBuilder1.setView(popUpView1);
        dialog1= dialogBuilder1.create();
        dialog1.show();

        popUpView1.findViewById(R.id.dismiss).setOnClickListener(v -> {
            dialog1.dismiss();
        });
        popUpView1.findViewById(R.id.exit).setOnClickListener(v -> {
            mAuth.signOut();
            Intent lp=new Intent(this,MainActivity.class);
            startActivity(lp);
            Toast.makeText(this, "Çıkış Başarılı", Toast.LENGTH_SHORT).show();
        });

    }

}