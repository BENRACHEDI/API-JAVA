package com.gidoc.pdf.model;

import java.util.List;

public class DataPath {

  private List<EZoneList> ezoneList;
  private String nomClassement;

  public List<EZoneList> getEZoneList() {
    return ezoneList;
  }

  public void setEZoneList(List<EZoneList> ezoneList) {
    this.ezoneList = ezoneList;
  }

  public String getNomClassement() {
    return nomClassement;
  }

  public void setNomClassement(String nomClassement) {
    this.nomClassement = nomClassement;
  }
}