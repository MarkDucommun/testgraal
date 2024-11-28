create table if not exists habit
(
    id   int auto_increment primary key,
    name text not null unique
);
