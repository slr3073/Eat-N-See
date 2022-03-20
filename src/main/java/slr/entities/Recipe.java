package slr.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Recipe {
    private String area;
    private String name;
    private String instructions;
    private String image_url;
    private String themealdb_url;
    private String source_url;
}
