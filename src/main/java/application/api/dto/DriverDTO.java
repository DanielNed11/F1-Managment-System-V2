package application.api.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverDTO {
    private Integer id;

    @NotBlank(message = "{validation.driver.name.notblank}")
    @Size(min = 2, max = 100, message = "{validation.driver.name.size}")
    private String name;

    @NotNull(message = "{validation.driver.dateOfBirth.notnull}")
    @Past(message = "{validation.driver.dateOfBirth.past}")
    private LocalDate dateOfBirth;

    @NotBlank(message = "{validation.driver.nationality.notblank}")
    @Size(min = 2, max = 50, message = "{validation.driver.nationality.size}")
    private String nationality;

    @Min(value = 0, message = "{validation.driver.worldChampionships.min}")
    @Max(value = 10, message = "{validation.driver.worldChampionships.max}")
    private int worldChampionships;

    private SimpleTeamDTO simpleTeamDTO;

    @Pattern(regexp = "^(https?://.*|/img/.*)$", message = "{validation.driver.imageUrl.pattern}")
    private String imageUrl;

}
