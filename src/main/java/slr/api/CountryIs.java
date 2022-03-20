package slr.api;

import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CountryIs {
    private final static String COUNTRY_IS_URL = "https://api.country.is/";
    private final static String LOCALHOST_IP = "127.0.0.1";

    /**
     * Transforme une adresse IP en code pays ISO 3166-1 alpha-2
     * @param ipAddress Une adresse IPv4
     * @return Un code pays ISO 3166-1 alpha-2
     */
    public static String getCountryCode(String ipAddress) {
        if (ipAddress.equals(LOCALHOST_IP)) return "FR";

        log.info("Appel API CountryIS : getCountryCode");

        Client client = ClientBuilder.newClient();
        JsonObject response = client.target(COUNTRY_IS_URL)
                .path(ipAddress)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get(JsonObject.class);

        return response.getString("country");
    }
}
