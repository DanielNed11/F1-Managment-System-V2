package application.viewmodel;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record PatchDriverDTO(
        @Size(min=2, max=30) String name,
        @Past LocalDate dateOfBirth,
        @Size(min=2, max=30) String nationality,
        @Min(0) @Max(10) Integer worldChampionships,
        String imageUrl
) {
}
