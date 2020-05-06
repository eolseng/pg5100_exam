create sequence hibernate_sequence start with 1 increment by 1;

create table copies (id bigint not null, amount integer not null check (amount>=0), item_id bigint not null, user_username varchar(255) not null, primary key (id));
create table items (id bigint not null, description varchar(512), latin_name varchar(128), name varchar(128), pain_level integer not null check (pain_level>=0 AND pain_level<=40), value integer not null check (value>=0 AND value<=9999), primary key (id));
create table user_roles (user_username varchar(255) not null, roles varchar(255));
create table users (username varchar(128) not null, balance bigint not null check (balance>=0), card_packs integer not null check (card_packs>=0), enabled boolean not null, password varchar(255), primary key (username));

alter table items add constraint UK_mpe11mtx1ey7n6fnu341rwc6i unique (latin_name);
alter table items add constraint UK_mnhl79u3u6jdvutuoeq54stne unique (name);
alter table copies add constraint FKaq8v2o6lm5d7cmqr05kuhc9p0 foreign key (item_id) references items;
alter table copies add constraint FKe6nlm6x1vtbq6heg4e09xxdwl foreign key (user_username) references users;
alter table user_roles add constraint FKs9rxtuttxq2ln7mtp37s4clce foreign key (user_username) references users;
