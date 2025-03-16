CREATE TABLE weather (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    station_name VARCHAR(100) NOT NULL,
    wmo_code VARCHAR(10) NOT NULL,
    air_temperature DECIMAL(5, 2) NOT NULL,
    wind_speed DECIMAL(5, 2) NOT NULL,
    weather_phenomenon VARCHAR(255) NOT NULL,
    observation_timestamp TIMESTAMP NOT NULL
);

CREATE TABLE city (
    id BIGINT AUTO_INCREMENT  PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    station_name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS vehicle (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE rbf (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    city_id BIGINT NOT NULL,
    vehicle_id BIGINT NOT NULL,
    amount DECIMAL(5, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN NOT NULL,
    deactivated_at TIMESTAMP,
    FOREIGN KEY (city_id) REFERENCES city(id),
    FOREIGN KEY (vehicle_id) REFERENCES vehicle(id)
);

CREATE TABLE atef (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id BIGINT NOT NULL,
    min_temperature DECIMAL(5, 2) NOT NULL,
    max_temperature DECIMAL(5, 2) NOT NULL,
    amount DECIMAL(5, 2),
    forbidden BOOLEAN NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN NOT NULL,
    deactivated_at TIMESTAMP,
    FOREIGN KEY (vehicle_id) REFERENCES vehicle(id)
);

CREATE TABLE wsef (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id BIGINT NOT NULL,
    min_speed DECIMAL(5, 2) NOT NULL,
    max_speed DECIMAL(5, 2) NOT NULL,
    amount DECIMAL(5, 2),
    forbidden BOOLEAN NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN NOT NULL,
    deactivated_at TIMESTAMP,
    FOREIGN KEY (vehicle_id) REFERENCES vehicle(id)
);

CREATE TABLE wpef(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    amount DECIMAL(5, 2),
    phenomenon VARCHAR(100) NOT NULL,
    forbidden BOOLEAN NOT NULL,
    vehicle_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN NOT NULL,
    deactivated_at TIMESTAMP,
    FOREIGN KEY (vehicle_id) REFERENCES vehicle(id)
);




