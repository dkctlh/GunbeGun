package com.example.diaryapp;

import android.net.Uri;

import androidx.annotation.Nullable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.File;

public class diary {
    public String baslık;
    public String ani;
    public String tarih;
    public String mod;
    public String  resim;
    public String  video;
    public String kilit;
    public String konum;
    public String uid2;
    public String kilitlock;
    public String uid;


    public diary(String baslık, String ani, String tarih, String mod, String resim, String video, String kilit, String konum, String uid2, String kilitlock,String uid) {
        this.baslık = baslık;
        this.ani = ani;
        this.tarih = tarih;
        this.mod = mod;
        this.resim = resim;
        this.video = video;
        this.kilit = kilit;
        this.konum = konum;
        this.uid2=uid2;
        this.kilitlock=kilitlock;
        this.uid=uid;
    }
}


