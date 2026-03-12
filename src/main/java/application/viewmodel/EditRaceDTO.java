package application.viewmodel;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class EditRaceDTO {

    private Integer id;
    private String name;
    private LocalDate date;
    private Integer trackId;
    private Integer winnerId;
    private List<RaceDriverSelectionDTO> driverSelections = new ArrayList<>();
}
