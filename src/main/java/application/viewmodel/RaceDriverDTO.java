package application.viewmodel;

import lombok.Data;

@Data
public class RaceDriverDTO {

    private Integer id;
    private DriverDTO driver;
    private Integer position;
}
