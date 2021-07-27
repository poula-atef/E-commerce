package com.example.e_commerce.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.e_commerce.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class ItemQrCodeActivity extends AppCompatActivity {

    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_qr_code);
        img = findViewById(R.id.qr);
        String txt = getIntent().getStringExtra("itemName");
        MultiFormatWriter mfw = new MultiFormatWriter();
        try{
            BitMatrix bm = mfw.encode(txt, BarcodeFormat.QR_CODE,500,500);
            BarcodeEncoder be = new BarcodeEncoder();
            Bitmap bitmap = be.createBitmap(bm);
            img.setImageBitmap(bitmap);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSlideRight(this);
    }
}