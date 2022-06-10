package com.example.diaryapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LockedAdapter extends RecyclerView.Adapter<LockedAdapter.MyViewHolder1> {
    Context context;
    ArrayList<ListofLockedDiary> list;

    public LockedAdapter(Context context, ArrayList<ListofLockedDiary> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v1= LayoutInflater.from(context).inflate(R.layout.locked_ani_item_karti,parent,false);
        MyViewHolder1 holder1=new MyViewHolder1(v1);
        return holder1;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder1 holder, int position) {
        ListofLockedDiary selectedLockedDiary = list.get(position);
        holder.tarih.setText(selectedLockedDiary.getTarih());
        holder.title.setText(selectedLockedDiary.getBaslık());
        String mod=selectedLockedDiary.getMod();
        String f=selectedLockedDiary.getResim();
        String v=selectedLockedDiary.getVideo();
        if(f==null && v==null){
            holder.fv.setVisibility(View.INVISIBLE);
        }if(f != null && v != null){
            holder.fv.setText("Fotoğraflı ve Videolu");
        }if(f == null && v != null){
            holder.fv.setText("Videolu");
        }if(f != null && v == null){
            holder.fv.setText("Fotoğraflı");
        }
        holder.mood.setText(selectedLockedDiary.getMod().substring(0,1).toUpperCase()+selectedLockedDiary.getMod().substring(1));
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

    public class MyViewHolder1 extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tarih,title,mood,fv;
        ImageView emoji;

        public MyViewHolder1(@NonNull View itemView) {
            super(itemView);
            tarih=itemView.findViewById(R.id.tarih232);
            title=itemView.findViewById(R.id.title32);
            mood=itemView.findViewById(R.id.mood22);
            fv=itemView.findViewById(R.id.mood23);
            emoji=itemView.findViewById(R.id.emoji232);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int pos=getAdapterPosition();
            ListofLockedDiary selectedLockedDiary= list.get(pos);
            Intent i=new Intent(v.getContext(),ani_guncelle.class);
            i.putExtra("ani",selectedLockedDiary.ani);
            i.putExtra("tarih",selectedLockedDiary.tarih);
            i.putExtra("baslık",selectedLockedDiary.baslık);
            i.putExtra("konum",selectedLockedDiary.konum);
            i.putExtra("resim",selectedLockedDiary.resim);
            i.putExtra("mod",selectedLockedDiary.mod);
            i.putExtra("video",selectedLockedDiary.video);
            i.putExtra("uid",selectedLockedDiary.uid);
            i.putExtra("uid2",selectedLockedDiary.uid2);
            i.putExtra("passw",selectedLockedDiary.kilitlock);
            v.getContext().startActivity(i);

        }
    }
}
