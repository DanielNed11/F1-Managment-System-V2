DROP TABLE IF EXISTS race_drivers;
DROP TABLE IF EXISTS races;
DROP TABLE IF EXISTS drivers;
DROP TABLE IF EXISTS teams;
DROP TABLE IF EXISTS tracks;

CREATE TABLE teams (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    founded_year INT,
    league VARCHAR(50),
    team_logo_url VARCHAR(500),
    budget_in_millions DOUBLE
);

CREATE TABLE drivers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    date_of_birth DATE,
    nationality VARCHAR(100),
    world_championships INT DEFAULT 0,
    image_url VARCHAR(500),
    team_id INT,
    FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE SET NULL
);

CREATE TABLE tracks (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255),
    length_km DOUBLE,
    opened_year INT,
    track_type VARCHAR(20) DEFAULT 'BASIC',
    city_name VARCHAR(255),
    days_to_setup INT,
    annual_rental_cost DOUBLE,
    has_temporary_barriers BOOLEAN,
    has_museum BOOLEAN,
    test_days_per_year INT,
    facilities VARCHAR(500)
);

CREATE TABLE races (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    date DATE,
    track_id INT,
    winner_id INT,
    has_ended BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (track_id) REFERENCES tracks(id) ON DELETE SET NULL,
    FOREIGN KEY (winner_id) REFERENCES drivers(id) ON DELETE SET NULL
);

CREATE TABLE race_drivers (
    race_id INT NOT NULL,
    driver_id INT NOT NULL,
    PRIMARY KEY (race_id, driver_id),
    FOREIGN KEY (race_id) REFERENCES races(id) ON DELETE CASCADE,
    FOREIGN KEY (driver_id) REFERENCES drivers(id) ON DELETE CASCADE
);
