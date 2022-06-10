package com.example.diaryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.ViewSwitcher;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class ani_ekle extends AppCompatActivity implements LocationListener {
    TextView loc,tarih;
    ConstraintLayout videop,aniekle,sifregir;
    ImageView happy,angry,sad,play,save;
    ImageSwitcher is;
    FloatingActionButton atach,image,film;
    LocationManager locationManager;
    String mod;
    VideoView video;
    Uri videoUri,imageUri;

    EditText ani,ozelsifre,title;
    Button kaydet;


    String lock="unlocked";
    String uid2;

    private static final int PICK_IMAGES_CODE=0;
    private static final int PICK_VIDEO_CODE=1;
    int vis=0;
    int pp=0;
    String userId;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ani_ekle);
        ani=findViewById(R.id.ani_ekle1);
        is=findViewById(R.id.imageS);
        loc=findViewById(R.id.location);
        tarih=findViewById(R.id.tarih2);
        video=findViewById(R.id.videoView);
        play=findViewById(R.id.playpause);
        videop=findViewById(R.id.videos);
        userId= UUID.randomUUID().toString();
        mAuth=FirebaseAuth.getInstance();
        aniekle=findViewById(R.id.ani_ekle_layout);
        sifregir=findViewById(R.id.özel_sifre);
        ozelsifre=findViewById(R.id.sifreguncel);
        kaydet=findViewById(R.id.ozelsifrekaydet);
        mDatabase = FirebaseDatabase.getInstance().getReference("diarys");
        Log.i("uid","anı ekle  " + userId);
        uid2=getIntent().getExtras().getString("userId");
        title=findViewById(R.id.title_diary);

        if (ContextCompat.checkSelfPermission(ani_ekle.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ani_ekle.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }
        getLocation();
        String currentDate = new SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault()).format(new Date());
        tarih.setText(currentDate);

        is.setFactory(new ViewSwitcher.ViewFactory(){
                @Override
            public View makeView(){
                    ImageView imageView = new ImageView(getApplicationContext());
                    return imageView;
                }
        });
    }



    @Override
    protected void onStart() {
        findViewById(R.id.lock).setOnClickListener(v -> {
            if(lock.equals("locked")){
                findViewById(R.id.lock).setBackgroundResource(R.drawable.unlock);

                lock="unlock";

            }else {
                findViewById(R.id.lock).setBackgroundResource(R.drawable.padlock);
                lock="locked";
                Log.i("lock", "onStart: "+lock);
            }
        });
        findViewById(R.id.happy).setOnClickListener(
                v -> {
                    findViewById(R.id.happy).getLayoutParams().height=96;
                    findViewById(R.id.happy).getLayoutParams().width=96;
                    findViewById(R.id.sad).getLayoutParams().height=64;
                    findViewById(R.id.sad).getLayoutParams().width=64;
                    findViewById(R.id.angry).getLayoutParams().height=64;
                    findViewById(R.id.angry).getLayoutParams().width=64;
                    findViewById(R.id.angry).requestLayout();
                    findViewById(R.id.sad).requestLayout();
                    findViewById(R.id.happy).requestLayout();
                    mod="mutlu";

                }
        );
        findViewById(R.id.sad).setOnClickListener(
                v -> {
                    findViewById(R.id.happy).getLayoutParams().height=64;
                    findViewById(R.id.happy).getLayoutParams().width=64;
                    findViewById(R.id.sad).getLayoutParams().height=96;
                    findViewById(R.id.sad).getLayoutParams().width=96;
                    findViewById(R.id.angry).getLayoutParams().height=64;
                    findViewById(R.id.angry).getLayoutParams().width=64;
                    findViewById(R.id.angry).requestLayout();
                    findViewById(R.id.sad).requestLayout();
                    findViewById(R.id.happy).requestLayout();

                    mod="üzgün";

                }
        );
        findViewById(R.id.angry).setOnClickListener(
                v -> {
                    findViewById(R.id.happy).getLayoutParams().height=64;
                    findViewById(R.id.happy).getLayoutParams().width=64;
                    findViewById(R.id.sad).getLayoutParams().height=64;
                    findViewById(R.id.sad).getLayoutParams().width=64;
                    findViewById(R.id.angry).getLayoutParams().height=96;
                    findViewById(R.id.angry).getLayoutParams().width=96;
                    findViewById(R.id.angry).requestLayout();
                    findViewById(R.id.sad).requestLayout();
                    findViewById(R.id.happy).requestLayout();

                    mod="kızgın";

                }
        );
        findViewById(R.id.atach).setOnClickListener(
                v -> {
                    vis++;
                    if(vis%2!=0){
                        findViewById(R.id.image).setVisibility(View.VISIBLE);
                        findViewById(R.id.image).setOnClickListener(
                                v1 -> {
                                        pickImagesIntent();
                                }
                        );
                        findViewById(R.id.film).setVisibility(View.VISIBLE);
                        findViewById(R.id.film).setOnClickListener(
                                v1 -> {
                                    pickVideoIntent();
                                }
                        );
                    }else{
                        findViewById(R.id.image).setVisibility(View.INVISIBLE);
                        findViewById(R.id.film).setVisibility(View.INVISIBLE);
                    }

                }
        );
        findViewById(R.id.save).setOnClickListener(v -> {
            if(title.getText().length() <3){
                Toast.makeText(this, "Başlık en az 4 karakter olabilir.", Toast.LENGTH_SHORT).show();
            }else{
            if(lock.equals("locked")){
                aniekle.setVisibility(View.INVISIBLE);
                sifregir.setVisibility(View.VISIBLE);

            }
              else{
                    writeNewDiary(title.getText().toString(),ani.getText().toString(),tarih.getText().toString(),mod,imageUri,videoUri,lock,loc.getText().toString());
                    Toast.makeText(this, "Anı Eklendi", Toast.LENGTH_LONG).show();
                    Intent hs=new Intent(this,home_page.class);
                    startActivity(hs);
                }

            }
           // Log.i("Diary", "Diary: "+ userId+" "+ani.getText().toString()+ ""+ tarih.getText().toString() + " " +mod+""+imageUri +" "+videoUri+" " +lock +" "+loc.getText().toString());

        });
        kaydet.setOnClickListener( v -> {
            if(ozelsifre.getText().toString().length() < 3){
                Toast.makeText(this, "Özel Şifre minimum 4 karakterli olmalıdır.", Toast.LENGTH_SHORT).show();
            }
            else {
                writeNewLockedDiary(title.getText().toString(),ani.getText().toString(), tarih.getText().toString(), mod, imageUri, videoUri, lock, loc.getText().toString(), ozelsifre.getText().toString());
                Toast.makeText(this, "Anı Eklendi", Toast.LENGTH_LONG).show();
                Intent hs=new Intent(this,home_page.class);
                startActivity(hs);
            }

        });
        super.onStart();
    }

    @SuppressLint("MissingPermission")
        private void getLocation() {

            try {
                locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,ani_ekle.this);

            }catch (Exception e){
                e.printStackTrace();
            }

        }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        Toast.makeText(this, ""+location.getLatitude()+","+location.getLongitude(), Toast.LENGTH_SHORT).show();
        try {
            Geocoder geocoder = new Geocoder(ani_ekle.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String address = addresses.get(0).getAddressLine(0);

            loc.setText(address);
            loc.setVisibility(View.VISIBLE);
            tarih.setVisibility(View.VISIBLE);
            locationManager.removeUpdates(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
    private void pickVideoIntent(){
        Intent pv=new Intent();
        pv.setType("video/*");
        pv.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(pv,"select video"),PICK_VIDEO_CODE);
    }
    private void pickImagesIntent(){
        Intent pi=new Intent();
        pi.setType("image/*");
        pi.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(pi,"select image"),PICK_IMAGES_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGES_CODE){
            if(resultCode == Activity.RESULT_OK){
                is.setVisibility(View.VISIBLE);
                imageUri=data.getData();

                is.setImageURI(imageUri);
                if(videoUri != null){
                    video.start();
                }
            }
        }
        if(requestCode==PICK_VIDEO_CODE){
            if(resultCode == Activity.RESULT_OK){
                videop.setVisibility(View.VISIBLE);
                videoUri=data.getData();
                video.setVideoURI(videoUri);
                video.start();
                play.setOnClickListener(v -> {
                    pp++;
                    if(pp%2!=0){
                        video.pause();
                    }else
                    video.start();
                });

            }
        }
    }

    public void writeNewDiary(String title,String ani, String tarih,String mod,Uri resim,Uri video,String kilit,String konum) {
        if(resim ==null && video == null){

            diary diary = new diary(title,ani, tarih,mod,null,null,kilit,konum,uid2,null,userId);
            mDatabase.child("diarys").child(userId).setValue(diary);
        }else if(resim == null && video!=null){
            diary diary = new diary(title,ani, tarih,mod,null,video.toString(),kilit,konum,uid2,null,userId);
            mDatabase.child("diarys").child(userId).setValue(diary);
        }else if(resim !=null && video == null){
            diary diary = new diary(title,ani, tarih,mod,resim.toString(),null,kilit,konum,uid2,null,userId);
            mDatabase.child("diarys").child(userId).setValue(diary);
        }else if(resim !=null && video != null){
            diary diary = new diary(title,ani, tarih,mod,resim.toString(),video.toString(),kilit,konum,uid2,null,userId);
            mDatabase.child("diarys").child(userId).setValue(diary);
        }
    }
    public void writeNewLockedDiary(String title,String ani, String tarih,String mod,Uri resim,Uri video,String kilit,String konum,String kilitlock) {
        if(resim ==null && video == null){

            diary diary = new diary(title,ani, tarih,mod,null,null,kilit,konum,uid2,kilitlock,userId);
            mDatabase.child("locked_diarys").child(userId).setValue(diary);
        }else if(resim == null && video!=null){
            diary diary = new diary(title,ani, tarih,mod,null,video.toString(),kilit,konum,uid2,kilitlock,userId);
            mDatabase.child("locked_diarys").child(userId).setValue(diary);
        }else if(resim !=null && video == null){
            diary diary = new diary(title,ani, tarih,mod,resim.toString(),null,kilit,konum,uid2,kilitlock,userId);
            mDatabase.child("locked_diarys").child(userId).setValue(diary);
        }else if(resim !=null && video != null){
            diary diary = new diary(title,ani, tarih,mod,resim.toString(),video.toString(),kilit,konum,uid2,kilitlock,userId);
            mDatabase.child("locked_diarys").child(userId).setValue(diary);
        }



    }
}