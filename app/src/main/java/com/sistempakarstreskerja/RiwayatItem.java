package com.sistempakarstreskerja;

public class RiwayatItem {
    private String namaPenyakit;
    private String tanggal;
    private String metode;
    private String idPenyakit;

    public RiwayatItem(String namaPenyakit, String tanggal, String metode, String idPenyakit) {
        this.namaPenyakit = namaPenyakit;
        this.tanggal = tanggal;
        this.metode = metode;
        this.idPenyakit = idPenyakit;
    }

    public String getNamaPenyakit() {
        return namaPenyakit;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getMetode() {
        return metode;
    }

    public String getIdPenyakit() {
        return idPenyakit;
    }
}
