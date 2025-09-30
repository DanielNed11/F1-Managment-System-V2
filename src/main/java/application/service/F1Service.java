package application.service;

import application.domain.Driver;
import application.domain.F1Team;
import application.repository.DataFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class F1Service {

    private static List<F1Team> f1Teams = new ArrayList<>();

    static {
        DataFactory.drivers.stream()
                .distinct().map(Driver::getTeam)
                .forEach(team -> {
                    f1Teams.add(team);
                });
    }

    public List<F1Team> getF1Teams() {
        return f1Teams;
    }

}
