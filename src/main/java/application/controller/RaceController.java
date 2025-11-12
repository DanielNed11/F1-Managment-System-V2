package application.controller;

import application.domain.Driver;
import application.domain.Race;
import application.domain.Track;
import application.service.IDriverService;
import application.service.IRaceService;
import application.service.ITrackService;
import application.session.SessionHistory;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/races")
public class RaceController {

    private final IRaceService raceService;
    private final IDriverService driverService;
    private final ITrackService trackService;
    private final SessionHistory sessionHistory;
    private final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    public RaceController(IRaceService raceService, IDriverService driverService, ITrackService trackService, SessionHistory sessionHistory) {
        this.raceService = raceService;
        this.driverService = driverService;
        this.trackService = trackService;
        this.sessionHistory = sessionHistory;
    }


    @GetMapping
    public String showAllRaces(HttpSession session, Model model) {
        sessionHistory.addVisit(session, "/races", "All Races");

        model.addAttribute("races", raceService.getAll());
        log.info("Showing all races");
        return "races/races";
    }

    @GetMapping("/{id}")
    public String showRace(@PathVariable int id, HttpSession session, Model model) {
        Race race = raceService.getById(id);
        if (race == null) {
            return "redirect:/races";
        }

        sessionHistory.addVisit(session, "/races/" + id, "Race Details");

        model.addAttribute("race", race);
        log.info("Showing race with id " + id);
        return "races/races";
    }

    @GetMapping("/add")
    public String showAddRaceForm(HttpSession session, Model model) {
        sessionHistory.addVisit(session, "/races/add", "Add Race");

        model.addAttribute("tracks", trackService.getAll());
        model.addAttribute("drivers", driverService.getAll());
        log.info("Show Add Race Form");
        return "races/add-race";
    }

    @PostMapping("/add")
    public String addRace(@RequestParam String name,
                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                          @RequestParam int trackId,
                          @RequestParam(required = false) int[] driverIds) {

        Race race = new Race();
        race.setName(name);
        race.setDate(date);
        race.setTrack(trackService.getById(trackId));

        validateDriverIds(driverIds, race);

        raceService.add(race);
        log.info("Added race with id " + race.getId());
        return "redirect:/races";
    }

    private void validateDriverIds(@RequestParam(required = false) int[] driverIds, Race race) {
        List<Driver> selectedDrivers = new ArrayList<>();
        if (driverIds != null) {
            for (int driverId : driverIds) {
                Driver d = driverService.getById(driverId);
                if (d != null) selectedDrivers.add(d);
            }
        }
        selectedDrivers.forEach(race::addDriver);
    }


    @GetMapping("/edit/{id}")
    public String showEditRaceForm(@PathVariable int id, HttpSession session, Model model) {
        Race race = raceService.getById(id);
        if (race == null) {
            return "redirect:/races";
        }

        sessionHistory.addVisit(session, "/races/edit/" + id, "Edit Race");

        model.addAttribute("race", race);
        model.addAttribute("tracks", trackService.getAll());
        model.addAttribute("drivers", driverService.getAll());
        model.addAttribute("newDrivers", new ArrayList<Driver>());
        model.addAttribute("newTrack", new Track());
        log.info("Show Edit Race Form");
        return "races/edit-race";
    }

    @PostMapping("/edit/{id}")
    public String updateRace(@PathVariable int id,
                             @ModelAttribute Race updatedRace,
                             @RequestParam(required = false) int[] driverIds) {

        Race existingRace = raceService.getById(id);
        if (existingRace == null) {
            return "redirect:/races";
        }

        if (updatedRace.getTrack() != null && updatedRace.getTrack().getId() != 0) {
            existingRace.setTrack(trackService.getById(updatedRace.getTrack().getId()));
        } else {
            existingRace.setTrack(null);
        }

        validateDriverIds(driverIds, existingRace);

        raceService.update(existingRace);
        log.info("Updated Race with id " + id);
        return "redirect:/races";
    }


    @PostMapping("/{raceId}/remove-driver/{driverId}")
    public String removeDriverPost(@PathVariable int raceId, @PathVariable int driverId) {
        Race race = raceService.getById(raceId);
        Driver driver = driverService.getById(driverId);

        if (race == null || driver == null) {
            return "redirect:/races";
        }

        race.removeDriver(driver);
        raceService.update(race);
        log.info("Removed Driver with id " + driverId);
        return "redirect:/races/edit/" + raceId;
    }


    @GetMapping("/{raceId}/add-driver/{driverId}")
    public String addDriverToRace(@PathVariable int raceId, @PathVariable int driverId, HttpSession session) {
        Race race = raceService.getById(raceId);
        Driver driver = driverService.getById(driverId);

        if (race == null || driver == null) {
            return "redirect:/races";
        }

        race.addDriver(driver);

        raceService.update(race);
        log.info("Added Driver with id " + driverId + " for session: " + session.getId());
        return "redirect:/races/edit/" + raceId;
    }
}
