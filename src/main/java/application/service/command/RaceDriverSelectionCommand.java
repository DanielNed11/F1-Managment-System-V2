package application.service.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RaceDriverSelectionCommand {
    private Integer driverId;
    private String driverName;
    private boolean participated;
    private Integer position;
}
