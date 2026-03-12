package application.controller;

import application.service.IDriverService;
import application.service.IRaceService;
import application.service.ITeamService;
import application.viewmodel.DriverDTO;
import application.viewmodel.RaceDTO;
import jakarta.validation.Valid;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

    @Autowired
    public DriverController(IDriverService driverService, ITeamService teamService, IRaceService raceService) {
        this.driverService = driverService;
        this.teamService = teamService;
        this.raceService = raceService;
    }

    @GetMapping({"/drivers", "/"})
    public String showAllDrivers(
            @RequestParam(required = false) String nationality,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dateOfBirth,
            Model model) {

        List<DriverDTO> driverDTOS = driverService.filterDrivers(nationality, dateOfBirth);

        model.addAttribute("nationality", nationality);
        model.addAttribute("dateOfBirth", dateOfBirth);
        model.addAttribute("drivers", driverDTOS);
        return "drivers/drivers";
    }

    @GetMapping("/drivers/{id}")
    public String getDriverById(@PathVariable Integer id, Model model) {
        DriverDTO driverDto = driverService.getById(id);
        if (driverDto == null) {
            log.warn("Driver with id " + id + " not found");
            return "redirect:/drivers";
        }

        List<RaceDTO> races = raceService.findRacesByDriverId(id);

        model.addAttribute("driver", driverDto);
        model.addAttribute("races", races);
        return "drivers/driver";
    }

    @GetMapping("/drivers/add")
    public String showAddForm(Model model) {

        model.addAttribute("driverDTO", new DriverDTO());
        model.addAttribute("teams", teamService.getAll());
        return "drivers/add-driver";
    }

    @PostMapping("/drivers/add")
    public String addDriver(@Valid @ModelAttribute DriverDTO driverDto,
                            BindingResult bindingResult,
                            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("teams", teamService.getAll());
            return "drivers/add-driver";
        }

        driverService.add(driverDto);
        log.info("Adding driver with id " + driverDto.getId());
        return "redirect:/drivers";
    }

    @GetMapping("/drivers/edit/{id}")
    public String editDriver(@PathVariable Integer id, Model model) {

        DriverDTO driverDto = driverService.getById(id);
        model.addAttribute("driverDTO", driverDto);
        model.addAttribute("teams", teamService.getAll());
        return "drivers/edit-driver";
    }

    @PostMapping("/drivers/edit/{id}")
    public String updateDriver(@PathVariable Integer id,
                               @Valid @ModelAttribute DriverDTO driverDto,
                               BindingResult bindingResult,
                               Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("teams", teamService.getAll());
            log.warn("Validation errors when updating driver with id " + id);
            return "drivers/edit-driver";
        }

        driverService.update(driverDto);
        log.info("Updating driver with id " + id);
        return "redirect:/drivers";
    }

    @PostMapping("/drivers/{id}/delete")
    public String deleteDriver(@PathVariable Integer id) {
        driverService.delete(id);
        log.info("Deleted driver with id " + id);
        return "redirect:/drivers";
    }

    @GetMapping("/drivers/champions")
    public String showChampions(Model model) {

        List<DriverDTO> champions = driverService.findChampions();

        model.addAttribute("drivers", champions);
        log.info("Showing champion drivers");
        return "drivers/champions";
    }

}
