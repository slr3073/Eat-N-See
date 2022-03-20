package slr.api;

import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Test API REST Countries")
public class RestCountriesTest {
    @Test
    @DisplayName("Récupération pays/adjectif OK")
    void responseIsCorrectTest(){
        //given
        //when
        Pair<String, String> actualCountryAdjective = RestCountries.getInfos("FR");

        //then
        assertEquals(new ImmutablePair<>("France", "French"), actualCountryAdjective);
    }

    @Test
    @DisplayName("Code Invalide ISO")
    void responseWithInvalidISOTest(){
        //given possible http status
        List<Response.Status> status = Arrays.asList(Response.Status.values());
        //when then
        Throwable actualException = assertThrows(IllegalArgumentException.class, () -> RestCountries.getInfos("U6"));
        assertEquals("Erreur : 404", actualException.getMessage());
    }

    @Test
    @DisplayName("Code Invalide")
    void responseIsInvalid(){
        //given
        //when then
        Throwable actualException = assertThrows(IllegalAccessError.class, () -> RestCountries.getInfos("ZA"));
        assertTrue(actualException.getMessage().contains(" n'est pas un code connu (TheMealDB)"));
    }

    @Test
    @DisplayName("Récupération coordonnées OK")
    void getCapitalCoordinates() {
        //given
        //when
        Pair<Double, Double> actualCoordinates =  RestCountries.getCapitalCoordinates("FR");

        //then
        assertEquals(new ImmutablePair<>(48.87d, 2.33d), actualCoordinates);
    }
}
