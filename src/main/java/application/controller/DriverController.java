package application.controller;

import application.domain.Driver;
import application.service.IDriverService;
import application.service.ITeamService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/drivers")
public class DriverController {

    private final IDriverService driverService;
    private final ITeamService teamService;
    private final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    public DriverController(IDriverService driverService, ITeamService teamService) {
        this.driverService = driverService;
        this.teamService = teamService;
    }

    @GetMapping
    public String showAllDrivers(
            @RequestParam(required = false) String nationality,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dateOfBirth,
            Model model) {

        model.addAttribute("nationality", nationality);
        model.addAttribute("dateOfBirth", dateOfBirth);
        model.addAttribute("drivers", driverService.filterDrivers(nationality, dateOfBirth));
        log.info("Showing all drivers");
        return "drivers/drivers";
    }

    @GetMapping("/{id}")
    public String getDriverById(@PathVariable int id, Model model) {
        Driver driver = driverService.getById(id);
        if (driver == null) {
            log.warn("Driver with id " + id + " not found");
            return "redirect:/drivers";
        }
        model.addAttribute("driver", driver);
        log.info("Getting driver with id " + id);
        return "drivers/driver";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("driver", new Driver());
        model.addAttribute("teams", teamService.getAll());
        log.info("Showing all drivers");
        return "drivers/add-driver";
    }

    @PostMapping("/add")
    public String addDriver(@ModelAttribute Driver driver) {
        validateDriversTeam(driver);
        driverService.add(driver);
        log.info("Adding driver with id " + driver.getId());
        return "redirect:/drivers";
    }

    @GetMapping("/edit/{id}")
    public String editDriver(@PathVariable int id, Model model) {
        Driver driver = driverService.getById(id);
        if (driver == null) {
            return "redirect:/drivers";
        }
        model.addAttribute("driver", driver);
        model.addAttribute("teams", teamService.getAll());
        log.info("Show edit form drivers");
        return "drivers/edit-driver";
    }

    @PostMapping("/edit/{id}")
    public String updateDriver(@PathVariable int id, @ModelAttribute Driver driver) {
        Driver existingDriver = driverService.getById(id);
        if (existingDriver == null) {
            return "redirect:/drivers";
        }

        validateDriversTeam(driver);
        driver.setId(id);
        driverService.update(driver);
        log.info("Updating driver with id " + id);
        return "redirect:/drivers";
    }

    private void validateDriversTeam(Driver driver) {
        if (driver.getTeamId() != 0) {
            driver.setTeam(teamService.getById(driver.getTeamId()));
        } else {
            driver.setTeam(null);
        }
    }
}
