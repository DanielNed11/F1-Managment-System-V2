package application.controller;

import application.domain.Driver;
import application.mapper.DriverMapper;
import application.security.CustomUser;
import application.service.IDriverService;
import application.service.IRaceService;
import application.service.ITeamService;
import application.viewmodel.AddDriverDto;
import application.viewmodel.DriverDTO;
import application.viewmodel.PatchDriverDTO;
import application.viewmodel.RaceDTO;
import jakarta.validation.Valid;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

// TODO Handle Exceptions
@Controller
public class DriverController {

    private final IDriverService driverService;
    private final ITeamService teamService;
    private final IRaceService raceService;
    private final Log log = LogFactory.getLog(this.getClass());
    private final DriverMapper driverMapper;

    @Autowired
    public DriverController(IDriverService driverService, ITeamService teamService, IRaceService raceService, DriverMapper driverMapper) {
        this.driverService = driverService;
        this.teamService = teamService;
        this.raceService = raceService;
        this.driverMapper = driverMapper;
    }

    @GetMapping({"/drivers", "/"})
    public String showAllDrivers(
            @RequestParam(required = false) String nationality,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dateOfBirth,
            @AuthenticationPrincipal CustomUser customUser,
            Model model) {

        List<Driver> drivers = driverService.filterDrivers(nationality, dateOfBirth);

        List<DriverDTO> driverDTOList = drivers.stream().map(driver -> {

            boolean canModifyDriver = customUser != null &&
                    customUser.getAppUserId() != null &&
                    driverService.canModifyDriver(driver.getId(), customUser.getAppUserId());
            DriverDTO driverDTO = driverMapper.toDriverDTO(driver);
            driverDTO.setModifiable(canModifyDriver);
            return driverDTO;
        }).toList();

        boolean isAdmin = customUser != null &&
                customUser.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("nationality", nationality);
        model.addAttribute("dateOfBirth", dateOfBirth);
        model.addAttribute("drivers", driverDTOList);
        return "drivers/drivers";
    }

    @GetMapping("/drivers/{id}")
    public String getDriverById(@PathVariable Integer id, Model model, @AuthenticationPrincipal CustomUser customUser) {
        Driver driver = driverService.getById(id);
        if (driver == null) {
            log.warn("Driver with id " + id + " not found");
            return "redirect:/drivers";
        }

        List<RaceDTO> races = raceService.findRacesByDriverId(id);

        DriverDTO driverDTO = driverMapper.toDriverDTO(driver);
        driverDTO.setModifiable(
                customUser != null
                        && customUser.getAppUserId() != null
                        && driverService.canModifyDriver(driver.getId(), customUser.getAppUserId())
        );
        model.addAttribute("driver", driverDTO);
        model.addAttribute("races", races);
        return "drivers/driver";
    }

    @GetMapping("/drivers/add")
    public String showAddForm(Model model) {

        model.addAttribute("driverDTO", new AddDriverDto());
        model.addAttribute("teams", teamService.getAll());
        return "drivers/add-driver";
    }

    @PostMapping("/drivers/add")
    public String addDriver(@Valid @ModelAttribute AddDriverDto driverDto,
                            BindingResult bindingResult,
                            @AuthenticationPrincipal CustomUser customUser,
                            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("teams", teamService.getAll());
            return "drivers/add-driver";
        }

        Driver driver = driverService.add(driverDto, customUser.getAppUserId());
        log.info("Adding driver with id " + driver.getId());
        return "redirect:/drivers";
    }

    @GetMapping("/drivers/edit/{id}")
    public String editDriver(@PathVariable Integer id, Model model) {

        Driver driver = driverService.getById(id);
        if (driver == null) {
            log.warn("Driver with id " + id + " not found");
            return "redirect:/drivers";
        }

        model.addAttribute("driverDTO",driverMapper.toPatchDriverDTO(driver));
        model.addAttribute("driverId", id);
        model.addAttribute("teams", teamService.getAll());
        return "drivers/edit-driver";
    }

    @PostMapping("/drivers/edit/{id}")
    public String updateDriver(@PathVariable Integer id,
                               @Valid @ModelAttribute PatchDriverDTO patchDriverDTO,
                               @AuthenticationPrincipal CustomUser customUser,
                               BindingResult bindingResult,
                               Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("driverId", id);
            model.addAttribute("teams", teamService.getAll());
            log.warn("Validation errors when updating driver with id " + id);
            return "drivers/edit-driver";
        }

        driverService.update(id, patchDriverDTO, customUser.getAppUserId());
        log.info("Updating driver with id " + id);
        return "redirect:/drivers";
    }

    @PostMapping("/drivers/{id}/delete")
    public String deleteDriver(@PathVariable Integer id, @AuthenticationPrincipal CustomUser customUser) {
        driverService.delete(id, customUser.getAppUserId());
        log.info("Deleted driver with id " + id);
        return "redirect:/drivers";
    }

    @GetMapping("/drivers/champions")
    public String showChampions(Model model) {

        List<Driver> champions = driverService.findChampions();


        model.addAttribute("drivers", driverMapper.toDriverDTOList(champions));
        log.info("Showing champion drivers");
        return "drivers/champions";
    }

}
