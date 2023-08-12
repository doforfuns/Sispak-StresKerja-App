package com.sistempakarstreskerja;

public class Gejala {
    private String kodeGejala;
    private String namaGejala;
    private String selectedAnswer;

    public Gejala(String kodeGejala, String namaGejala, String selectedAnswer) {
        this.kodeGejala = kodeGejala;
        this.namaGejala = namaGejala;
        this.selectedAnswer = selectedAnswer;
    }

    public String getKodeGejala() {
        return kodeGejala;
    }

    public void setKodeGejala(String kodeGejala) {
        this.kodeGejala = kodeGejala;
    }

    public String getNamaGejala() {
        return namaGejala;
    }

    public void setNamaGejala(String namaGejala) {
        this.namaGejala = namaGejala;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    // Tambahan metode lain sesuai kebutuhan

    @Override
    public String toString() {
        return "Gejala{" +
                "kodeGejala='" + kodeGejala + '\'' +
                ", namaGejala='" + namaGejala + '\'' +
                ", selectedAnswer='" + selectedAnswer + '\'' +
                '}';
    }
}

