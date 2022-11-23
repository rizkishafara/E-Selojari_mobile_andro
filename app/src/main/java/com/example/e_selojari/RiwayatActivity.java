package com.example.e_selojari;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.e_selojari.util.ServerAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.example.e_selojari.LoginActivity.TAG_USERNAME;

public class RiwayatActivity extends AppCompatActivity{
    String username, musername;
    TextView user;
    ArrayList<Riwayat> mItems;
    private RiwayatAdapter adapter;
    RecyclerView mRecyclerview;
    //RecyclerView.Adapter mAdapter;
    TextView riwayat;

    private ArrayList<Riwayat> riwayatArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);

        mItems = new ArrayList<>();
        adapter= new RiwayatAdapter(mItems,this);
        username = getIntent().getStringExtra(TAG_USERNAME);
        musername = String.valueOf(username);


        mRecyclerview = (RecyclerView) findViewById(R.id.list_riwayat);
        riwayat=(TextView) findViewById(R.id.txt_no_nota);
        // Membaca Semua Barang
        tampilRiwayat();
        adapter= new RiwayatAdapter(mItems,this);
        //GRID 2 kolom
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        mRecyclerview.setLayoutManager(layoutManager);
        mRecyclerview.setAdapter(adapter);
        //mAdapter.setClickListener(this);
        //adapter.setClickListener(this);

    }

    private void tampilRiwayat() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerAPI.URL_RIWAYAT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("volley", "response : " + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("result");
                    for (int i = 0; i < array.length(); i++) {
                            JSONObject data = array.getJSONObject(i);
                            Riwayat md = new Riwayat();

                            md.setNo_nota(data.getString("no_nota"));
                            md.setTgl_jual(data.getString("tgl_jual"));
                            md.setSubtot(data.getString("subtot"));
                            md.setOngkir(data.getString("ongkir"));
                            md.setTotal(data.getString("total_biaya"));
                            md.setStatus(data.getString("status"));
                            mItems.add(md);

                    }
                    //mAdapter.notifyDataSetChanged();
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    Log.d("volley", "error : " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("username", username);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    /*@Override
    public void onClick(View view, int position) {

    }*/
}