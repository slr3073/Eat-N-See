package slr.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test API Country is")
public class CountryIsTest {
    @Test
    @DisplayName("Récupération code OK")
    void responseIsCorrectTest(){
        //given
        //when
        String actualCode = CountryIs.getCountryCode("109.220.157.103");

        //then
        assertEquals("FR", actualCode);
    }

    @Test
    @DisplayName("Récupération code avec IP localhost")
    void localhostHandled(){
        //given
        //when
        String actualCode = CountryIs.getCountryCode("127.0.0.1");

        //then
        assertEquals("FR", actualCode);
    }
}
