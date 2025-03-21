INSERT INTO city (name, station_name) VALUES ('Tallinn', 'Tallinn-Harku');
INSERT INTO city (name, station_name) VALUES ('Tartu', 'Tartu-Tõravere');
INSERT INTO city (name, station_name) VALUES ('Pärnu', 'Pärnu');

INSERT INTO vehicle (name) VALUES ('car');
INSERT INTO vehicle (name) VALUES ('scooter');
INSERT INTO vehicle (name) VALUES ('bike');

INSERT INTO rbf (city_id, vehicle_id, amount, created_at, active, deactivated_at)
VALUES
    ((SELECT id FROM city WHERE name = 'Tallinn'), (SELECT id FROM vehicle WHERE name = 'car'), 3.80, '2024-12-01 12:00:00', FALSE, '2025-03-01 12:00:00'),
    ((SELECT id FROM city WHERE name = 'Tallinn'), (SELECT id FROM vehicle WHERE name = 'scooter'), 3.30, '2024-11-15 08:30:00', FALSE, '2025-03-01 12:00:00'),
    ((SELECT id FROM city WHERE name = 'Tallinn'), (SELECT id FROM vehicle WHERE name = 'bike'), 2.90, '2024-10-20 15:45:00', FALSE, '2025-03-01 12:00:00'),
    ((SELECT id FROM city WHERE name = 'Tartu'), (SELECT id FROM vehicle WHERE name = 'car'), 3.20, '2024-12-05 10:15:00', FALSE, '2025-03-01 12:00:00'),
    ((SELECT id FROM city WHERE name = 'Tartu'), (SELECT id FROM vehicle WHERE name = 'scooter'), 2.80, '2024-11-10 14:20:00', FALSE, '2025-03-01 12:00:00'),
    ((SELECT id FROM city WHERE name = 'Tartu'), (SELECT id FROM vehicle WHERE name = 'bike'), 2.30, '2024-09-30 07:10:00', FALSE, '2025-03-01 12:00:00'),
    ((SELECT id FROM city WHERE name = 'Pärnu'), (SELECT id FROM vehicle WHERE name = 'car'), 2.80, '2024-10-25 13:40:00', FALSE, '2025-03-01 12:00:00'),
    ((SELECT id FROM city WHERE name = 'Pärnu'), (SELECT id FROM vehicle WHERE name = 'scooter'), 2.20, '2024-09-05 09:55:00', FALSE, '2025-03-01 12:00:00'),
    ((SELECT id FROM city WHERE name = 'Pärnu'), (SELECT id FROM vehicle WHERE name = 'bike'), 1.80, '2024-08-15 16:30:00', FALSE, '2025-03-01 12:00:00'),
    ((SELECT id FROM city WHERE name = 'Pärnu'), (SELECT id FROM vehicle WHERE name = 'bike'), 2.5, '2024-06-15 16:30:00', FALSE, '2024-08-15 17:00:00');

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

INSERT INTO atef (vehicle_id, min_temperature, max_temperature, amount, forbidden, active, created_at, deactivated_at)
VALUES
    ((SELECT id FROM vehicle WHERE name = 'scooter'), -10.00, 0.00, 10, FALSE, FALSE, CURRENT_TIMESTAMP - INTERVAL '30' DAY, CURRENT_TIMESTAMP - INTERVAL '2' DAY),
    ((SELECT id FROM vehicle WHERE name = 'scooter'), -999.99, -10.01, 15, FALSE, FALSE, CURRENT_TIMESTAMP - INTERVAL '60' DAY, CURRENT_TIMESTAMP - INTERVAL '2' DAY),
    ((SELECT id FROM vehicle WHERE name = 'scooter'), -999.99, -10.01, 30, FALSE, FALSE, CURRENT_TIMESTAMP - INTERVAL '365' DAY, CURRENT_TIMESTAMP - INTERVAL '60' DAY),
    ((SELECT id FROM vehicle WHERE name = 'bike'), -10.00, 0.00, 20, FALSE, FALSE, CURRENT_TIMESTAMP - INTERVAL '15' DAY, CURRENT_TIMESTAMP - INTERVAL '2' DAY),
    ((SELECT id FROM vehicle WHERE name = 'bike'), -999.99, -10.01, 25, FALSE, FALSE, CURRENT_TIMESTAMP - INTERVAL '45' DAY, CURRENT_TIMESTAMP - INTERVAL '2' DAY);


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