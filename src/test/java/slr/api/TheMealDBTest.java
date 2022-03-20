package slr.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import slr.entities.Recipe;
import slr.entities.theMealDB.Meal;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test API The Meal DB")
public class TheMealDBTest {

    @Nested
    @DisplayName("Récupération d'un plat")
    class GetMealTest {
        @Test
        @DisplayName("Pays spécifique OK")
        void getCountryMealAndSpecificMealInstruction(){
            //given countryAdjective and idMeals from that country
            Recipe expectedRecipe1 = TheMealDB.getRecipe("52967");
            Recipe expectedRecipe2 = TheMealDB.getRecipe("52968");
            // C'est le plus facile à tester même si ce n'est pas une bonne pratique

            // when
            Recipe actualRecipe = TheMealDB.getCountryRecipe("Kenyan");

            // then it should be one of these two meals.
            assertMeal(actualRecipe);
            assertTrue(actualRecipe.equals(expectedRecipe1) || actualRecipe.equals(expectedRecipe2));
        }

        @Test
        @DisplayName("Plat aléatoire car adjectif inconnu OK")
        void getRdmMealFromUnknownAdjective(){
            // given
            // when we fetch a rdm meal
            Recipe actualRecipe = TheMealDB.getCountryRecipe("UnknownAdjective");

            // then we get something
            assertMeal(actualRecipe);
        }
        @Test
        @DisplayName("Plat aléatoire OK")
        void getRdmRecipe(){
            //given
            //when
            Recipe actualRecipe = TheMealDB.getRandomRecipe();

            //then
            assertMeal(actualRecipe);
        }

        @Test
        @DisplayName("Plat spécifique OK")
        void getRecipe(){
            // given
            // when
            Recipe actualRecipe = TheMealDB.getRecipe("52967");
            // then we get something
            assertMeal(actualRecipe);
        }

    }

    @Test
    @DisplayName("Récupération des adjectifs OK")
    void getAdjectives(){
        // Given the full list of countries
        List<String> expectedAdjectives = Arrays.asList("American", "British", "Canadian", "Chinese", "Croatian",
                "Dutch", "Egyptian", "French", "Greek", "Indian", "Irish", "Italian", "Jamaican",
                "Japanese", "Kenyan", "Malaysian", "Mexican", "Moroccan", "Polish", "Portuguese", "Russian",
                "Spanish", "Thai", "Tunisian", "Turkish", "Unknown", "Vietnamese");

        // when
        List<String> actualAdjectives = TheMealDB.getAllCountryAdjectives();

        // then
        assertEquals(expectedAdjectives, actualAdjectives);
    }

    private void assertMeal(Recipe recipe){
        assertNotNull(recipe);
        assertFalse(recipe.getArea().isEmpty());
        assertFalse(recipe.getImage_url().isEmpty());
        assertFalse(recipe.getThemealdb_url().isEmpty());
        assertFalse(recipe.getName().isEmpty());
        assertFalse(recipe.getInstructions().isEmpty());
    }
}
