package com.example.e_selojari;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.e_selojari.util.ServerAPI;

import static com.example.e_selojari.LoginActivity.TAG_USERNAME;

public class MainActivity extends AppCompatActivity implements ProdukAdapter.ItemClickListener{
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    RecyclerView mRecyclerview;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mManager;

    ArrayList<Produk> mItems;
    Button btnInsert, btnDelete;
    TextView produk;
    ProgressDialog pd;
    Toast toast;
    String username;
    SharedPreferences sharedpreferences;
    private ProdukAdapter adapter;
    private ArrayList<Produk> produkArrayList;
    private ArrayList<String> listGambar;
    ViewFlipper v_flipper;
    public double tot=0;
    String dataProduk[] = null;
    String dS[] = null;
    int images[] = {R.drawable.keripik,R.drawable.keripik2,R.drawable.keripik3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        v_flipper = findViewById(R.id.v_flipper);
        for (int i =0; i<images.length; i++){
            fliverImages(images[i]);
        }
        for (int image: images)
            fliverImages(image);
        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        username = getIntent().getStringExtra(TAG_USERNAME);
        toast = Toast.makeText(getApplicationContext(), null, Toast.LENGTH_SHORT);
        mRecyclerview = (RecyclerView) findViewById(R.id.recycler_view);
        pd = new ProgressDialog(MainActivity.this);
        mItems = new ArrayList<>();
        produk=(TextView) findViewById(R.id.txt_nama_produk);
        // Membaca Semua Barang
        loadJson();
        //mManager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false);
        //mRecyclerview.setLayoutManager(mManager);
        mAdapter = new ProdukAdapter(mItems,this);
        adapter= new ProdukAdapter(mItems,this);
        //mRecyclerview.setAdapter(mAdapter);
        //GRID 2 kolom
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        //STAGGER 4 KOLOM
        // StaggeredGridLayoutManager llm=new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerview.setLayoutManager(layoutManager);
        mRecyclerview.setAdapter(adapter);
        //mAdapter.setClickListener(this);
        adapter.setClickListener(this);



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent = new Intent();
        String data;
        String phone="0895360622007";
        switch (id) {
            case R.id.action_riwayat:
                Intent riwayat = new Intent(MainActivity.this,RiwayatActivity.class);
                //String musername = String.valueOf(username);
                riwayat.putExtra(TAG_USERNAME,username);

                startActivity(riwayat);
                Toast.makeText(this,"Riwayat Belanja " + username,Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_update:
                //memanggil username ke updateactivity
                Intent i= new Intent(MainActivity.this, UpdateActivity.class);
                i.putExtra("username", username);
                startActivity(i);
                finish();
                return true;
            case R.id.action_call:

                String number = ("tel:" + phone);
                Intent mIntent = new Intent(Intent.ACTION_CALL);
                mIntent.setData(Uri.parse(number));
                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE);

                    // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                } else {
                    //You already have permission
                    try {
                        startActivity(mIntent);
                    } catch(SecurityException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            case R.id.action_sms:
                Uri uri = Uri.parse("smsto:0895360622007");
                Intent sintent = new Intent(Intent.ACTION_SENDTO, uri);
                sintent.putExtra("sms_body", "Saya Mau pesan");
                startActivity(sintent);
                return true;
            case R.id.action_logout:
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(LoginActivity.session_status, false);
                editor.putString(TAG_USERNAME, null);
                editor.commit();
                intent.setClass(MainActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void postTotals()
    {
        TextView txtTot=(TextView) findViewById(R.id.totalPrice);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        txtTot.setText("Rp. "+decimalFormat.format(tot));
    }
    public void onClick(View view, int position) {
        //final Produk mhs = produkArrayList.get(position);
        final Produk mhs = mItems.get(position);
        switch (view.getId()) {
            case R.id.txt_nama_produk:
                Intent i= new Intent(getApplicationContext(), DetailActivity.class);
                i.putExtra("nama",mhs.getNama());
                i.putExtra("harga",mhs.getHarga());
                i.putExtra("images",mhs.getImg());

                //kirimkan ke detail.java
                startActivity(i);

                Toast.makeText(this,"okkkkkk" + mhs.getNama(),Toast.LENGTH_SHORT).show();
                return;
            case R.id.img_card:
                tot=tot+Double.parseDouble(mhs.getHarga());
                Toast.makeText(this,"Pesanan....." + mhs.getNama() ,Toast.LENGTH_SHORT).show();
                mhs.setJumlah(mhs.getJumlah() + 1);
                postTotals();
                return;
            default:
                Toast.makeText(this,"lainnya..... -> " + mhs.getNama()+ " Rp. "+tot ,Toast.LENGTH_SHORT).show();
                break;
        }
    }private void loadJson() {
        pd.setMessage("Mengambil Data");
        pd.setCancelable(false);
        pd.show();
        JsonArrayRequest reqData = new JsonArrayRequest(Request.Method.POST, ServerAPI.URL_DATA, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pd.cancel();
                Log.d("volley", "response : " + response.toString());
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject data = response.getJSONObject(i);
                        Produk md = new Produk();

                        md.setKode(data.getString("kd_brg"));
                        md.setNama(data.getString("nm_brg"));
                        md.setHarga(data.getString("harga"));
                        md.setImg(data.getString("gambar"));
                        mItems.add(md);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //mAdapter.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        Log.d("volley", "error : " + error.getMessage());
                    }
                });

        com.example.e_selojari.util.AppController.getInstance().addToRequestQueue(reqData);
    }
    public void ambildata(){
        listGambar= new ArrayList<String>();
        listGambar.add("angrek");
        listGambar.add("angrek");
        listGambar.add("angrek");
        listGambar.add("angrek");
        listGambar.add("angrek");
    }
    public void checkout(View view) {
        if (tot > 0) {
            String[] kode = new String[mItems.size()];
            int[] qty = new int[mItems.size()];
            Intent checkout = new Intent(this, OngkirActivity.class);
            for (int i = 0; i < mItems.size(); i++) {
                Produk md = mItems.get(i);
                kode[i] = md.getKode();
                qty[i] = md.getJumlah();
            }
            checkout.putExtra("total", tot);
            checkout.putExtra("kode", kode);
            checkout.putExtra("qty", qty);
            checkout.putExtra(TAG_USERNAME, username);
            startActivity(checkout);
        } else {
            Toast.makeText(this,"Belanja dulu...",Toast.LENGTH_SHORT).show();
        }
    }
    public void fliverImages(int images){
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(images);
        v_flipper.addView(imageView);
        v_flipper.setFlipInterval(4000);
        v_flipper.setAutoStart(true);
        v_flipper.setInAnimation(this,android.R.anim.slide_in_left);
        v_flipper.setOutAnimation(this,android.R.anim.slide_out_right);
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Apakah kamu ingin keluar?");
        builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed toexit from application
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                int pid = android.os.Process.myPid();
                android.os.Process.killProcess(pid);
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}

