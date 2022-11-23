package com.example.e_selojari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.URL;

public class DetailActivity extends AppCompatActivity {
    ImageView gambar;
    TextView nama, harga;

    String mnama, mharga, mgambar;
    int img,url;
    int position=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //get views
        gambar = (ImageView) findViewById(R.id.IvDetailGambar);
        nama = (TextView)findViewById(R.id.TxtNama);
        harga = (TextView)findViewById(R.id.TxtHarga);


        // ambil nilai yang di kirim pada saat di klik
        mnama = getIntent().getStringExtra("nama");
        mharga = getIntent().getStringExtra("harga");
        mgambar = getIntent().getStringExtra("images");
        //img = Integer.parseInt(mgambar);


        // tampilkan data pada DetailActivity
        Glide.with(this).load(mgambar).into(gambar);
        //gambar.setImageResource(img);
        nama.setText(mnama);
        harga.setText(mharga);
    }
}