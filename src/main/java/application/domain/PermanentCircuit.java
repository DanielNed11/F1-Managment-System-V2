package application.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("PERMANENT")
@NoArgsConstructor
public class PermanentCircuit extends Track {

    @Getter
    @Setter
    private Boolean hasMuseum;

    @Getter
    @Setter
    private Integer testDaysPerYear;

    @Getter
    @Setter
    private String facilities;

    public PermanentCircuit(String name, String location, double lengthKm, int openedYear,
                            Boolean hasMuseum, Integer testDaysPerYear, String facilities) {
        super(name, location, lengthKm, openedYear);
        this.hasMuseum = hasMuseum;
        this.testDaysPerYear = testDaysPerYear;
        this.facilities = facilities;
    }

    @Override
    public String toString() {
        return super.toString() + " [Permanent Circuit]";
    }
}
