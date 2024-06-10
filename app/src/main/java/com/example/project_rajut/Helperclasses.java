package com.example.project_rajut;

public class Helperclasses {

    String id, kategori, tanggal, bulan, tahun, keterangan, jenistransaksi, totalbarang;
    int jumlahbayar;

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

    public String getTotalbarang() {
        return totalbarang;
    }

    public void setTotalbarang(String totalbarang) {
        this.totalbarang = totalbarang;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getJenistransaksi() {
        return jenistransaksi;
    }

    public void setJenistransaksi(String jenistransaksi) {
        this.jenistransaksi = jenistransaksi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getBulan() {
        return bulan;
    }

    public void setBulan(String bulan) {
        this.bulan = bulan;
    }

    public String getTahun() {
        return tahun;
    }

    public void setTahun(String tahun) {
        this.tahun = tahun;
    }

    public int getJumlahbayar() {
        return jumlahbayar;
    }

    public void setJumlahbayar(int jumlahbayar) {
        this.jumlahbayar = jumlahbayar;
    }

    public Helperclasses(String id, String kategori, String totalbarang, String keterangan, String jenistransaksi, String tanggal, String bulan, String tahun, int jumlahbayar) {
        this.id = id;
        this.kategori = kategori;
        this.totalbarang = totalbarang;
        this.keterangan = keterangan;
        this.jenistransaksi = jenistransaksi;
        this.tanggal = tanggal;
        this.bulan = bulan;
        this.tahun = tahun;
        this.jumlahbayar = jumlahbayar;
    }

    public Helperclasses() {
    }
}
