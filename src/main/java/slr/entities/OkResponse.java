package slr.entities;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OkResponse {
    private boolean success;
    private String client_ip;
    private String country_alpha2;
    private boolean random_area;
    private Recipe recipe;
    private List<Photo> photos;
}
