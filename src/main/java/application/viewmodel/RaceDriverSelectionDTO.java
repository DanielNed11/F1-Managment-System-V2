package application.viewmodel;

import lombok.Data;

@Data
public class RaceDriverSelectionDTO {

    private Integer driverId;
    private String driverName;
    private boolean participated;
    private Integer position;
}
