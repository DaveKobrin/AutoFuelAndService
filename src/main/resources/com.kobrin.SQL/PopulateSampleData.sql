/**
 * Author:  David Kobrin
 * Created: May 14, 2020
 */
-- DATA INTO USER_TABLE
INSERT INTO USER_TABLE (USER_ID, USER_PASS, USER_TYPE, FIRST_NAME, LAST_NAME)
    VALUES
        ('admin', 'adminpass', 'admin', NULL, NULL),
        ('davek', 'davepass', 'normal', 'David', 'Kobrin');

-- DATA INTO VEHICLE_TABLE
INSERT INTO VEHICLE_TABLE (VIN, MODEL_YEAR, MAKER, MODEL_NAME, TRIM_LEVEL, CURRENT_ODO, TIRE_SIZE, DISPLAY_NAME, IS_ACTIVE, USER_ID)
    VALUES
        ('2C3SAMPLESAMPLE40', 2003, 'CHRYSLER', '300M', 'BASE', 104025, '225/55R16', '300M', TRUE, 'davek'),
        ('JTDSAMPLESAMPLE70', 2014, 'TOYOTA', 'PRIUS V', '3', 79387, '215/65R16', 'PRIUSAMPLE', TRUE, 'davek');

-- DATA INTO FUEL_EVENT
INSERT INTO FUEL_EVENT (VIN, EVENT_TIME, ODOMETER, TOTAL_PRICE, NUM_GAL, IS_FULL_TANK)
    VALUES
        ('2C3SAMPLESAMPLE40', '2020-05-14 17:35:00', 105245, 29.85, 11.412, FALSE),
        ('JTDSAMPLESAMPLE70', '2020-05-14 17:38:00', 81302, 24.21, 9.146, TRUE),
        ('2C3SAMPLESAMPLE40', '2020-05-14 18:39:00', 107245, 2.65, 1.000, TRUE);

-- DATA INTO SERVICE_EVENT
INSERT INTO SERVICE_EVENT (ODOMETER, SERV_DATE, SERV_LOCATION, VIN)
    VALUES
        (105245, '2020-05-14', 'STS Piscataway', '2C3SAMPLESAMPLE40'),
        (86502, '2020-05-16', 'STS Piscataway', 'JTDSAMPLESAMPLE70');

-- DATA INTO SERVICE_TABLE
INSERT INTO SERVICE_TABLE (SERV_DESC, SERV_COST, SERV_EVENT_ID)
    VALUES
        ('Synthetice Oil Change', 44.99, 1),
        ('RR Turn Signal Bulb Replaced', 9.99, 1),
        ('Coolant Flush', 49.99, 1),
        ('Synthetic Oil Change', 44.99, 2),
        ('Front Brake Pads Replaced', 79.99, 2),
        ('Front Brake Rotors Replaced', 149.99, 2),
        ('Brake Fluid Flush', 29.99, 2);
-- DATA INTO VEHICLE_FUEL
--INSERT INTO VEHICLE_FUEL_TABLE (VIN, EVENT_TIME) VALUES
--    ('2C3SAMPLESAMPLE40','2020-05-14 17:35:00'),
--    ('JTDSAMPLESAMPLE70','2020-05-14 17:38:00'),
--    ('2C3SAMPLESAMPLE40','2020-05-14 18:39:00');
