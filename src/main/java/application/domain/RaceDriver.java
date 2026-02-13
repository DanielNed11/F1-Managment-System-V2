package application.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "race_drivers")
@NoArgsConstructor
public class RaceDriver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "race_id")
    private Race race;

    @Getter
    @Setter
    private Integer position;

    public RaceDriver(Driver driver, Race race, Integer position) {
        this.driver = driver;
        this.race = race;
        this.position = position;
    }

    public RaceDriver(Driver driver, Race race) {
        this.driver = driver;
        this.race = race;
        this.position = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RaceDriver that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "DriverRace{" +
                "id=" + id +
                ", driver=" + (driver != null ? driver.getName() : "null") +
                ", race=" + (race != null ? race.getName() : "null") +
                '}';
    }
}
