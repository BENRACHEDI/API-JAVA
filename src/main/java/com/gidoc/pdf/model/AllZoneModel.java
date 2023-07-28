package com.gidoc.pdf.model;

import java.util.ArrayList;
import java.util.List;

import com.gidoc.pdf.model.Zone;

public class AllZoneModel {

    private int id;
    private List<Zone> zoneList;
    // private String nomModel;

    public void AllZoneModel() {
        id = id;
        zoneList =new ArrayList<Zone>();
    } 
 
    public void AllZoneModel(int id, List<Zone> zoneList) {
        this.id = id;
        this.zoneList = zoneList;
       // this.nomModel = nomModel;
    }  

    public List<Zone> getZoneList() {
        return zoneList;
    }
    public int getId() {
        return id;
    }  
    /*     public String getNomModel() {
            return nomModel;
        }  */ 

    public void setZoneList(List<Zone> zoneList) {
        this.zoneList = zoneList;
    }
    public void setId(int id) {
        this.id = id;
    }
    /*  public void setNomModel(String nomModel) {
         this.nomModel = nomModel;
     } */
    
}
