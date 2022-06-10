package com.example.diaryapp;

import android.content.Context;
import android.content.Intent;
import android.icu.text.Transliterator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    ArrayList<ListofDiary> list;



    public MyAdapter(Context context, ArrayList<ListofDiary> list) {

        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.ani_item_karti,parent,false);


        MyViewHolder holder=new MyViewHolder(v);
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ListofDiary selectedDiary= list.get(position);

        holder.tarih.setText(selectedDiary.getTarih());
        holder.ani.setText(selectedDiary.getAni());
        holder.title.setText(selectedDiary.getBaslık());
        String mod=selectedDiary.getMod();
        String f=selectedDiary.getResim();
        String v=selectedDiary.getVideo();
        if(f==null && v==null){
            holder.fv.setVisibility(View.INVISIBLE);
        }if(f != null && v != null){
            holder.fv.setText("Fotoğraflı ve Videolu");
        }if(f == null && v != null){
            holder.fv.setText("Videolu");
        }if(f != null && v == null){
            holder.fv.setText("Fotoğraflı");
        }
        holder.mood.setText(selectedDiary.getMod().substring(0,1).toUpperCase()+selectedDiary.getMod().substring(1));
        if(mod.equals("kızgın")){
            holder.emoji.setImageResource(R.drawable.angry);
        }if(mod.equals("üzgün")){
            holder.emoji.setImageResource(R.drawable.sad);
        }
        if(mod.equals("mutlu")){
            holder.emoji.setImageResource(R.drawable.happy);
        }



    }

    @Override
    public int getItemCount() {

        return list.size();
    }


    public  class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tarih,ani,title,mood,fv;
        ImageView emoji;




        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tarih=itemView.findViewById(R.id.tarih23);
            ani=itemView.findViewById(R.id.ani_metin23);
            emoji=itemView.findViewById(R.id.emoji23);
            title=itemView.findViewById(R.id.title32);
            mood=itemView.findViewById(R.id.mood);
            fv=itemView.findViewById(R.id.mood22);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            int pos=getAdapterPosition();
            ListofDiary selectedDiary= list.get(pos);
            Intent i=new Intent(v.getContext(),ani_guncelle.class);
            i.putExtra("ani",selectedDiary.ani);
            i.putExtra("tarih",selectedDiary.tarih);
            i.putExtra("baslık",selectedDiary.baslık);
            i.putExtra("konum",selectedDiary.konum);
            i.putExtra("resim",selectedDiary.resim);
            i.putExtra("mod",selectedDiary.mod);
            i.putExtra("video",selectedDiary.video);
            i.putExtra("uid",selectedDiary.uid);
            i.putExtra("uid2",selectedDiary.uid2);
            v.getContext().startActivity(i);

        }
    }
}
