package fudan.se.lab4.repository;

import fudan.se.lab4.entity.Ingredient;

public interface IngredientRepository {

    /**
     * Get Ingredient by name in data/ingredient.csv
     *
     * @param name
     * @return ingredient
     */
    Ingredient getIngredient(String name);

    void createIngredient(Ingredient ingredient);
}
