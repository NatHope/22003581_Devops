INSERT INTO aircraft (tail_number, model, manufacturer, capacity, status) VALUES
('G-ABCD', 'A320', 'Airbus', 180, 'AIRWORTHY'),
('G-EFGH', '737-800', 'Boeing', 189, 'MAINTENANCE'),
('G-IJKL', 'C172', 'Cessna', 4, 'AIRWORTHY')
ON DUPLICATE KEY UPDATE tail_number = tail_number;
