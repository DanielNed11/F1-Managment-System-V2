package application.controller.viewmodel;

import lombok.Data;

@Data
public class RaceDriverSelectionViewModel {

    private Integer driverId;
    private String driverName;
    private boolean participated;
    private Integer position;
}
