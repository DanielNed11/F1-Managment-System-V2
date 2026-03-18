package application.viewmodel;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchDriverDTO {

    private Integer id;
    @Size(min = 2, max = 30)
    private String name;
    @Past
    private LocalDate dateOfBirth;
    @Size(min = 2, max = 30)
    private String nationality;
    @Min(0)
    @Max(10)
    private Integer worldChampionships;
    private String imageUrl;
    private Integer teamId;
}
