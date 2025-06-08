package io.hellorin.proteinpulseai.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "COMPO")
public class Composition {
    private String alimCode;
    private String constCode;
    private String constNomFr;
    private String constNomEng;
    private String teneur;
    private String min;
    private String max;
    private String codeConfiance;
    private String sourceCode;

    public Composition() {
        // For JAX-B
    }

    public Composition(String alimCode, String constCode, String teneur) {
        this.alimCode = alimCode;
        this.constCode = constCode;
        this.teneur = teneur;
    }

    @XmlElement(name = "alim_code")
    public String getAlimCode() {
        return alimCode;
    }

    public void setAlimCode(String alimCode) {
        this.alimCode = alimCode;
    }

    @XmlElement(name = "const_code")
    public String getConstCode() {
        return constCode;
    }

    public void setConstCode(String constCode) {
        this.constCode = constCode;
    }

    @XmlElement(name = "const_nom_fr")
    public String getConstNomFr() {
        return constNomFr;
    }

    public void setConstNomFr(String constNomFr) {
        this.constNomFr = constNomFr;
    }

    @XmlElement(name = "const_nom_eng")
    public String getConstNomEng() {
        return constNomEng;
    }

    public void setConstNomEng(String constNomEng) {
        this.constNomEng = constNomEng;
    }

    @XmlElement(name = "teneur")
    public String getTeneur() {
        return teneur;
    }

    public void setTeneur(String teneur) {
        this.teneur = teneur;
    }

    @XmlElement(name = "min")
    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    @XmlElement(name = "max")
    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    @XmlElement(name = "code_confiance")
    public String getCodeConfiance() {
        return codeConfiance;
    }

    public void setCodeConfiance(String codeConfiance) {
        this.codeConfiance = codeConfiance;
    }

    @XmlElement(name = "source_code")
    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    @Override
    public String toString() {
        return "Composition{" +
                "alimCode='" + alimCode + '\'' +
                ", constCode='" + constCode + '\'' +
                ", constNomFr='" + constNomFr + '\'' +
                ", constNomEng='" + constNomEng + '\'' +
                ", teneur='" + teneur + '\'' +
                ", min='" + min + '\'' +
                ", max='" + max + '\'' +
                ", codeConfiance='" + codeConfiance + '\'' +
                ", sourceCode='" + sourceCode + '\'' +
                '}';
    }
} 