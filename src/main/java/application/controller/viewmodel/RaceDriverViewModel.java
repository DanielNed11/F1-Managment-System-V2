package application.controller.viewmodel;

import lombok.Data;

@Data
public class RaceDriverViewModel {

    private Integer id;
    private DriverViewModel driver;
    private Integer position;
}
