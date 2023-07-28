package com.gidoc.pdf.model;

import java.util.ArrayList;
import java.util.List;
import com.gidoc.pdf.model.Classement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;




@XmlRootElement(name = "RegleClassement")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClassementList {

    private String nomClassement;
    private String nomModel;
    @XmlElement(name = "Classement") 
    public List<Classement> classementList;

    public void ClassementList() {
        nomClassement =  nomClassement;
        nomModel = nomModel;
        classementList =  new ArrayList<Classement>();
    } 

    public void ClassementList(List<Classement> classementList, String nomClassement, String nomModel) {
        this.nomClassement = nomClassement;
        this.nomModel = nomModel;
        this.classementList = classementList;
    }  

    //@XmlElement
    public List<Classement> getClassementList() {
        return classementList;
    }
    public String getNomClassement() {
        return nomClassement;
    }  
    public String getNomModel() {
        return nomModel;
    }  

    public void setClassementList(List<Classement> classementList) {
        this.classementList = classementList;
    }
    public void setNomClassement(String nomClassement) {
        this.nomClassement = nomClassement;
    }
    public void setNomModel(String nomModel) {
        this.nomModel = nomModel;
    }
    
}
