package application.controller.viewmodel;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class EditRaceViewModel {

    private Integer id;
    private String name;
    private LocalDate date;
    private Integer trackId;
    private Integer winnerId;
    private List<RaceDriverSelectionViewModel> driverSelections = new ArrayList<>();
}
