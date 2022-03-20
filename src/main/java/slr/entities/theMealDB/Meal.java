package slr.entities.theMealDB;

import lombok.Data;
import slr.entities.Recipe;

@Data
public class Meal {
    private String idMeal;
    private String strArea;
    private String strMeal;
    private String strInstructions;
    private String strMealThumb;
    private String strSource;

    public Recipe getRecipe() {
        return new Recipe(strArea, strMeal, strInstructions, strMealThumb, "https://www.themealdb.com/meal/" + idMeal, strSource);
    }
}
