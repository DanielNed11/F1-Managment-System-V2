package application.api.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamDTO {
    private Integer id;


    private String name;


    private int foundedYear;

    private String league;

    @Pattern(regexp = "^(https?://.*|/img/.*)$", message = "{validation.team.teamLogoUrl.pattern}")
    private String teamLogoUrl;

    @DecimalMin(value = "0.0", message = "{validation.team.budgetInMillions.min}")
    @DecimalMax(value = "1000.0", message = "{validation.team.budgetInMillions.max}")
    private double budgetInMillions;

    private List<String> driverNames;
}
