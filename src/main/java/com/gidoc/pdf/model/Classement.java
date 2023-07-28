package com.gidoc.pdf.model;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlAccessType;

@XmlType(propOrder = { "id", "nomZone"})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Classement {

@XmlAttribute
private int id;
private String nomZone;

public Classement() {}
public Classement( int id, String nomZone ) {
        super();
        this.id = id;
        this.nomZone = nomZone;
    }

public String getnomZone() { return nomZone; }
public void setnomZone(String nomZone) { this.nomZone=nomZone; }

public int getId() { return id; }
public void setId(int id) { this.id=id; }

 
}
