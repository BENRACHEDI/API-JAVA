package com.gidoc.pdf.model;

import java.util.List;

public class EZoneListData {
    public List<EZoneList> eZoneListArray;
    public String nomClassement;

    public EZoneListData() {
        // Constructeur vide requis par Spring pour la désérialisation JSON
    }

    public EZoneListData(List<EZoneList> eZoneListArray, String nomClassement) {
        this.eZoneListArray = eZoneListArray;
        this.nomClassement = nomClassement;
    }

    // Getters and setters (ou utilisez Lombok pour générer automatiquement les getters et setters)

    public List<EZoneList> getEZoneListArray() {
        return eZoneListArray;
    }

    public void setEZoneListArray(List<EZoneList> eZoneListArray) {
        this.eZoneListArray = eZoneListArray;
    }

    public String getNomClassement() {
        return nomClassement;
    }

    public void setNomClassement(String nomClassement) {
        this.nomClassement = nomClassement;
    }
}
