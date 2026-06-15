INSERT INTO hangar_bay (bay_number, location, capacity) VALUES
('BAY-1', 'North Hangar', 2),
('BAY-2', 'North Hangar', 1),
('BAY-3', 'South Hangar', 4)
ON DUPLICATE KEY UPDATE bay_number = bay_number;
