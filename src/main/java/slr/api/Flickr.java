package slr.api;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import slr.entities.Flickr.FlickerResponse;
import slr.entities.Flickr.PhotoFlicker;
import slr.entities.Photo;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class Flickr {
    private final static String API_KEY = "4ef6913f3d4f9513b006b426b89e7b61";
    private final static String FLICKR_URL = "https://www.flickr.com/services/rest/";
    private final static String[] SORTING_OPTIONS = {"date-posted-asc", "date-posted-desc", "date-taken-asc",
            "date-taken-desc", "interestingness-desc", "interestingness-asc", "relevance"};
    private final static int MAX_ELEMENT_PER_REQUEST = 4000;

    /**
     * Retourne un URL aléatoire à une position donné
     * @param lat La latitude
     * @param lon La longitude
     * @param totalRequestable Le nombre d'éléments récupérable
     * @return Un URL
     */
    public static Photo getRdmPhoto(double lat, double lon, int totalRequestable) {
        PhotoFlicker result = getSearchResult(lon, lat, new Random().nextInt(totalRequestable) + 1).getPhotos().getPhoto().get(0);
        while (result == null || result.getUrl_l() == null || result.getUrl_l().isEmpty()){
            log.info("Appel Flicker : getRdmURL");
            result = getSearchResult(lon, lat, new Random().nextInt(totalRequestable) + 1).getPhotos().getPhoto().get(0);
        }

        return result.getPhoto();
    }

    /**
     * Retourne une liste d'URL Flicker aléatoires à une position donné
     * @param lat La latitude
     * @param lon La longitude
     * @param urlsNumber Le nombre d'URL souhaité
     * @return Une liste d'URL
     */
    public static List<Photo> getRdmPhotos(double lat, double lon, int urlsNumber) {
        int totalRequestable = getTotalRequestable(lat, lon);
        List<Photo> result = Stream.generate(Photo::new).limit(urlsNumber).collect(Collectors.toList());
        result = result.parallelStream().map(e -> getRdmPhoto(lat,lon, totalRequestable))
                .collect(Collectors.toList());

        return result;
    }

    /**
     * Retourne le nombre d'éléments récupérable en prenant en compte les limitations de l'api
     * @param lat La latitude
     * @param lon La longitude
     * @return Un nombre d'éléments récupérable
     */
    public static int getTotalRequestable(double lat, double lon) {
        FlickerResponse response = getSearchResult(lon, lat, 1);
        return Math.min(response.getPhotos().getTotal(), MAX_ELEMENT_PER_REQUEST);
    }

    public static FlickerResponse getSearchResult(double lon, double lat, int page) {
        Client client = ClientBuilder.newClient();
        return client.target(FLICKR_URL)
                .queryParam("method", "flickr.photos.search")
                .queryParam("api_key", API_KEY)
                .queryParam("media", "photos")
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .queryParam("sort", SORTING_OPTIONS[new Random().nextInt(SORTING_OPTIONS.length)]) //Sort Rdm
                .queryParam("extras", "url_l, owner_name")
                .queryParam("per_page", 1)
                .queryParam("page", page)
                .queryParam("format", "json")
                .queryParam("nojsoncallback", 1)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get(FlickerResponse.class);
    }
}
