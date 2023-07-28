package com.gidoc.pdf.model;

public class ListZoneModel {
    private int id;
    private String nomZone;

    public ListZoneModel(int id, String nomZone) {
        this.id = id;
        this.nomZone = nomZone;
    }
    public ListZoneModel() {}

    public int getId() {
        return id;
    }
    public String getnomZone() {
        return nomZone;
    }

    @Override
    public String toString() {
        return "Item [id=" + id + ", nomZone=" + nomZone + "]";
    }
}
