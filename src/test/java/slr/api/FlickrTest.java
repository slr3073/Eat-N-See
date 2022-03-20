package slr.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import slr.entities.Flickr.PhotoFlicker;
import slr.entities.Photo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test API Flickr")
class FlickrTest {
    private final static int MAX_ELEMENT_PER_REQUEST = 4000;

    @Nested
    @DisplayName("Récupération de lien d'image direct")
    class URLDirectTest {

        @Test
        @DisplayName("Lien aléatoire OK")
        void getRdmPhoto() {
            //given params
            //when
            Photo actualPhoto = Flickr.getRdmPhoto(48.87d, 2.33d, MAX_ELEMENT_PER_REQUEST);

            //then
            assertNotNull(actualPhoto);
        }

        @Test
        @DisplayName("25 photos aléatoires OK")
        void get25RdmURLs() {
            //given
            //when
            List<Photo> actualPhotos = Flickr.getRdmPhotos(48.87d, 2.33d, 25);

            //then
            assertNotNull(actualPhotos);
            assertEquals(25, actualPhotos.size());
        }
    }

    @Test
    @DisplayName("Récupération du nombre d'image récupérable")
    void getTotalRequestable() {
        //given
        //when
        int actualTotal = Flickr.getTotalRequestable(48.87d, 2.33d);

        //then
        assertEquals(true, actualTotal >= 0 && actualTotal <= MAX_ELEMENT_PER_REQUEST);
    }

}