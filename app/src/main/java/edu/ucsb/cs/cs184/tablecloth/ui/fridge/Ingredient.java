package edu.ucsb.cs.cs184.tablecloth.ui.fridge;

public class Ingredient {

    public String ingredient;
    public int count;

    public Ingredient() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Ingredient(String ingredient) {
        this.ingredient = ingredient;
        this.count = 1;
    }
}
