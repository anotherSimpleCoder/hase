CREATE TABLE Users (
    matrikelNr INTEGER PRIMARY KEY AUTOINCREMENT ,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL ,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL
);

INSERT INTO Users(firstName, lastName, email)
VALUES ('Erwin', 'Holzhauser','erwinholzhauser.eh@gmail.com');

CREATE TABLE Appointments (
        appointmentId INTEGER PRIMARY KEY AUTOINCREMENT ,
        name VARCHAR(50) NOT NULL,
        date LocalDateTime NOT NULL,
        location VARCHAR(50)
                          );
