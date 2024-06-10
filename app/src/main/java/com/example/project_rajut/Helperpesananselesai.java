package com.example.project_rajut;

public class Helperpesananselesai {

    String id ,kategori, tanggal, namapemesan, kontak, selesai, jumlahbayar, totalbarang;

    public String getId() {
        return id;
    }

    public String getKategori() {
        return kategori;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getNamapemesan() {
        return namapemesan;
    }

    public String getJumlahbayar() {
        return jumlahbayar;
    }

    public String getKontak() {
        return kontak;
    }

    public String getSelesai() {
        return selesai;
    }

    public String getTotalbarang() {
        return totalbarang;
    }

    public Helperpesananselesai(String id, String kategori, String tanggal, String namapemesan, String jumlahbayar, String kontak, String selesai, String totalbarang) {
        this.id = id;
        this.kategori = kategori;
        this.tanggal = tanggal;
        this.namapemesan = namapemesan;
        this.jumlahbayar = jumlahbayar;
        this.kontak = kontak;
        this.selesai = selesai;
        this.totalbarang = totalbarang;
    }
}
