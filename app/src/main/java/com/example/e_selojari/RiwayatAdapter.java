package com.example.e_selojari;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.RiwayatViewHolder> {
    public interface ItemClickListener {
        void onClick(View view, int position);
    }
    private ArrayList<Riwayat> dataList;
    Context context;
    public RiwayatAdapter(ArrayList<Riwayat> dataList, Context context)
    {
        this.context=context;
        this.dataList = dataList;
    }
    private RiwayatAdapter.ItemClickListener clickListener;
    @Override
    public RiwayatAdapter.RiwayatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_riwayat, parent, false);
        return new RiwayatAdapter.RiwayatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RiwayatAdapter.RiwayatViewHolder holder, int position){
        holder.txtNoNota.setText(String.valueOf(dataList.get(position).getNo_nota()));
        holder.txtTglJual.setText(String.valueOf(dataList.get(position).getTgl_jual()));
        holder.txtSubtot.setText(String.valueOf(dataList.get(position).getSubtot()));
        holder.txtOngkir.setText(String.valueOf(dataList.get(position).getOngkir()));
        holder.txtTotBiaya.setText(String.valueOf(dataList.get(position).getTotal()));
        holder.txtStatus.setText(String.valueOf(dataList.get(position).getStatus()));
    }
    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }
    public void setClickListener(RiwayatAdapter.ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public class RiwayatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView txtNoNota, txtTglJual, txtSubtot, txtOngkir, txtTotBiaya, txtStatus;
        public RiwayatViewHolder(View itemView) {
            super(itemView);
            txtNoNota = itemView.findViewById(R.id.txt_no_nota);
            txtTglJual = itemView.findViewById(R.id.txt_tgl_jual);
            txtSubtot = itemView.findViewById(R.id.txt_subtot);
            txtOngkir = itemView.findViewById(R.id.txt_ongkir);
            txtTotBiaya = itemView.findViewById(R.id.txt_tot_biaya);
            txtStatus = itemView.findViewById(R.id.txt_status);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if(clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }
}
