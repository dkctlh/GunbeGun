package com.example.diaryapp;

import static android.Manifest.permission.MANAGE_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.ViewSwitcher;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ani_guncelle extends AppCompatActivity {

    TextView tarih,konum;
    EditText ani,baslik,sifre;
    ImageView mod,play2;
    int vis=0;
    int pp=0;
    ImageSwitcher is2;
    Uri videoUri1,imageUri1;
    ConstraintLayout videoss,glayout,klayout;
    VideoView video2;
    String passw_,mod_,tarih_,konum_,uid_,uid2;
    private static final int PICK_IMAGES_CODE=0;
    private static final int PICK_VIDEO_CODE=1;
    private DatabaseReference mDatabase;
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    int pageHeight = 1120;
    int pagewidth = 792;

    Bitmap bmp, scaledbmp,bmp1,scaledbmp1;


    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ani_guncelle);
        tarih=findViewById(R.id.gunceltarih2);
        konum=findViewById(R.id.guncelkonum2);
        ani=findViewById(R.id.guncelani);
        baslik=findViewById(R.id.guncelbaslik);
        mod=findViewById(R.id.guncelmod2);
        is2=findViewById(R.id.is2);
        videoss=findViewById(R.id.videos2);
        video2=findViewById(R.id.videoView2);
        play2=findViewById(R.id.playpause2);
        glayout=findViewById(R.id.guncellayout);
        klayout=findViewById(R.id.killitl);
        sifre=findViewById(R.id.sifreguncel);
        Intent i=getIntent();
        String ani_=i.getStringExtra("ani");
        tarih_=i.getStringExtra("tarih");
        String baslik_ =i.getStringExtra("baslık");
        konum_=i.getStringExtra("konum");
        uid2=i.getStringExtra("uid2");
        String resim_=i.getStringExtra("resim");
        String video_=i.getStringExtra("video");
        mod_=i.getStringExtra("mod");
        uid_=i.getStringExtra("uid");
        passw_=i.getStringExtra("passw");

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.splash_note);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);
        if(passw_ != null){
            glayout.setVisibility(View.INVISIBLE);
            klayout.setVisibility(View.VISIBLE);
            mDatabase = FirebaseDatabase.getInstance().getReference("diarys").child("locked_diarys").child(uid_);
        }else {
            mDatabase = FirebaseDatabase.getInstance().getReference("diarys").child("diarys").child(uid_);
        }
        if (checkPermission()) {

        } else {
            requestPermission();
        }



        tarih.setText(tarih_);
        ani.setText(ani_);
        baslik.setText(baslik_);
        konum.setText(konum_);
        if(mod_.equals("kızgın")){

            mod.setImageResource(R.drawable.angry);
            bmp1 = BitmapFactory.decodeResource(getResources(), R.drawable.angry);

        }if(mod_.equals("üzgün")){
            mod.setImageResource(R.drawable.sad);
            bmp1 = BitmapFactory.decodeResource(getResources(), R.drawable.sad);
        }
        if(mod_.equals("mutlu")){
            mod.setImageResource(R.drawable.happy);
            bmp1 = BitmapFactory.decodeResource(getResources(), R.drawable.happy);
        }
        scaledbmp1 = Bitmap.createScaledBitmap(bmp1, 50, 50, false);
        is2.setFactory(new ViewSwitcher.ViewFactory(){
            @Override
            public View makeView(){
                ImageView imageView = new ImageView(getApplicationContext());
                return imageView;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        findViewById(R.id.guncelatach).setOnClickListener(
                v -> {
                    vis++;
                    if(vis%2!=0){
                        findViewById(R.id.guncelresim).setVisibility(View.VISIBLE);
                        findViewById(R.id.guncelresim).setOnClickListener(
                                v1 -> {
                                    pickImagesIntent();
                                }
                        );
                        findViewById(R.id.guncelvideo).setVisibility(View.VISIBLE);
                        findViewById(R.id.guncelvideo).setOnClickListener(
                                v1 -> {
                                    pickVideoIntent();
                                }
                        );
                    }else{
                        findViewById(R.id.guncelresim).setVisibility(View.INVISIBLE);
                        findViewById(R.id.guncelvideo).setVisibility(View.INVISIBLE);
                    }

                }

        );
        findViewById(R.id.görüntüle).setOnClickListener(v -> {
            if(sifre.getText().toString().equals(passw_) )
            {
                glayout.setVisibility(View.VISIBLE);
                klayout.setVisibility(View.INVISIBLE
                );
            }else if(sifre.getText().toString() != passw_){
                Toast.makeText(this, "Sifre Yanlıs", Toast.LENGTH_SHORT).show();
                sifre.setText("");
            }
        });
        findViewById(R.id.guncelle).setOnClickListener(v -> {
            if(passw_ != null){
                updateLockedDiary(baslik.getText().toString(),ani.getText().toString(),tarih_,mod_,imageUri1,videoUri1,"locked",konum_,passw_);

            }else {
               updateDiary(baslik.getText().toString(),ani.getText().toString(),tarih_,mod_,imageUri1,videoUri1,"unlocked",konum_);
            }
            Intent hp=new Intent(this,home_page.class);
            startActivity(hp);
            Toast.makeText(this, "Anı Güncellendi..", Toast.LENGTH_LONG).show();
        });
        findViewById(R.id.remove).setOnClickListener(v -> {
            showPopUp();
        });
        findViewById(R.id.pdf).setOnClickListener(v -> {
            generatePDF();
            findViewById(R.id.share).setVisibility(View.VISIBLE);
        });
        findViewById(R.id.share).setOnClickListener(v -> {

            File file = new File(Environment.getExternalStorageDirectory(), baslik.getText().toString().replace(" ","")+".pdf");


            if(!file.exists()){
                Toast.makeText(this,"Pdf bulunamadı...",Toast.LENGTH_SHORT);
            }else{

                Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);

                Intent share = new Intent();
                share.setAction(Intent.ACTION_SEND);
                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                share.setType("application/pdf");
                share.putExtra(Intent.EXTRA_STREAM,uri);
                startActivity(Intent.createChooser(share,"Anını Paylaş..."));

            }

        });
    }
    private void pickVideoIntent(){
        Intent pv2 =new Intent();
        pv2.setType("video/*");
        pv2.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(pv2,"select video"),PICK_VIDEO_CODE);
    }
    private void pickImagesIntent(){
        Intent pi2 =new Intent();
        pi2.setType("image/*");
        pi2.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(pi2,"select image"),PICK_IMAGES_CODE);
    }
    private void showPopUp(){
        dialogBuilder=new AlertDialog.Builder(this);
        final View popUpView=getLayoutInflater().inflate(R.layout.remove_popup,null);
        dialogBuilder.setView(popUpView);
        dialog=dialogBuilder.create();
        dialog.show();

        popUpView.findViewById(R.id.keep_button).setOnClickListener(v -> {
            dialog.dismiss();
        });
        popUpView.findViewById(R.id.remove_button).setOnClickListener(v -> {
            mDatabase.removeValue();
            Intent hp=new Intent(this,home_page.class);
            startActivity(hp);
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGES_CODE){
            if(resultCode == RESULT_OK){
                is2.setVisibility(View.VISIBLE);
                imageUri1=data.getData();
            Log.i("TASDASD","resc  :" + imageUri1);

                is2.setImageURI(imageUri1);
                if(videoUri1 != null){
                    video2.start();
                }
            }
        }
        if(requestCode==PICK_VIDEO_CODE){
            if(resultCode == RESULT_OK){
                videoss.setVisibility(View.VISIBLE);
                videoUri1=data.getData();

                video2.setVideoURI(videoUri1);
                video2.start();
                play2.setOnClickListener(v -> {
                    pp++;
                    if(pp%2!=0){
                        video2.pause();
                    }else
                        video2.start();
                });

            }
        }
    }
    public void updateDiary(String title, String ani, String tarih, String mod, Uri resim, Uri video, String kilit, String konum) {
        if(resim ==null && video == null){

            diary diary = new diary(title,ani, tarih,mod,null,null,kilit,konum,uid2,null,uid_);
            mDatabase.setValue(diary);
        }else if(resim == null && video!=null){
            diary diary = new diary(title,ani, tarih,mod,null,video.toString(),kilit,konum,uid2,null,uid_);
            mDatabase.setValue(diary);
        }else if(resim !=null && video == null){
            diary diary = new diary(title,ani, tarih,mod,resim.toString(),null,kilit,konum,uid2,null,uid_);
            mDatabase.setValue(diary);
        }else if(resim !=null && video != null){
            diary diary = new diary(title,ani, tarih,mod,resim.toString(),video.toString(),kilit,konum,uid2,null,uid_);
            mDatabase.setValue(diary);
        }
    }
    public void updateLockedDiary(String title, String ani, String tarih, String mod, Uri resim, Uri video, String kilit, String konum, String kilitlock) {
        if (resim == null && video == null) {

            diary diary = new diary(title, ani, tarih, mod, null, null, kilit, konum, uid2, kilitlock, uid_);
            mDatabase.setValue(diary);
        } else if (resim == null && video != null) {
            diary diary = new diary(title, ani, tarih, mod, null, video.toString(), kilit, konum, uid2, kilitlock, uid_);
            mDatabase.setValue(diary);
        } else if (resim != null && video == null) {
            diary diary = new diary(title, ani, tarih, mod, resim.toString(), null, kilit, konum, uid2, kilitlock, uid_);
            mDatabase.setValue(diary);
        } else if (resim != null && video != null) {
            diary diary = new diary(title, ani, tarih, mod, resim.toString(), video.toString(), kilit, konum, uid2, kilitlock, uid_);
            mDatabase.setValue(diary);
        }

    }
    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int permission3 = ContextCompat.checkSelfPermission(getApplicationContext(), MANAGE_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED && permission3 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE,MANAGE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denined.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
    private void generatePDF() {

        PdfDocument pdfDocument = new PdfDocument();


        Paint paint = new Paint();
        Paint title = new Paint();
        Paint ani_=new Paint();
        Paint baslik_=new Paint();


        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();


        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);


        Canvas canvas = myPage.getCanvas();

        canvas.drawBitmap(scaledbmp, 56, 40, paint);
        canvas.drawBitmap(scaledbmp1, 540, 275, paint);
     //   canvas.drawBitmap(scaledbmp2,600,50,paint);
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));

        title.setTextSize(15);


        title.setColor(ContextCompat.getColor(this, R.color.orange));


        canvas.drawText("Anılarınızı saklayın.", 209, 150, title);
        canvas.drawText("GünbeGün", 209, 100, title);

        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(this, R.color.orange));
        title.setTextSize(15);

        baslik_.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        baslik_.setTextAlign(Paint.Align.LEFT);
        baslik_.setColor(ContextCompat.getColor(this,R.color.black));
        canvas.drawText(baslik.getText().toString(), 396, 375, baslik_);

        ani_.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        ani_.setTextAlign(Paint.Align.LEFT);
        ani_.setColor(ContextCompat.getColor(this,R.color.black));
        canvas.drawText(ani.getText().toString(), 40, 425, ani_);
        canvas.drawText("Konum :  "+ konum_,540,250,baslik_);
        canvas.drawText("Tarih :   "+ tarih_,540,225,baslik_);

        pdfDocument.finishPage(myPage);


        File file = new File(Environment.getExternalStorageDirectory(), baslik.getText().toString().replace(" ","")+".pdf");

        try {

            pdfDocument.writeTo(new FileOutputStream(file));


            Toast.makeText(ani_guncelle.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("TAGASDASD","error:  "+ e.toString()  );
            e.printStackTrace();
        }

        pdfDocument.close();
    }
}