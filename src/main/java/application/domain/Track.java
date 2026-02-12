package application.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "tracks")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "track_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("BASIC")
@NoArgsConstructor
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Integer id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String location;
    @Getter
    @Setter
    private double lengthKm;
    @Getter
    @Setter
    private int openedYear;

    public Track(String name, String location, double lengthKm, int openedYear) {
        this.name = name;
        this.location = location;
        this.lengthKm = lengthKm;
        this.openedYear = openedYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Track track)) return false;
        return Objects.equals(id, track.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name + " (" + location + ", " + lengthKm + " km)";
    }
}
