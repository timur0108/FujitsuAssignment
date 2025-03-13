INSERT INTO city (name, station_name) VALUES ('Tallinn', 'Tallinn-Harku');
INSERT INTO city (name, station_name) VALUES ('Tartu', 'Tartu-Tõravere');
INSERT INTO city (name, station_name) VALUES ('Pärnu', 'Pärnu');

INSERT INTO vehicle (name) VALUES ('car');
INSERT INTO vehicle (name) VALUES ('scooter');
INSERT INTO vehicle (name) VALUES ('bike');

INSERT INTO rbf (city_id, vehicle_id, amount)
VALUES
    ((SELECT id FROM city WHERE name = 'Tallinn'), (SELECT id FROM vehicle WHERE name = 'car'), 4.00),
    ((SELECT id FROM city WHERE name = 'Tallinn'), (SELECT id FROM vehicle WHERE name = 'scooter'), 3.50),
    ((SELECT id FROM city WHERE name = 'Tallinn'), (SELECT id FROM vehicle WHERE name = 'bike'), 3.00),
    ((SELECT id FROM city WHERE name = 'Tartu'), (SELECT id FROM vehicle WHERE name = 'car'), 3.50),
    ((SELECT id FROM city WHERE name = 'Tartu'), (SELECT id FROM vehicle WHERE name = 'scooter'), 3.00),
    ((SELECT id FROM city WHERE name = 'Tartu'), (SELECT id FROM vehicle WHERE name = 'bike'), 2.50),
    ((SELECT id FROM city WHERE name = 'Pärnu'), (SELECT id FROM vehicle WHERE name = 'car'), 3.00),
    ((SELECT id FROM city WHERE name = 'Pärnu'), (SELECT id FROM vehicle WHERE name = 'scooter'), 2.50),
    ((SELECT id FROM city WHERE name = 'Pärnu'), (SELECT id FROM vehicle WHERE name = 'bike'), 2.00);

INSERT INTO atef (vehicle_id, min_temperature, max_temperature, amount, forbidden)
VALUES
    ((SELECT id FROM vehicle WHERE name = 'scooter'), -10.00, 0.00, 0.50, FALSE),
    ((SELECT id FROM vehicle WHERE name = 'scooter'), -999.99, -10.01, 1.00, FALSE),
    ((SELECT id FROM vehicle WHERE name = 'bike'), -10.00, 0.00, 0.50, FALSE),
    ((SELECT id FROM vehicle WHERE name = 'bike'), -999.99, -10.01, 1.00, FALSE);

INSERT INTO wsef (vehicle_id, min_speed, max_speed, amount, forbidden)
VALUES
    ((SELECT id FROM vehicle WHERE name = 'bike'), 10.00, 20.00, 0.50, FALSE),
    ((SELECT id FROM vehicle WHERE name = 'bike'), 20.01, 999.99, NULL, TRUE);

INSERT INTO wpef (amount, phenomenon, forbidden, vehicle_id)
VALUES
    (1.00, 'snow', FALSE, (SELECT id FROM vehicle WHERE name = 'scooter')),
    (1.00, 'sleet', FALSE, (SELECT id FROM vehicle WHERE name = 'scooter')),
    (0.50, 'rain', FALSE, (SELECT id FROM vehicle WHERE name = 'scooter')),
    (1.00, 'snow', FALSE, (SELECT id FROM vehicle WHERE name = 'bike')),
    (1.00, 'sleet', FALSE, (SELECT id FROM vehicle WHERE name = 'bike')),
    (0.50, 'rain', FALSE, (SELECT id FROM vehicle WHERE name = 'bike')),
    (NULL, 'glaze', TRUE, (SELECT id FROM vehicle WHERE name = 'scooter')),
    (NULL, 'hail', TRUE, (SELECT id FROM vehicle WHERE name = 'scooter')),
    (NULL, 'thunder', TRUE, (SELECT id FROM vehicle WHERE name = 'scooter')),
    (NULL, 'glaze', TRUE, (SELECT id FROM vehicle WHERE name = 'bike')),
    (NULL, 'hail', TRUE, (SELECT id FROM vehicle WHERE name = 'bike')),
    (NULL, 'thunder', TRUE, (SELECT id FROM vehicle WHERE name = 'bike'));