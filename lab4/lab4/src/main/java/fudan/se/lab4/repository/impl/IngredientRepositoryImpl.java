package fudan.se.lab4.repository.impl;

import fudan.se.lab4.constant.FileConstant;
import fudan.se.lab4.constant.InfoConstant;

import fudan.se.lab4.entity.Ingredient;
import fudan.se.lab4.repository.IngredientRepository;
import fudan.se.lab4.util.FileUtil;

import java.text.MessageFormat;

public class IngredientRepositoryImpl implements IngredientRepository {
    @Override
    public Ingredient getIngredient(String name) {
        return stringArrayToObject(FileUtil.readByName(name, FileConstant.INGREDIENT_CSV));
    }

    @Override
    public void createIngredient(Ingredient ingredient) {
        FileUtil.write(objectToStringArray(ingredient), FileConstant.INGREDIENT_CSV);
    }

    private String[] objectToStringArray(Ingredient ingredient) {
        // if user already exists, throw exception
        if (FileUtil.exist(ingredient.getName(), FileConstant.INGREDIENT_CSV)) {
            throw new RuntimeException(MessageFormat.format(InfoConstant.ENTITY_EXIST, "Ingredient",
                    ingredient.getName()));
        }
        String[] array = new String[3];
        array[0] = ingredient.getName();
        array[1] = ingredient.getDescription();
        array[2] = String.valueOf(ingredient.getPrice());
//        array[3] = String.valueOf(ingredient.getSize());
        return array;
    }

    private Ingredient stringArrayToObject(String[] array) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(array[0]);
        ingredient.setDescription(array[1]);
        ingredient.setPrice(Double.parseDouble(array[2]));
//        ingredient.setSize(Integer.parseInt(array[3]));
        return ingredient;
    }

    public String getName(){
        return "Ingredient";
    }
}
