package application.api.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddTeamDTO {
    @NotBlank(message = "{validation.team.name.notblank}")
    @Size(min = 2, max = 100, message = "{validation.team.name.size}")
    private String name;
    @Min(value = 1900, message = "{validation.team.foundedYear.min}")
    @Max(value = 2025, message = "{validation.team.foundedYear.max}")
    private int foundedYear;
    @NotBlank(message = "{validation.team.league.notblank}")
    private String league;
    @DecimalMin(value = "0.0", message = "{validation.team.budgetInMillions.min}")
    @DecimalMax(value = "1000.0", message = "{validation.team.budgetInMillions.max}")
    private double budgetInMillions;
}
