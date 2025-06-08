package io.hellorin.proteinpulseai.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "TABLE")
public class FoodItemList {
    private List<FoodItem> foodItems = new ArrayList<FoodItem>();

    @XmlElement(name = "ALIM")
    public List<FoodItem> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(List<FoodItem> foodItems) {
        this.foodItems = foodItems;
    }
} 