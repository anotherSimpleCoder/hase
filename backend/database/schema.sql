create table Appointments (
    appointmentId integer auto increment not null,
    name varchar(30),
    date datetime,
    location varchar(30),

    primary key (appointmentId)
);