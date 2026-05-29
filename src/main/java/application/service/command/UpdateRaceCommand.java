package application.service.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRaceCommand {
    private int id;
    private String name;
    private LocalDate date;
    private Integer trackId;
    private Integer winnerId;
    private List<RaceDriverSelectionCommand> driverSelections = new ArrayList<>();
}
