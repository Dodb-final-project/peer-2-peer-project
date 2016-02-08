
create database peer1;
use peer1;
create table file
(
hash varchar(255),
file_name varchar(255),
file_size integer(20),
ip_address varchar(255),
Port integer(20)

);

create database peer2;
use peer2;
create table file
(
hash varchar(255),
file_name varchar(255),
file_size integer(20),
ip_address varchar(255),
Port integer(20)

);

create database replication;
use replication;
create table file
(
hash varchar(255),
file_name varchar(255),
file_size integer(20),
ip_address varchar(255),
Port integer(20)

);

