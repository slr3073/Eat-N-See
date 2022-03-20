package slr.api;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IPInfo {
    private final static String IP_INFO_URL = "https://ipinfo.io/";

    /**
     * Donne l'adresse IPv4 de l'appelant
     * @return Une adresse IPv4
     */
    public static String getIP() {
        log.info("Appel API : IPinfo");

        Client client = ClientBuilder.newClient();

        return client.target(IP_INFO_URL)
                .path("ip")
                .request()
                .accept(MediaType.TEXT_PLAIN)
                .get(String.class);
    }
}
