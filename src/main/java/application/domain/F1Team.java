import java.util.ArrayList;
import java.util.List;

enum TeamStatus { ACTIVE, INACTIVE, LEGENDARY }

public class F1Team {
    private int id;
    private String name;
    private int foundedYear;
    private TeamStatus status;
    private boolean isActive;
    private String teamLogoUrl;
    private double budgetInMillions;

    private List<Driver> drivers = new ArrayList<>();


    public F1Team(int id, String name, int foundedYear, TeamStatus status, boolean isActive, String teamLogoUrl, double budgetInMillions) {
        this.id = id;
        this.name = name;
        this.foundedYear = foundedYear;
        this.status = status;
        this.isActive = isActive;
        this.teamLogoUrl = teamLogoUrl;
        this.budgetInMillions = budgetInMillions;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getFoundedYear() { return foundedYear; }
    public boolean isActive() { return isActive; }
    public String getTeamLogoUrl() { return teamLogoUrl; }
    public List<Driver> getDrivers() { return drivers; }

    public void addDriver(Driver driver) {
        drivers.add(driver);
        driver.setTeam(this);
    }

    @Override
    public String toString() {
        return name + " (" + ", Founded: " + foundedYear + ")";
    }
}
