package com.sistempakarstreskerja;

public class HasilDiagnosis {
    private String idPenyakit;
    private String namaPenyakit;
    private String nilai;

    public HasilDiagnosis(String idPenyakit, String namaPenyakit, String nilai) {
        this.idPenyakit = idPenyakit;
        this.namaPenyakit = namaPenyakit;
        this.nilai = nilai;
    }

    // Getters and setters (if needed)
    public String getIdPenyakit() {
        return idPenyakit;
    }

    public void setIdPenyakit(String idPenyakit) {
        this.idPenyakit = idPenyakit;
    }

    public String getNamaPenyakit() {
        return namaPenyakit;
    }

    public void setNamaPenyakit(String namaPenyakit) {
        this.namaPenyakit = namaPenyakit;
    }

    public String getNilai() {
        return nilai;
    }

    public void setNilai(String nilai) {
        this.nilai = nilai;
    }
}
