package com.gidoc.pdf.model;


import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlAccessType;

@XmlType(propOrder = {"nomZone", "p" , "x", "y", "w", "h"})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Zone {
@XmlAttribute
private String nomZone;
private int p;
private int x;
private int y;
private int w;
private int h;

public Zone() {}
public Zone(String nomZone, int p , int x, int y, int w, int h) {
        super();
        this.nomZone = nomZone;
        this.p = p;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

public String getnomZone() { return nomZone; }
public void setnomZone(String nomZone) { this.nomZone=nomZone; }

public int getp() { return p; }
public void setp(int p) { this.p=p; }

public int getx() { return x; }
public void setx(int x) {  this.x=x; }

public int gety() { return y; }
public void sety(int y) {  this.y=y; }

public int getw() { return w; }
public void setw(int w) {  this.w=w; }

public int geth() { return h; }
public void seth(int h) { this.h=h; }
 
}
