package com.gidoc.pdf.model;

import java.util.ArrayList;
import java.util.List;

import com.gidoc.pdf.model.Zone;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "Zone")
@XmlAccessorType(XmlAccessType.FIELD)
public class ZoneList {

    @XmlElement(name = "Rectangle") 
    public List<Zone> zoneList;
    private String nomModel;

    public void ZoneList() {
        zoneList =  new ArrayList<Zone>();
        nomModel =  nomModel;
    } 

    public void ZoneList(List<Zone> zoneList, String nomModel) {
        this.zoneList = zoneList;
        this.nomModel = nomModel;
    }  

    //@XmlElement
    public List<Zone> getZoneList() {
        return zoneList;
    }
    public String getNomModel() {
        return nomModel;
    }  

    public void setZoneList(List<Zone> zoneList) {
        this.zoneList = zoneList;
    }
    public void setNomModel(String nomModel) {
        this.nomModel = nomModel;
    }
    
}
