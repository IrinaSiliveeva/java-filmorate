create table if not exists ratings
(
    mpa_id int not null primary key unique,
    mpa    varchar(10)
);

create table if not exists films
(
    film_id      int         not null auto_increment primary key unique,
    film_title   varchar(64) not null,
    description  varchar(200),
    release_date date,
    duration     int,
    mpa_id       int,
    film_rate    int,
    foreign key (mpa_id) REFERENCES ratings (mpa_id)
);

create table if not exists users
(
    user_id  int         not null auto_increment primary key,
    login    varchar(20) not null unique,
    name     varchar(20),
    email    varchar(64) not null unique,
    birthday date
);

create table if not exists friends
(
    user_id   int not null,
    friend_id int not null,
    friends_status boolean,
    foreign key (user_id) references users (USER_ID) on delete cascade,
    foreign key (friend_id) references users (user_id) on delete cascade
);

create table if not exists film_like
(
    film_id int,
    user_id int,
    foreign key (film_id) references films (film_id) on delete cascade,
    foreign key (user_id) references users (user_id) on delete cascade
);

create table if not exists genres
(
    genre_id int not null unique primary key,
    genre    varchar(30)
);

create table if not exists film_genre
(
    film_id  int not null,
    genre_id int not null,
    foreign key (film_id) references films (film_id) on delete cascade,
    foreign key (genre_id) references genres (genre_id)
);
