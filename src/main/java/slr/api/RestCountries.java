package slr.api;

import jakarta.json.*;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import java.io.StringReader;
import static org.apache.commons.lang3.ArrayUtils.contains;

@Slf4j
public class RestCountries {
    private final static String REST_COUNTRY_URL = "https://restcountries.com";
    private final static String[] COUNTRY_ADJECTIVE_IN_MEAL = {"American", "British", "Canadian", "Chinese", "Croatian", "Dutch", "Egyptian", "French", "Greek", "Indian", "Irish", "Italian", "Jamaican", "Japanese", "Kenyan", "Malaysian", "Mexican", "Moroccan", "Polish", "Portuguese", "Russian", "Spanish", "Thai", "Tunisian", "Turkish", "Unknown", "Vietnamese"};


    /**
     * Transforme une adresse IP en code pays ISO 3166-1 alpha-2
     * @param country_code code pays ISO 3166-1 alpha-2
     * @return String[2] renvoie le nom du pays en anglais & l'adjectif en anglais qui le caractérisera dans TheMealDB
     */
    public static Pair<String, String> getInfos(String country_code) throws IllegalArgumentException {
        log.info("Appel API RestCountries : getInfos");

        Client client = ClientBuilder.newClient();
        Response response = client.target(REST_COUNTRY_URL)
                .path("/v2/alpha/" + country_code)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get();

        if (response.getStatus() == 200) {
            StringReader stringReader = new StringReader(response.readEntity(String.class));
            JsonReader jsonReader = Json.createReader(stringReader);
            JsonObject json = jsonReader.readObject();
            Pair<String, String> pair = new ImmutablePair<>(json.getString("name"), json.getString("demonym"));
            if (contains(COUNTRY_ADJECTIVE_IN_MEAL, pair.getRight())) {
                return pair;
            } else {
                throw new IllegalAccessError(json.getString("demonym") + " n'est pas un code connu (TheMealDB)");
            }
        }
        else {
            throw new IllegalArgumentException("Erreur : " + response.getStatus());
        }
        
    }


    /**
     * Donne les coordonnées de la capitale du pays
     * @param countryCode Un code pays ISO 3166-1 alpha-2
     * @return Paire latitude / longitude
     */
    public static Pair<Double, Double> getCapitalCoordinates(String countryCode) {
        log.info("Appel API RestCountries : getCapitalCoordinates");

        Client client = ClientBuilder.newClient();
        JsonArray response = client.target(REST_COUNTRY_URL)
                .path("v3.1/alpha/" + countryCode)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get(JsonArray.class);

        Double lat = response.getJsonObject(0).getJsonObject("capitalInfo").getJsonArray("latlng").getJsonNumber(0).doubleValue();
        Double lng = response.getJsonObject(0).getJsonObject("capitalInfo").getJsonArray("latlng").getJsonNumber(1).doubleValue();

        return new ImmutablePair<>(lat,lng);
    }
}