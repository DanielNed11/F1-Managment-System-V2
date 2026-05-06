package application.api;

import application.api.dto.AddDriverDTO;
import application.api.dto.DriverDTO;
import application.api.dto.PatchDriverDTO;
import application.api.dto.RaceDTO;
import application.domain.Driver;
import application.mapper.DriverMapper;
import application.mapper.RaceMapper;
import application.security.CustomUser;
import application.service.IDriverService;
import application.service.command.UpdateDriverCommand;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


// TODO handle exception
@RestController
@RequestMapping("/api/drivers")
public class ApiDriverController {
    private final IDriverService driverService;
    private final DriverMapper driverMapper;
    private final RaceMapper raceMapper;

    public ApiDriverController(IDriverService driverService, DriverMapper driverMapper, RaceMapper raceMapper) {
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

        if (driver == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(driver);
    }

    @GetMapping("/{id}/races")
    public ResponseEntity<List<RaceDTO>> getRaces(@PathVariable Integer id) {
        Driver driver = driverService.getById(id);

        if (driver == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(raceMapper.toRaceDTOs(driverService.getRacesByDriver(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id, @AuthenticationPrincipal CustomUser customUser) {
        DriverDTO driver = driverMapper.toDriverDTO(driverService.getById(id));

        if (driver == null) return ResponseEntity.notFound().build();

        driverService.delete(id, customUser.getAppUserId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<DriverDTO> addDriver(@Valid @RequestBody
                                                   AddDriverDTO addDriverDTO,
                                               @AuthenticationPrincipal
                                               CustomUser customUser) {

        Driver driver = driverMapper.toDriver(addDriverDTO);
        driverService.add(driver, customUser.getAppUserId());

        return ResponseEntity.status(201).body(driverMapper.toDriverDTO(driver));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DriverDTO> updateDriver(@PathVariable Integer id,
                                                        @Valid @RequestBody PatchDriverDTO patchDriverDTO,
                                                        @AuthenticationPrincipal CustomUser customUser) {

        if (driverService.getById(id) == null) return ResponseEntity.notFound().build();

        UpdateDriverCommand command = driverMapper.toUpdateDriverCommand(patchDriverDTO);

        command.setId(id);

        Driver updated = driverService.update(command, customUser.getAppUserId());

        return ResponseEntity.ok(driverMapper.toDriverDTO(updated));
    }
}
