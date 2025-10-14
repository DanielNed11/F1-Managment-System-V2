package application.domain;

public enum League {
    Formula_1,
    Formula_2,
    Formula_3,
    Formula_4,
    Formula_E;

    @Override
    public String toString() {
        return name().replace("_", " ");
    }
}
