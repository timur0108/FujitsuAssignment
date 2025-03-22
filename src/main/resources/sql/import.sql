INSERT INTO city (name, station_name, deleted) VALUES ('Tallinn', 'Tallinn-Harku', false);
INSERT INTO city (name, station_name, deleted) VALUES ('Tartu', 'Tartu-Tõravere', false);
INSERT INTO city (name, station_name, deleted) VALUES ('Pärnu', 'Pärnu', false);

INSERT INTO vehicle (name) VALUES ('car');
INSERT INTO vehicle (name) VALUES ('scooter');
INSERT INTO vehicle (name) VALUES ('bike');

INSERT INTO rbf (city_id, vehicle_id, amount, active)
VALUES
    ((SELECT id FROM city WHERE name = 'Tallinn'), (SELECT id FROM vehicle WHERE name = 'car'), 4.00, TRUE),
    ((SELECT id FROM city WHERE name = 'Tallinn'), (SELECT id FROM vehicle WHERE name = 'scooter'), 3.50, TRUE),
    ((SELECT id FROM city WHERE name = 'Tallinn'), (SELECT id FROM vehicle WHERE name = 'bike'), 3.00, TRUE),
    ((SELECT id FROM city WHERE name = 'Tartu'), (SELECT id FROM vehicle WHERE name = 'car'), 3.50, TRUE),
    ((SELECT id FROM city WHERE name = 'Tartu'), (SELECT id FROM vehicle WHERE name = 'scooter'), 3.00, TRUE),
    ((SELECT id FROM city WHERE name = 'Tartu'), (SELECT id FROM vehicle WHERE name = 'bike'), 2.50, TRUE),
    ((SELECT id FROM city WHERE name = 'Pärnu'), (SELECT id FROM vehicle WHERE name = 'car'), 3.00, TRUE),
    ((SELECT id FROM city WHERE name = 'Pärnu'), (SELECT id FROM vehicle WHERE name = 'scooter'), 2.50, TRUE),
    ((SELECT id FROM city WHERE name = 'Pärnu'), (SELECT id FROM vehicle WHERE name = 'bike'), 2.00, TRUE);

INSERT INTO atef (vehicle_id, min_temperature, max_temperature, amount, forbidden, active)
VALUES
    ((SELECT id FROM vehicle WHERE name = 'scooter'), -10.00, 0.00, 0.50, FALSE, TRUE),
    ((SELECT id FROM vehicle WHERE name = 'scooter'), -999.99, -10.01, 1.00, FALSE, TRUE),
    ((SELECT id FROM vehicle WHERE name = 'bike'), -10.00, 0.00, 0.50, FALSE, TRUE),
    ((SELECT id FROM vehicle WHERE name = 'bike'), -999.99, -10.01, 1.00, FALSE, TRUE);

INSERT INTO wsef (vehicle_id, min_speed, max_speed, amount, forbidden, active)
VALUES
    ((SELECT id FROM vehicle WHERE name = 'bike'), 10.00, 20.00, 0.50, FALSE, TRUE),
    ((SELECT id FROM vehicle WHERE name = 'bike'), 20.01, 999.99, NULL, TRUE, TRUE);

INSERT INTO wpef (amount, phenomenon, forbidden, vehicle_id, active)
VALUES
    (1.00, 'snow', FALSE, (SELECT id FROM vehicle WHERE name = 'scooter'), TRUE),
    (1.00, 'sleet', FALSE, (SELECT id FROM vehicle WHERE name = 'scooter'), TRUE),
    (0.50, 'rain', FALSE, (SELECT id FROM vehicle WHERE name = 'scooter'), TRUE),
    (1.00, 'snow', FALSE, (SELECT id FROM vehicle WHERE name = 'bike'), TRUE),
    (1.00, 'sleet', FALSE, (SELECT id FROM vehicle WHERE name = 'bike'), TRUE),
    (0.50, 'rain', FALSE, (SELECT id FROM vehicle WHERE name = 'bike'), TRUE),
    (NULL, 'glaze', TRUE, (SELECT id FROM vehicle WHERE name = 'scooter'), TRUE),
    (NULL, 'hail', TRUE, (SELECT id FROM vehicle WHERE name = 'scooter'), TRUE),
    (NULL, 'thunder', TRUE, (SELECT id FROM vehicle WHERE name = 'scooter'), TRUE),
    (NULL, 'glaze', TRUE, (SELECT id FROM vehicle WHERE name = 'bike'), TRUE),
    (NULL, 'hail', TRUE, (SELECT id FROM vehicle WHERE name = 'bike'), TRUE),
    (NULL, 'thunder', TRUE, (SELECT id FROM vehicle WHERE name = 'bike'), TRUE);