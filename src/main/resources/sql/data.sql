INSERT INTO teams (name, founded_year, league, team_logo_url, budget_in_millions) VALUES
                                                                                      ('Mercedes', 1954, 'Formula_1', '/img/mercedesLogo.png', 10.0),
                                                                                      ('Ferrari', 1929, 'Formula_1', '/img/ferrariLogo.png', 30.0),
                                                                                      ('Red Bull Racing', 2005, 'Formula_1', '/img/redbullLogo.png', 20.0),
                                                                                      ('Levski', 1914, 'Formula_E', 'https://upload.wikimedia.org/wikipedia/en/5/51/PFC_Levski_Sofia.svg', 7);

INSERT INTO tracks (name, location, length_km, opened_year, track_type, city_name, days_to_setup, annual_rental_cost, has_temporary_barriers, has_museum, test_days_per_year, facilities) VALUES
                                                                                                                                                                                              ('Monaco GP', 'Monaco', 3.337, 1929, 'STREET', 'Monte Carlo', 42, 15.5, TRUE, NULL, NULL, NULL),
                                                                                                                                                                                              ('Silverstone', 'UK', 5.891, 1948, 'PERMANENT', NULL, NULL, NULL, NULL, TRUE, 20, 'Paddock Club, Modern Pit Complex, F1 Museum'),
                                                                                                                                                                                              ('Spa-Francorchamps', 'Belgium', 7.004, 1921, 'PERMANENT', NULL, NULL, NULL, NULL, FALSE, 15, 'Historic Paddock, Eau Rouge Corner Museum'),
                                                                                                                                                                                              ('Monza', 'Italy', 5.793, 1922, 'PERMANENT', NULL, NULL, NULL, NULL, TRUE, 25, 'Autodromo Museum, Paddock Club, Historic Banking'),
                                                                                                                                                                                              ('Suzuka', 'Japan', 5.807, 1962, 'PERMANENT', NULL, NULL, NULL, NULL, TRUE, 18, 'Honda Collection Hall, Theme Park, Paddock');

INSERT INTO drivers (name, date_of_birth, nationality, world_championships, image_url, team_id) VALUES
                                                                                                    ('Lewis Hamilton', '1985-01-07', 'British', 7, '/img/Hamilton.png', 1),
                                                                                                    ('Max Verstappen', '1997-09-30', 'Dutch', 2, '/img/Verstappen.png', 3),
                                                                                                    ('Charles Leclerc', '1997-10-16', 'Monegasque', 0, '/img/Leclerc.png', 2),
                                                                                                    ('Carlos Sainz', '1994-09-01', 'Spanish', 0, '/img/Carlos.png', 2),
                                                                                                    ('Nikola Tsolov', '2006-12-21', 'Bulgarian', 0, '/img/Nikola.png', 4);

INSERT INTO races (name, date, track_id, winner_id, has_ended) VALUES
                                                                   ('Monaco Grand Prix', '2025-05-25', 1, 1, TRUE),
                                                                   ('British Grand Prix', '2025-07-13', 2, 2, TRUE);

INSERT INTO race_drivers (race_id, driver_id, position) VALUES
                                                  (1, 1, 1),
                                                  (1, 3, 2),
                                                  (2, 2, 1),
                                                  (2, 1, 2);

INSERT INTO app_users (username, password) VALUES ('Daniel', '$2a$12$bTagU/qGW0i/bXumJT8Xs.8swHwJ1nHKehO2q/KX.uo6BxqakPcoW');
INSERT INTO app_users (username, password) VALUES ('Ivan', '$2a$12$gyHY3GTEKgvqY2H.pQaVBOhKAV7GrPOMVI5bhXWJmjXMYreidJRuu');
INSERT INTO app_users (username, password) VALUES ('Viki', '$2a$12$54n6poQDwuP7q9G/ZsKUeOiFfU8.1VI2s0vDb3M9mxKRdBGPz6rli');
