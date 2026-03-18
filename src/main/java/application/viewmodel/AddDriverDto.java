package application.viewmodel;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddDriverDto {

    @NotBlank
    @Size(min = 2, max = 30)
    private String name;
    @NotNull
    @Past
    private LocalDate dateOfBirth;
    @NotBlank
    @Size(min = 2, max = 30)
    private String nationality;
    @Min(0)
    @Max(10)
    private int worldChampionships;
    @NotBlank
    private String imageUrl;

    private Integer teamId = null;
}
