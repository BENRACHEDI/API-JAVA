package com.gidoc.pdf.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Document")
@XmlAccessorType(XmlAccessType.FIELD)
public class TextpdfList {

    @XmlElement(name = "Rectangle") 
    public List<Textpdf> textpdf= new ArrayList<>();
    
}
