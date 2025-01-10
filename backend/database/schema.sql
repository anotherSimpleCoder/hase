create table Appointments (
    appointmentId integer autoincrement,
    name varchar(50) not null,
    date datetime not null,
    location varchar(50),

    primary key (appointmentId)
);