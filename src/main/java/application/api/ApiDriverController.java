package application.api;

import application.service.impl.DriverService;
import application.viewmodel.AddDriverDto;
import application.viewmodel.DriverDTO;
import application.viewmodel.PatchDriverDTO;
import application.viewmodel.RaceDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


// TODO handle exception
@RestController
@RequestMapping("/api/drivers")
public class ApiDriverController {
    private final DriverService driverService;

    public ApiDriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping
    public ResponseEntity<List<DriverDTO>> showDrivers() {
        return ResponseEntity.ok(driverService.getAll()
                .stream()
                .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverDTO> getById(@PathVariable Integer id) {
        DriverDTO driver = driverService.getById(id);
        if (driver == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(driverService.getById(id));
    }

    @GetMapping("/{id}/races")
    public ResponseEntity<List<RaceDTO>> getRaces(@PathVariable Integer id) {
        DriverDTO driver = driverService.getById(id);
        if (driver == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(driverService.getRacesByDriver(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        DriverDTO driver = driverService.getById(id);
        if (driver == null) {
            return ResponseEntity.notFound().build();
        }
        driverService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<DriverDTO> addDriver(@Valid @RequestBody AddDriverDto addDriverDto) {
        DriverDTO driverDTO = driverService.add(addDriverDto.mapToServiceDto(addDriverDto));
        return ResponseEntity.status(201).body(driverDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DriverDTO> updateDriver(@PathVariable Integer id, @Valid @RequestBody PatchDriverDTO patchDriverDto) {
        DriverDTO driver = driverService.getById(id);
        if (driver == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(driverService.update(id, patchDriverDto));
    }
}
