package application.api;

import application.domain.Driver;
import application.mapper.DriverMapper;
import application.mapper.RaceMapper;
import application.security.CustomUser;
import application.service.impl.DriverService;
import application.viewmodel.AddDriverDto;
import application.viewmodel.DriverDTO;
import application.viewmodel.PatchDriverDTO;
import application.viewmodel.RaceDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


// TODO handle exception
@RestController
@RequestMapping("/api/drivers")
public class ApiDriverController {
    private final DriverService driverService;
    private final DriverMapper driverMapper;
    private final RaceMapper raceMapper;

    public ApiDriverController(DriverService driverService, DriverMapper driverMapper, RaceMapper raceMapper) {
        this.driverService = driverService;
        this.driverMapper = driverMapper;
        this.raceMapper = raceMapper;
    }

    @GetMapping
    public ResponseEntity<List<DriverDTO>> showDrivers() {
        return ResponseEntity.ok
                (driverMapper.toDriverDTOList(driverService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverDTO> getById(@PathVariable Integer id) {
        DriverDTO driver = driverMapper.toDriverDTO(driverService.getById(id));
        if (driver == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(driver);
    }

    @GetMapping("/{id}/races")
    public ResponseEntity<List<RaceDTO>> getRaces(@PathVariable Integer id) {
        DriverDTO driver = driverMapper.toDriverDTO(driverService.getById(id));
        if (driver == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(raceMapper.toRaceDTOList(driverService.getRacesByDriver(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id, @AuthenticationPrincipal CustomUser customUser) {
        DriverDTO driver = driverMapper.toDriverDTO(driverService.getById(id));
        if (driver == null) {
            return ResponseEntity.notFound().build();
        }
        driverService.delete(id, customUser.getAppUserId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<DriverDTO> addDriver(@Valid @RequestBody AddDriverDto addDriverDto, @AuthenticationPrincipal CustomUser customUser) {
        Driver driver = driverService.add(addDriverDto, customUser.getAppUserId());
        return ResponseEntity.status(201).body(driverMapper.toDriverDTO(driver));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DriverDTO> updateDriver(@PathVariable Integer id,
                                                  @Valid @RequestBody PatchDriverDTO patchDriverDto,
                                                  @AuthenticationPrincipal CustomUser customUser) {
        DriverDTO driverDTO = driverMapper.toDriverDTO(driverService.getById(id));

        if (driverDTO == null) return ResponseEntity.notFound().build();

        Driver driver = driverService.update(id, patchDriverDto, customUser.getAppUserId());

        return ResponseEntity.ok(driverMapper.toDriverDTO(driver));
    }
}
