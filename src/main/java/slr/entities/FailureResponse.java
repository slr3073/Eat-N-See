package slr.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FailureResponse {
    private boolean success;
    private String api_failed;
    private String api_status;
}
