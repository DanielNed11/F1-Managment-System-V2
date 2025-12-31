package application.controller;

import application.domain.PermanentCircuit;
import application.domain.Race;
import application.domain.StreetCircuit;
import application.domain.Track;
import application.service.IRaceService;
import application.service.impl.TrackService;
import application.session.SessionHistory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tracks")
public class TrackController {

    private final TrackService trackService;
    private final IRaceService raceService;
    private final SessionHistory sessionHistory;
    private final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    public TrackController(TrackService trackService, IRaceService raceService, SessionHistory sessionHistory) {
        this.trackService = trackService;
        this.raceService = raceService;
        this.sessionHistory = sessionHistory;
    }

    private String getTrackType(Track track) {
        if (track instanceof StreetCircuit) return "STREET";
        if (track instanceof PermanentCircuit) return "PERMANENT";
        return "BASIC";
    }

    private Map<Integer, String> buildTrackTypesMap(List<Track> tracks) {
        Map<Integer, String> trackTypes = new HashMap<>();
        tracks.forEach(track -> trackTypes.put(track.getId(), getTrackType(track)));
        return trackTypes;
    }


    @GetMapping
    public String showAllTracks(HttpSession session, Model model) {
        sessionHistory.addVisit(session, "/tracks", "All Tracks");
        List<Track> tracks = trackService.getAll();
        model.addAttribute("tracks", tracks);
        model.addAttribute("trackTypes", buildTrackTypesMap(tracks));
        log.info("Showing all tracks");
        return "tracks/tracks";
    }

    @GetMapping("/{id}")
    public String getTrack(HttpSession session, Model model, @PathVariable int id) {
        sessionHistory.addVisit(session, "/tracks/" + id, "Track Details");
        Track track = trackService.getById(id);
        List<Race> races = raceService.getAll().stream()
                .filter(r -> r.getTrack() != null && r.getTrack().getId() == id)
                .collect(Collectors.toList());
        model.addAttribute("track", track);
        model.addAttribute("races", races);
        model.addAttribute("trackType", getTrackType(track));
        log.info("Getting track with id " + id);
        return "tracks/track";
    }

    @GetMapping("/add")
    public String showAddTrack(HttpSession session, Model model,
                               @RequestParam(required = false, defaultValue = "BASIC") String trackType) {
        sessionHistory.addVisit(session, "/tracks/add", "Add Track");

        Track track;
        if ("STREET".equals(trackType)) {
            track = new StreetCircuit();
        } else if ("PERMANENT".equals(trackType)) {
            track = new PermanentCircuit();
        } else {
            track = new Track();
        }

        model.addAttribute("track", track);
        model.addAttribute("trackType", trackType);
        log.info("Showing add track form for type: " + trackType);
        return "tracks/add-track";
    }

    @PostMapping("/add")
    public String addTrack(HttpServletRequest request,
                          @RequestParam String trackType,
                          Model model) {
        Track track;

        if ("STREET".equals(trackType)) {
            StreetCircuit streetCircuit = new StreetCircuit();
            streetCircuit.setName(request.getParameter("name"));
            streetCircuit.setLocation(request.getParameter("location"));
            streetCircuit.setLengthKm(Double.parseDouble(request.getParameter("lengthKm")));
            streetCircuit.setOpenedYear(Integer.parseInt(request.getParameter("openedYear")));
            streetCircuit.setCityName(request.getParameter("cityName"));
            streetCircuit.setDaysToSetup(Integer.parseInt(request.getParameter("daysToSetup")));
            streetCircuit.setAnnualRentalCost(Double.parseDouble(request.getParameter("annualRentalCost")));
            streetCircuit.setHasTemporaryBarriers(request.getParameter("hasTemporaryBarriers") != null);
            track = streetCircuit;
        } else if ("PERMANENT".equals(trackType)) {
            PermanentCircuit permanentCircuit = new PermanentCircuit();
            permanentCircuit.setName(request.getParameter("name"));
            permanentCircuit.setLocation(request.getParameter("location"));
            permanentCircuit.setLengthKm(Double.parseDouble(request.getParameter("lengthKm")));
            permanentCircuit.setOpenedYear(Integer.parseInt(request.getParameter("openedYear")));
            permanentCircuit.setHasMuseum(request.getParameter("hasMuseum") != null);
            permanentCircuit.setTestDaysPerYear(Integer.parseInt(request.getParameter("testDaysPerYear")));
            permanentCircuit.setFacilities(request.getParameter("facilities"));
            track = permanentCircuit;
        } else {
            track = new Track();
            track.setName(request.getParameter("name"));
            track.setLocation(request.getParameter("location"));
            track.setLengthKm(Double.parseDouble(request.getParameter("lengthKm")));
            track.setOpenedYear(Integer.parseInt(request.getParameter("openedYear")));
        }

        trackService.add(track);
        log.info("Added new track of type: " + trackType);
        return "redirect:/tracks";
    }

    @GetMapping("/edit/{id}")
    public String showEditTrackForm(HttpSession session, Model model, @PathVariable int id) {
        sessionHistory.addVisit(session, "/tracks/edit/" + id, "Edit Track");
        Track track = trackService.getById(id);
        model.addAttribute("track", track);
        model.addAttribute("trackType", getTrackType(track));
        log.info("Editing track with id " + id);
        return "tracks/edit-track";
    }

    @PostMapping("/edit/{id}")
    public String editTrack(@PathVariable int id,
                           HttpServletRequest request,
                           @RequestParam String trackType,
                           Model model) {
        Track track = trackService.getById(id);

        track.setName(request.getParameter("name"));
        track.setLocation(request.getParameter("location"));
        track.setLengthKm(Double.parseDouble(request.getParameter("lengthKm")));
        track.setOpenedYear(Integer.parseInt(request.getParameter("openedYear")));

        if (track instanceof StreetCircuit streetCircuit) {
            streetCircuit.setCityName(request.getParameter("cityName"));
            streetCircuit.setDaysToSetup(Integer.parseInt(request.getParameter("daysToSetup")));
            streetCircuit.setAnnualRentalCost(Double.parseDouble(request.getParameter("annualRentalCost")));
            streetCircuit.setHasTemporaryBarriers(request.getParameter("hasTemporaryBarriers") != null);
        } else if (track instanceof PermanentCircuit permanentCircuit) {
            permanentCircuit.setHasMuseum(request.getParameter("hasMuseum") != null);
            permanentCircuit.setTestDaysPerYear(Integer.parseInt(request.getParameter("testDaysPerYear")));
            permanentCircuit.setFacilities(request.getParameter("facilities"));
        }

        trackService.update(track);
        log.info("Edited track with id " + id + " of type " + trackType);
        return "redirect:/tracks";
    }

    @PostMapping("/{id}/delete")
    public String deleteTrack(@PathVariable int id, HttpSession session) {
        trackService.delete(id);
        log.info("Deleted track with id " + id + " for session: " + session.getId());
        return "redirect:/tracks";
    }

    @GetMapping("/long-tracks")
    public String showLongTracks(HttpSession session, Model model) {
        sessionHistory.addVisit(session, "/tracks/long-tracks", "Long Tracks");
        List<Track> longTracks = trackService.getAll().stream()
                .filter(t -> t.getLengthKm() > 5.0)
                .collect(Collectors.toList());
        model.addAttribute("tracks", longTracks);
        model.addAttribute("trackTypes", buildTrackTypesMap(longTracks));
        log.info("Showing long tracks (>5km)");
        return "tracks/long-tracks";
    }

}
