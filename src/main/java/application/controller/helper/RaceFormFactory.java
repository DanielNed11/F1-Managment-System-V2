package application.controller.helper;

import application.controller.viewmodel.AddRaceViewModel;
import application.controller.viewmodel.EditRaceViewModel;
import application.controller.viewmodel.RaceDriverSelectionViewModel;
import application.domain.Race;
import application.domain.RaceDriver;
import application.service.IDriverService;
import application.service.IRaceService;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RaceFormFactory {

    private final IDriverService driverService;
    private final IRaceService raceService;

    public RaceFormFactory(IDriverService driverService, IRaceService raceService) {
        this.driverService = driverService;
        this.raceService = raceService;
    }

    public AddRaceViewModel buildAdd() {
        AddRaceViewModel form = new AddRaceViewModel();

        List<RaceDriverSelectionViewModel> selections = driverService.getAll()
                .stream().map(driver -> {
                    RaceDriverSelectionViewModel selection = new RaceDriverSelectionViewModel();
                    selection.setDriverId(driver.getId());
                    selection.setDriverName(driver.getName());
                    return selection;
                }).toList();

        form.setDriverSelections(selections);
        return form;
    }

    public EditRaceViewModel buildEdit(Integer id) {
        Race existing = raceService.getById(id);

        EditRaceViewModel form = new EditRaceViewModel();

        Map<Integer, Integer> positionByDriverId = new HashMap<>();
        for (RaceDriver raceDriver : existing.getRaceDrivers()) {
            if (raceDriver.getDriver() != null) {
                positionByDriverId.put(raceDriver.getDriver().getId(), raceDriver.getPosition());
            }
        }

        form.setId(existing.getId());
        form.setName(existing.getName());
        form.setDate(existing.getDate());
        form.setTrackId(existing.getTrack() != null ? existing.getTrack().getId() : null);
        form.setWinnerId(existing.getWinner() != null ? existing.getWinner().getId() : null);

        List<RaceDriverSelectionViewModel> selections = driverService.getAll().stream()
                .map(driver -> {
                    RaceDriverSelectionViewModel selection = new RaceDriverSelectionViewModel();
                    selection.setDriverId(driver.getId());
                    selection.setDriverName(driver.getName());
                    selection.setParticipated(positionByDriverId.containsKey(driver.getId()));
                    selection.setPosition(positionByDriverId.get(driver.getId()));
                    return selection;
                })
                .toList();
        form.setDriverSelections(selections);
        return form;
    }
}
