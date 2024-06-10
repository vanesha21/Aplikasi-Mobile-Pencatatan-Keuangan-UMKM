package com.example.project_rajut;

public class HelperPesanan {

    String id, kategori, tanggal, namapemesan, kontak, totalbarang, jumlahbayar;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getNamapemesan() {
        return namapemesan;
    }

    public void setNamapemesan(String namapemesan) {
        this.namapemesan = namapemesan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getJumlahbayar() {
        return jumlahbayar;
    }

    public void setJumlahbayar(String jumlahbayar) {
        this.jumlahbayar = jumlahbayar;
    }

    public String getKontak() {
        return kontak;
    }

    public void setKontak(String kontak) {
        this.kontak = kontak;
    }

    public String getTotalbarang() {
        return totalbarang;
    }

    public void setTotalbarang(String totalbarang) {
        this.totalbarang = totalbarang;
    }

    public HelperPesanan(String id , String kategori, String nama, String tanggal, String jumlahbayar, String kontak, String totalbarang) {
        this.id = id;
        this.kategori = kategori;
        this.namapemesan = nama;
        this.tanggal = tanggal;
        this.jumlahbayar = jumlahbayar;
        this.kontak = kontak;
        this.totalbarang = totalbarang;
    }

    public HelperPesanan() {
    }
}
