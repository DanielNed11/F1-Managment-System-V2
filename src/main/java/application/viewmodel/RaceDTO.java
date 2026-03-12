package application.viewmodel;

import application.domain.Track;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RaceDTO {
    private Integer id;

    @NotBlank(message = "{validation.race.name.notblank}")
    @Size(min = 2, max = 100, message = "{validation.race.name.size}")
    private String name;

    @NotNull(message = "{validation.race.date.notnull}")
    private LocalDate date;

    private Integer trackId;
    private Track track;

    private String trackName;

    private Integer winnerId;
    private String winnerName;
    private DriverDTO winner;
    private List<RaceDriverDTO> raceDrivers;

    private boolean hasEnded;
}
