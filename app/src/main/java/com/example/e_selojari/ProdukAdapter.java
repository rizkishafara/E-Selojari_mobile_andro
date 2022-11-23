package com.example.e_selojari;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ProdukAdapter extends RecyclerView.Adapter<ProdukAdapter.ProdukViewHolder> {
    public interface ItemClickListener {
        void onClick(View view, int position);
    }
    private ArrayList<Produk> dataList;
    private String KEY_TOT="TOT";
    public double tot=0;
    Context context;
    //int gambar[] = {R.drawable.anggrek, R.drawable.anggrek};
    public ProdukAdapter(ArrayList<Produk> dataList, Context context)
    {
        this.context=context;
        this.dataList = dataList;
    }
    private ItemClickListener clickListener;

    @Override
    public ProdukViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_view, parent, false);
        return new ProdukViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProdukViewHolder holder, int position){
        holder.txtNama.setText(dataList.get(position).getNama());
        holder.txtKode.setText(dataList.get(position).getKode());
        holder.txtHarga.setText(dataList.get(position).getHarga());
        //holder.icon.setImageResource(gambar[position]);
        //holder.icon.setImageResource(Integer.parseInt(dataList.get(position).getImg()));



        //penggunaan library glide
        Glide.with(context) //konteks bisa didapat dari activity yang sedang berjalan
                .load(dataList.get(position).getImg()) // mengambil data dengan cara "list.get(position)" mendapatkan isi berupa objek Menu. kemudian "Menu.geturlGambar"
                .thumbnail(0.5f) // resize gambar menjadi setengahnya
                .into(holder.icon); // mengisikan ke imageView*/
    }
    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }
    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public class ProdukViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView txtNama, txtKode, txtHarga;
        private ImageView icon;
        public ProdukViewHolder(View itemView) {
            super(itemView);
            txtNama = itemView.findViewById(R.id.txt_nama_produk);
            txtKode = itemView.findViewById(R.id.txt_kode_produk);
            txtHarga = itemView.findViewById(R.id.txt_harga_produk);
            icon= itemView.findViewById(R.id.img_card);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
            txtNama.setOnClickListener(this);
            icon.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if(clickListener != null) clickListener.onClick(view,
                    getAdapterPosition());
        }
    }
    public double getTot()
    {
        return tot;
    }
    public void setTot(double tot)
    {
        this.tot=tot;
    }
}
