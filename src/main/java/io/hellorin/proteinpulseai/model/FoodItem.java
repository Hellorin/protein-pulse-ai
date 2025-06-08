package io.hellorin.proteinpulseai.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ALIM")
public class FoodItem {
    private String alimCode;
    private String alimNomEng;
    private String alimGrpCode;
    private String alimSsgrpCode;
    private String alimSsssgrpCode;

    public FoodItem() {
        // For JAX-B
    }

    public FoodItem(String alimCode, String alimNomEng) {
        this.alimCode = alimCode;
        this.alimNomEng = alimNomEng;
    }

    @XmlElement(name = "alim_code")
    public String getAlimCode() {
        return alimCode;
    }

    public void setAlimCode(String alimCode) {
        this.alimCode = alimCode;
    }

    @XmlElement(name = "alim_nom_eng")
    public String getAlimNomEng() {
        return alimNomEng;
    }

    public void setAlimNomEng(String alimNomEng) {
        this.alimNomEng = alimNomEng;
    }

    @XmlElement(name = "alim_grp_code")
    public String getAlimGrpCode() {
        return alimGrpCode;
    }

    public void setAlimGrpCode(String alimGrpCode) {
        this.alimGrpCode = alimGrpCode;
    }

    @XmlElement(name = "alim_ssgrp_code")
    public String getAlimSsgrpCode() {
        return alimSsgrpCode;
    }

    public void setAlimSsgrpCode(String alimSsgrpCode) {
        this.alimSsgrpCode = alimSsgrpCode;
    }

    @XmlElement(name = "alim_ssssgrp_code")
    public String getAlimSsssgrpCode() {
        return alimSsssgrpCode;
    }

    public void setAlimSsssgrpCode(String alimSsssgrpCode) {
        this.alimSsssgrpCode = alimSsssgrpCode;
    }
} 