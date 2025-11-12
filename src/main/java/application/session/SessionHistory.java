package application.session;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SessionHistory {

    private static final String SESSION_VISITS_KEY = "pageVisits";

    @SuppressWarnings("unchecked")
    public List<PageVisit> getVisits(HttpSession session) {
        List<PageVisit> visits = (List<PageVisit>) session.getAttribute(SESSION_VISITS_KEY);
        if (visits == null) {
            visits = new ArrayList<>();
            session.setAttribute(SESSION_VISITS_KEY, visits);
        }
        return visits;
    }

    public void addVisit(HttpSession session, String url, String pageName) {
        List<PageVisit> visits = getVisits(session);
        PageVisit visit = new PageVisit(url, pageName, LocalDateTime.now());
        visits.add(visit);
        session.setAttribute(SESSION_VISITS_KEY, visits);
    }

    public void clearHistory(HttpSession session) {
        session.removeAttribute(SESSION_VISITS_KEY);
    }

    public int getVisitCount(HttpSession session) {
        return getVisits(session).size();
    }
}
