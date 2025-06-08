package io.hellorin.proteinpulseai.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "TABLE")
public class CompositionTable {
    private List<Composition> compositions;

    @XmlElement(name = "COMPO")
    public List<Composition> getCompositions() {
        return compositions;
    }

    public void setCompositions(List<Composition> compositions) {
        this.compositions = compositions;
    }

    @Override
    public String toString() {
        return "CompositionTable{" +
                "compositions=" + compositions +
                '}';
    }
} 