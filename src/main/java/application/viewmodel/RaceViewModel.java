package application.viewmodel;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RaceViewModel {
    private int id;

    @NotBlank(message = "{validation.race.name.notblank}")
    @Size(min = 2, max = 100, message = "{validation.race.name.size}")
    private String name;

    @NotNull(message = "{validation.race.date.notnull}")
    private LocalDate date;

    @NotNull(message = "{validation.race.trackId.notnull}")
    private Integer trackId;

    private String trackName;

    private Integer winnerId;

    private String winnerName;

    private boolean hasEnded;
}