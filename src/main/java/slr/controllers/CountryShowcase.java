package slr.controllers;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.tuple.Pair;
import slr.api.*;
import slr.entities.FailureResponse;
import slr.entities.OkResponse;
import slr.entities.Photo;
import slr.entities.Recipe;

import java.util.List;

@Path("/country_showcase")
public class CountryShowcase {

    @GET
    @Path("/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCountryInfo(@PathParam("code") String code) {
        String adjective = "";
        boolean random_area = false;

        try {
            adjective = RestCountries.getInfos(code).getRight();
        } catch (IllegalAccessError e) {
            random_area = true;
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.OK).entity(new FailureResponse(false, "REST Countries", e.getMessage())).build();
        }

        Pair<Double, Double> coordinates = RestCountries.getCapitalCoordinates(code);
        List<Photo> photos = Flickr.getRdmPhotos(coordinates.getLeft(), coordinates.getRight(), 5);
        Recipe recipe = TheMealDB.getCountryRecipe(adjective);

        OkResponse response = new OkResponse(true, IPInfo.getIP(), code, random_area, recipe, photos);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLocalCountryInfo() {
        return getCountryInfo(CountryIs.getCountryCode(IPInfo.getIP()));
    }
}
