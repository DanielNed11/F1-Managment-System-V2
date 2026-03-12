package application.viewmodel;

import application.service.impl.DriverService;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record AddDriverDto(
        @NotBlank @Size(min=2, max=30) String name,
        @NotNull @Past LocalDate dateOfBirth,
        @NotBlank @Size(min=2, max=30) String nationality,
        @Min(0) @Max(10) int worldChampionships,
        @NotBlank String imageUrl
) {

    public DriverService.AddDriverDtoService mapToServiceDto(AddDriverDto addDriverDto) {
        return new DriverService.AddDriverDtoService(addDriverDto.name(),
                addDriverDto.dateOfBirth,
                addDriverDto.nationality,
                addDriverDto.worldChampionships,
                addDriverDto.imageUrl);
    }
}
