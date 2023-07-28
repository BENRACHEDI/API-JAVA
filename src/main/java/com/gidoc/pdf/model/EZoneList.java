package com.gidoc.pdf.model;

import java.util.ArrayList;
import java.util.List;

import com.gidoc.pdf.model.EZone;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "EZone")
@XmlAccessorType(XmlAccessType.FIELD)
public class EZoneList {

    @XmlElement(name = "ERectangle") 
    public List<EZone> ezoneList;
    private String nomModel;
    private String nomFile;

    public  EZoneList() {} 

    public EZoneList(List<EZone> ezoneList, String nomModel, String nomFile) {
        this.ezoneList = ezoneList;
        this.nomModel = nomModel;
        this.nomFile = nomFile;
    }  

    //@XmlElement
    public List<EZone> getEZoneList() {
        return ezoneList;
    }
    public String getNomModel() {
        return nomModel;
    }  
    public String getNomFile() {
        return nomFile;
    }  

    public void setEZoneList(List<EZone> ezoneList) {
        this.ezoneList = ezoneList;
    }
    public void setNomModel(String nomModel) {
        this.nomModel = nomModel;
    }
    public void setNomFile(String nomFile) {
        this.nomFile = nomFile;
    }
    
}
