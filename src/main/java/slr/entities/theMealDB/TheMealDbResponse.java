package slr.entities.theMealDB;

import lombok.Data;

import java.util.List;

@Data
public class TheMealDbResponse {
    private List<Meal> meals;
}
