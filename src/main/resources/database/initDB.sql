--region books

create table books
(
    id          bigserial
        constraint books_pk
            primary key,
    title       varchar(1000) not null,
    description varchar(2000),
    cover_id    bigint
        constraint books_files_id_fk
            references files,
    content_id  bigint
        constraint books_files_id_fk_2
            references files
);

comment on table books is 'Книги';

alter table books
    owner to lessonsuser;

create unique index books_id_uindex
    on books (id);

--endregion

--region authors
create table authors
(
    id   bigserial
        constraint authors_pk
            primary key,
    name varchar(1000) not null
);

comment on table authors is 'Авторы';

alter table authors
    owner to lessonsuser;

create unique index authors_id_uindex
    on authors (id);
--endregion

--region books_authors
create table books_authors
(
    book_id   bigint not null
        constraint books_authors_books_id_fk
            references books
        constraint books_authors_authors_id_fk
            references authors,
    author_id bigint not null,
    constraint books_authors_pk
        primary key (book_id, author_id)
);

alter table books_authors
    owner to lessonsuser;
--endregion

--region genres
create table genres
(
    id   bigserial
        constraint genres_pk
            primary key,
    name varchar(1000) not null
);

comment on table genres is 'Жанры';

alter table genres
    owner to lessonsuser;

create unique index genres_name_uindex
    on genres (name);

create unique index genres_id_uindex
    on genres (id);

--endregion

--region books_genres
create table books_genres
(
    book_id  bigint not null
        constraint books_ganres_books_id_fk
            references books,
    genre_id bigint not null
        constraint books_ganres_genres_id_fk
            references genres,
    constraint books_ganres_pk
        primary key (book_id, genre_id)
);

alter table books_genres
    owner to lessonsuser;
--endregion

--region files
create table files
(
    id      bigserial
        constraint files_pk
            primary key,
    name    varchar(1000) not null,
    content bytea
);

alter table files
    owner to lessonsuser;

create unique index files_id_uindex
    on files (id);

--endregion

--region users
create table users
(
    username  varchar(255)                                        not null
        constraint users_pk
            primary key,
    password  varchar(255)                                        not null,
    enabled   boolean                                             not null,
    authority varchar(255) default 'ROLE_USER'::character varying not null
);

alter table users
    owner to lessonsuser;

create unique index users_username_uindex
    on users (username);

--endregion