package application.api;

import application.domain.Driver;
import application.service.impl.DriverService;
import application.viewmodel.DriverViewModel;
import application.viewmodel.RaceViewModel;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        Driver driver = driverService.getById(id);
        if (driver == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(driverService.mapToViewModel(driver));
    }

    @GetMapping("/{id}/races")
    public ResponseEntity<List<RaceViewModel>> getRaces(@PathVariable Integer id) {
        Driver driver = driverService.getById(id);
        if (driver == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(driverService.getRacesAsViewModels(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        Driver driver = driverService.getById(id);
        if (driver == null) {
            return ResponseEntity.notFound().build();
        }
        driverService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
