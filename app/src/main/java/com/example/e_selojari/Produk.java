package com.example.e_selojari;

public class Produk {
    private String kode;
    private String nama;
    private String harga;
    private int jumlah;
    private String img;

    public Produk() {
    }

    public Produk(String kode, String nama, String harga, String img, int jumlah) {
        this.kode = kode;
        this.nama = nama;
        this.harga = harga;
        this.jumlah = jumlah;
        this.img = img;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
    public String getKode() { return kode; }
    public void setKode(String kode) {
        this.kode = kode;
    }
    public int getJumlah() {
        return jumlah;
    }
    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
    public String getHarga() {
        return harga;
    }
    public void setHarga(String harga) {
        this.harga = harga;
    }
    public void setImg(String img) {
        this.img = img;
    }
    public String getImg() {
        return "http://192.168.43.210/E-Selojari/images/produk/" +img;
    }
}
