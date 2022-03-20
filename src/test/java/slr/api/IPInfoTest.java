package slr.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test API IP info")
class IPInfoTest {

    @Test
    @DisplayName("Récupération de l'IP OK")
    void getIP() {
        //given
        //when
        String actualIP = IPInfo.getIP();

        //then
        assertNotNull(actualIP);
        assertNotEquals("", actualIP);
    }
}