package slr.api;

import jakarta.json.JsonObject;
import jakarta.json.JsonValue;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import slr.entities.Recipe;
import slr.entities.theMealDB.TheMealDbResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
public class TheMealDB {
    private final static String THEMEALDB_URL = "https://www.themealdb.com/api/json/v1/1/";

    /**
     * Renvoie la liste de adjectif propre aux plusieurs pays dans l'API.
     * @return une ArrayList contenant tous les adjectifs des pays.
     */
    public static List<String> getAllCountryAdjectives(){
        log.info("Appel API TheMealDB : getAllCountryAdjectives");

        Client client = ClientBuilder.newClient();
        JsonObject res = client.target(THEMEALDB_URL)
                .path("list.php")
                .queryParam("a", "list")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get(JsonObject.class);

        List<String> countryAdjectives = new ArrayList<>();
        for(JsonValue jsonObj: res.getJsonArray("meals"))
            countryAdjectives.add(jsonObj.asJsonObject().getString("strArea"));

        return countryAdjectives;
    }

    /**
     * Renvoie les informations d'une recette choisie parmi celles référencées dans ce pays
     * Ou prend une recette au hasard si @countryAdjective n'est pas trouvable dans la liste des pays.
     * @param countryAdjective adjectif en anglais qualifiant une région particulière.
     * @return les informations d'une recette tirée au hasard parmi une liste de recettes.
     */
    public static Recipe getCountryRecipe(String countryAdjective){
        Random rdm = new Random();
        List<String> countries = getAllCountryAdjectives().parallelStream().map(String::toLowerCase).collect(Collectors.toList());

        if(!countries.contains(countryAdjective.toLowerCase())){
            return getRandomRecipe();
        }

        log.info("Appel API TheMealDB : getCountryMeal");

        Client client = ClientBuilder.newClient();
        JsonObject res = client.target(THEMEALDB_URL)
                .path("filter.php")
                .queryParam("a", countryAdjective)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get(JsonObject.class);

        String idMeal = res.getJsonArray("meals")
                .get(rdm.nextInt(res.getJsonArray("meals").size()))
                .asJsonObject()
                .getString("idMeal");

        return getRecipe(idMeal);
    }

    /**
     * Accède à la page https://www.themealdb.com/api/json/v1/1/random.php
     * Récupère les informations de la recette dans le JSON.
     * @return les instructions d'un repas tiré au hasard.
     */
    public static Recipe getRandomRecipe(){
        log.info("Appel API TheMealDB : getRandomMeal");

        Client client = ClientBuilder.newClient();
        TheMealDbResponse res = client.target(THEMEALDB_URL)
                .path("random.php")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get(TheMealDbResponse.class);

        return res.getMeals().get(0).getRecipe();
    }

    /**
     * Appelle l'API TheMealDB sur cette adresse : https://www.themealdb.com/api/json/v1/1/lookup.php?i=<idMeal>
     * Récupère les informations de la recette dans le JSON.
     * @param idMeal pour l'id du repas
     * @return les instructions d'un repas auquel on a au préalable obtenu l'id.
     */
    public static Recipe getRecipe(String idMeal){
        log.info("Appel API TheMealDB : getMealInstructions");

        Client client = ClientBuilder.newClient();
        TheMealDbResponse res = client.target(THEMEALDB_URL)
                .path("lookup.php")
                .queryParam("i", idMeal)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get(TheMealDbResponse.class);

        return res.getMeals().get(0).getRecipe();
    }
}
