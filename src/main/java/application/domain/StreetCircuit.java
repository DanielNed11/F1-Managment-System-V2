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

    @Getter
    @Setter
    private String cityName;

    @Getter
    @Setter
    private Integer daysToSetup;

    @Getter
    @Setter
    private Double annualRentalCost;

    @Getter
    @Setter
    private Boolean hasTemporaryBarriers;

    @Override
    public String toString() {
        return super.toString() + " [Street Circuit in " + cityName + "]";
    }
}
