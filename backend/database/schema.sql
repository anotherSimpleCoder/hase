create table Appointments (
    appointmentId int auto_increment not null,
    name varchar(50) not null,
    date datetime not null,
    location varchar(50),

    primary key (appointmentId)
);