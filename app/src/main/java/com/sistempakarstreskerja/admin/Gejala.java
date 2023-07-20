package com.sistempakarstreskerja.admin;

public class Gejala {
    private String name;
    private boolean selected;
    private double nilaiCf;

    public Gejala(String name, boolean selected) {
        this.name = name;
        this.selected = selected;
        this.nilaiCf = 0.0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public double getNilaiCf() {
        return nilaiCf;
    }

    public void setNilaiCf(double nilaiCf) {
        this.nilaiCf = nilaiCf;
    }
}
