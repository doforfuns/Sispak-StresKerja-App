package com.sistempakarstreskerja;

public class DiagnosaCFGejala {
    private String kodeGejala;
    private String namaGejala;
    private String selectedAnswer;

    public DiagnosaCFGejala(String kodeGejala, String namaGejala, String selectedAnswer) {
        this.kodeGejala = kodeGejala;
        this.namaGejala = namaGejala;
        this.selectedAnswer = selectedAnswer;
    }

    public String getKodeGejala() {
        return kodeGejala;
    }

    public String getNamaGejala() {
        return namaGejala;
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

