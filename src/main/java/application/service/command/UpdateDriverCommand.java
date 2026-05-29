package application.service.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDriverCommand {
    private int id;
    private String name;
    private LocalDate dateOfBirth;
    private String nationality;
    private Integer worldChampionships;
    private String imageUrl;
    private Integer teamId;
}