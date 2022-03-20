package slr.controllers;

import jakarta.json.stream.JsonParser;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.leadpony.justify.api.JsonSchema;
import org.leadpony.justify.api.JsonValidationService;
import org.leadpony.justify.api.ProblemHandler;

import java.io.ByteArrayInputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test Controller Country Showcase")
class CountryShowcaseTest extends JerseyTest {
    JsonValidationService service = JsonValidationService.newInstance();
    JsonSchema schema = service.readSchema(Paths.get("src/test/resources/eat-and-see.schema.json"));

    @Override
    protected Application configure() {
        return new ResourceConfig(CountryShowcase.class);
    }

    @Test
    @DisplayName("Pas de Code OK")
    void countryShowcaseWithoutParamOK(){
        //given
        //when
        Response response = target("/country_showcase").request().get();
        String actualJsonResult = response.readEntity(String.class);

        //then
        assertResponseFormat(response);
        assertJsonFormat(actualJsonResult);
        assertTrue(actualJsonResult.contains("\"country_alpha2\":\"FR\""));
    }

    @Test
    @DisplayName("Cas Passant OK")
    void countryShowcaseParameterizedOK(){
        //given
        //when
        Response response = target("/country_showcase/GB").request().get();
        String actualJsonResult = response.readEntity(String.class);

        //then
        assertResponseFormat(response);
        assertJsonFormat(actualJsonResult);
        assertTrue(actualJsonResult.contains("\"country_alpha2\":\"GB\""));
        assertTrue(actualJsonResult.contains("\"random_area\":false"));
    }

    @Test
    @DisplayName("Code Inconnu Recette Aléatoire OK")
    void countryShowcaseUnknownCode(){
        //given
        //when
        Response response = target("/country_showcase/ZA").request().get();
        String actualJsonResult = response.readEntity(String.class);

        //then
        assertResponseFormat(response);
        assertJsonFormat(actualJsonResult);
        assertTrue(actualJsonResult.contains("\"country_alpha2\":\"ZA\""));
        assertTrue(actualJsonResult.contains("\"random_area\":true"));
    }

    @Test
    @DisplayName("Code Invalide ERREUR")
    void countryShowcaseInvalidCode(){
        //given
        //when
        Response response = target("/country_showcase/U6").request().get();
        String actualJsonResult = response.readEntity(String.class);

        //then
        assertResponseFormat(response);
        assertJsonFormat(actualJsonResult);
        assertTrue(actualJsonResult.contains("\"api_failed\":\"REST Countries\""));
        assertTrue(actualJsonResult.contains("\"api_status\":\"Erreur : 404\""));
        assertTrue(actualJsonResult.contains("\"success\":false"));
    }

    void assertResponseFormat(Response response){
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
    }

    void assertJsonFormat(String json){
        List<String> errors = new ArrayList<>();
        ProblemHandler handler = service.createProblemPrinter(errors::add);

        try (JsonParser parser = service.createParser(new ByteArrayInputStream(json.getBytes()), schema, handler)) {
            while (parser.hasNext()) {
                JsonParser.Event event = parser.next();
            }
        }
        assertTrue(errors.size() == 0);
    }
}