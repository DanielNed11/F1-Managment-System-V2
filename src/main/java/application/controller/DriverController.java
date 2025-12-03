package application.controller;

import application.domain.Driver;
import application.domain.Race;
import application.domain.Team;
import application.service.IDriverService;
import application.service.IRaceService;
import application.service.ITeamService;
import application.session.SessionHistory;
import application.viewmodel.DriverViewModel;
import jakarta.servlet.http.HttpSession;
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
import java.util.stream.Collectors;

@Controller
@RequestMapping("/drivers")
public class DriverController {

    private final IDriverService driverService;
    private final ITeamService teamService;
    private final IRaceService raceService;
    private final SessionHistory sessionHistory;
    private final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    public DriverController(IDriverService driverService, ITeamService teamService, IRaceService raceService, SessionHistory sessionHistory) {
        this.driverService = driverService;
        this.teamService = teamService;
        this.raceService = raceService;
        this.sessionHistory = sessionHistory;
    }

    @GetMapping
    public String showAllDrivers(
            @RequestParam(required = false) String nationality,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dateOfBirth,
            HttpSession session,
            Model model) {

        // Track this page visit in session history
        sessionHistory.addVisit(session, "/drivers", "All Drivers");

        List<Driver> drivers = driverService.filterDrivers(nationality, dateOfBirth);
        List<DriverViewModel> driverViewModels = drivers.stream()
                .map(this::mapToViewModel)
                .collect(Collectors.toList());

        model.addAttribute("nationality", nationality);
        model.addAttribute("dateOfBirth", dateOfBirth);
        model.addAttribute("drivers", driverViewModels);
        log.info("Showing all drivers for session: " + session.getId());
        return "drivers/drivers";
    }

    @GetMapping("/{id}")
    public String getDriverById(@PathVariable int id, HttpSession session, Model model) {
        Driver driver = driverService.getById(id);
        if (driver == null) {
            log.warn("Driver with id " + id + " not found");
            return "redirect:/drivers";
        }

        // Track this page visit in session history
        sessionHistory.addVisit(session, "/drivers/" + id, "Driver Details");

        List<Race> races = raceService.getAll().stream()
                .filter(r -> r.getDrivers().stream().anyMatch(d -> d.getId() == id))
                .collect(Collectors.toList());

        DriverViewModel driverViewModel = mapToViewModel(driver);
        model.addAttribute("driver", driverViewModel);
        model.addAttribute("races", races);
        log.info("Getting driver with id " + id);
        return "drivers/driver";
    }

    @GetMapping("/add")
    public String showAddForm(HttpSession session, Model model) {
        // Track this page visit in session history
        sessionHistory.addVisit(session, "/drivers/add", "Add Driver");

        model.addAttribute("driverViewModel", new DriverViewModel());
        model.addAttribute("teams", teamService.getAll());
        log.info("Showing add driver form");
        return "drivers/add-driver";
    }

    @PostMapping("/add")
    public String addDriver(@Valid @ModelAttribute DriverViewModel driverViewModel,
                           BindingResult bindingResult,
                           HttpSession session,
                           Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("teams", teamService.getAll());
            log.warn("Validation errors when adding driver");
            return "drivers/add-driver";
        }

        Driver driver = mapToDriver(driverViewModel);
        validateDriversTeam(driver);
        driverService.add(driver);
        log.info("Adding driver with id " + driver.getId() + " for session: " + session.getId());
        return "redirect:/drivers";
    }

    @GetMapping("/edit/{id}")
    public String editDriver(@PathVariable int id, HttpSession session, Model model) {
        Driver driver = driverService.getById(id);
        if (driver == null) {
            return "redirect:/drivers";
        }

        // Track this page visit in session history
        sessionHistory.addVisit(session, "/drivers/edit/" + id, "Edit Driver");

        DriverViewModel driverViewModel = mapToViewModel(driver);
        model.addAttribute("driverViewModel", driverViewModel);
        model.addAttribute("teams", teamService.getAll());
        log.info("Show edit form for driver with id " + id);
        return "drivers/edit-driver";
    }

    @PostMapping("/edit/{id}")
    public String updateDriver(@PathVariable int id,
                               @Valid @ModelAttribute DriverViewModel driverViewModel,
                               BindingResult bindingResult,
                               Model model) {
        Driver existingDriver = driverService.getById(id);
        if (existingDriver == null) {
            return "redirect:/drivers";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("teams", teamService.getAll());
            log.warn("Validation errors when updating driver with id " + id);
            return "drivers/edit-driver";
        }

        Driver driver = mapToDriver(driverViewModel);
        validateDriversTeam(driver);
        driver.setId(id);
        driverService.update(driver);
        log.info("Updating driver with id " + id);
        return "redirect:/drivers";
    }

    private void validateDriversTeam(Driver driver) {
        // Validation logic can be added here if needed
        // teamId is already set directly on the driver object
    }

    private DriverViewModel mapToViewModel(Driver driver) {
        DriverViewModel viewModel = new DriverViewModel();
        viewModel.setId(driver.getId());
        viewModel.setName(driver.getName());
        viewModel.setDateOfBirth(driver.getDateOfBirth());
        viewModel.setNationality(driver.getNationality());
        viewModel.setWorldChampionships(driver.getWorldChampionships());

        // Get team object and set both teamId and teamName
        Team team = driver.getTeam();
        if (team != null) {
            viewModel.setTeamId(team.getId());
            viewModel.setTeamName(team.getName());
        } else {
            viewModel.setTeamId(0);
            viewModel.setTeamName(null);
        }

        viewModel.setImageUrl(driver.getImageUrl());
        return viewModel;
    }

    private Driver mapToDriver(DriverViewModel viewModel) {
        Driver driver = new Driver();
        driver.setId(viewModel.getId());
        driver.setName(viewModel.getName());
        driver.setDateOfBirth(viewModel.getDateOfBirth());
        driver.setNationality(viewModel.getNationality());
        driver.setWorldChampionships(viewModel.getWorldChampionships());

        // Set the Team object instead of teamId
        if (viewModel.getTeamId() != 0) {
            Team team = teamService.getById(viewModel.getTeamId());
            driver.setTeam(team);
        }

        driver.setImageUrl(viewModel.getImageUrl());
        return driver;
    }

    @PostMapping("/{id}/delete")
    public String deleteDriver(@PathVariable int id, HttpSession session) {
        driverService.delete(id);
        log.info("Deleted driver with id " + id + " for session: " + session.getId());
        return "redirect:/drivers";
    }
}
