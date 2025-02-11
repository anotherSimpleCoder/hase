CREATE TABLE Users (
    matrikelNr INTEGER PRIMARY KEY,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL ,
    email VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,

    CONSTRAINT EmailConstraint
                   CHECK (email LIKE '%@%.%')
);

CREATE TABLE Appointments (
    appointmentId INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(50) NOT NULL,
    date LocalDateTime NOT NULL,
    location VARCHAR(50)
);

CREATE TABLE participates (
    appointmentId INTEGER NOT NULL,
    matrikelNr INTEGER NOT NULL,

    FOREIGN KEY (matrikelNr) REFERENCES Users(matrikelNr),
    FOREIGN KEY (appointmentId) REFERENCES Appointments(appointmentId)
)
