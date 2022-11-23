package com.example.e_selojari;

public class Riwayat {
    private String no_nota;
    private String tgl_jual;
    private String subtot;
    private String ongkir;
    private String total;
    private String status;


    public Riwayat() {
        this.no_nota = no_nota;
        this.tgl_jual = tgl_jual;
        this.subtot = subtot;
        this.ongkir = ongkir;
        this.total = total;
        this.status = status;
    }

    public String getNo_nota() {
        return no_nota;
    }

    public void setNo_nota(String no_nota) {
        this.no_nota = no_nota;
    }

    public String getTgl_jual() {
        return tgl_jual;
    }

    public void setTgl_jual(String tgl_jual) {
        this.tgl_jual = tgl_jual;
    }

    public String getSubtot() {
        return subtot;
    }

    public void setSubtot(String subtot) {
        this.subtot = subtot;
    }

    public String getOngkir() {
        return ongkir;
    }

    public void setOngkir(String ongkir) {
        this.ongkir = ongkir;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
