create table questions
(
    id serial primary key ,
    title varchar(100) not null,
    low_label varchar(100) not null,
    high_label varchar(100) not null
)