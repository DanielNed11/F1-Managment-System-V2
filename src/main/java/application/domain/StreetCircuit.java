package application.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("STREET")
@NoArgsConstructor
public class StreetCircuit extends Track {

    @Getter @Setter
    private String cityName;

    @Getter @Setter
    private Integer daysToSetup;

    @Getter @Setter
    private Double annualRentalCost;

    @Getter @Setter
    private Boolean hasTemporaryBarriers;

    public StreetCircuit(String name, String location, double lengthKm, int openedYear,
                         String cityName, Integer daysToSetup, Double annualRentalCost, Boolean hasTemporaryBarriers) {
        super(name, location, lengthKm, openedYear);
        this.cityName = cityName;
        this.daysToSetup = daysToSetup;
        this.annualRentalCost = annualRentalCost;
        this.hasTemporaryBarriers = hasTemporaryBarriers;
    }

    @Override
    public String toString() {
        return super.toString() + " [Street Circuit in " + cityName + "]";
    }
}
