create sequence hibernate_sequence start with 1 increment by 1;
create table placeholder_item (id bigint not null, name varchar(255) not null, primary key (id));
create table bookings (id bigint not null, created_at timestamp not null, item_id bigint not null, user_username varchar(255) not null, primary key (id));
create table user_roles (user_username varchar(255) not null, roles varchar(255));
create table users (username varchar(255) not null, enabled boolean not null, password varchar(255), primary key (username));
alter table bookings add constraint FKkr21203otxqi6jspe3cn6i7m8 foreign key (item_id) references placeholder_item;
alter table bookings add constraint FKc7a0rbvhayy2wkb3avahurydo foreign key (user_username) references users;
alter table user_roles add constraint FKs9rxtuttxq2ln7mtp37s4clce foreign key (user_username) references users;
