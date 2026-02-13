package application.api;

import application.service.impl.DriverService;
import application.viewmodel.DriverViewModel;
import application.viewmodel.RaceViewModel;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
public class ApiDriverController {
    private final DriverService driverService;

    public ApiDriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping
    public ResponseEntity<List<DriverViewModel>> showDrivers(HttpSession session) {
        return ResponseEntity.ok(driverService.getAll()
                .stream()
                .map(driverService::mapToViewModel)
                .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverViewModel> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(driverService.mapToViewModel(driverService.getById(id)));
    }

    @GetMapping("/{id}/races")
    public ResponseEntity<List<RaceViewModel>> getRaces(@PathVariable Integer id) {
        return ResponseEntity.ok(driverService.getRacesAsViewModels(id));
    }
}
