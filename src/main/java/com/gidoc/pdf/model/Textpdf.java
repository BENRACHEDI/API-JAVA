package com.gidoc.pdf.model;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlAccessType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Textpdf {
    public String zone;
    public String text;

    public Textpdf(){}
    public Textpdf(String zone, String text){
         this.zone=zone;
         this.text=text;
    }
    
public String getzone() { return zone; }
public void setzone(String zone) { this.zone=zone; }

public String gettext() { return text; }
public void settext(String text) { this.text=text; }

  public void add(String zone, String text){
         this.zone=zone;
         this.text=text;
    }

}
