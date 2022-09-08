create table if not exists passports
(
    id serial primary key,
    seria_p integer not null,
    number_p integer not null,
    created_p DATE not null default CURRENT_DATE,
    expiration_p DATE not null,
    unique (seria_p, number_p)
);