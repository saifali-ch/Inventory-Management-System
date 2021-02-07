drop sequence if exists cat_seq;
drop sequence if exists pro_seq;
drop sequence if exists stock_seq;
drop view if exists stock_view;
drop view if exists product_view;
drop table if exists stock;
drop table if exists product;
drop table if exists category;

create sequence cat_seq start with 1 increment by 1 cache 100;
create sequence pro_seq start with 1 increment by 1 cache 100;
create sequence stock_seq start with 1 increment by 1 cache 100;

create table category
(
    id   number primary key,
    name varchar2(50) unique
);

create table product
(
    id          number primary key,
    name        varchar2(50),
    category_id int not null,
    description varchar2(500) default 'Description not Provided',
    foreign key (category_id) references category (id) on delete cascade
);

create table stock
(
    id          number primary key,
    pro_id      int,
    date_added  date,
    notify_on   int,
    total_price int,
    quantity    int,
    foreign key (pro_id) references product (ID)
);

create or replace view product_view as
select p.id, p.name, c.name category, description
from product p
         inner join category c on p.category_id = c.id
order by 1;

create or replace view stock_view as
select s.id,
       p.id         product_id,
       p.name       product_name,
       p.category   category,
       s.date_added date_added,
       s.notify_on,
       s.total_price,
       s.quantity
from stock s
         join product_view p on s.pro_id = p.id
order by 1;

Insert into category
values (cat_seq.nextval, 'A-Gas');
Insert into category
values (cat_seq.nextval, 'G-Gas');
Insert into category
values (cat_seq.nextval, 'D-Gas');
Insert into category
values (cat_seq.nextval, 'S-Gas');
Insert into category
values (cat_seq.nextval, 'P-Gas');
Insert into category
values (cat_seq.nextval, 'V-Gas');
Insert into category
values (cat_seq.nextval, 'B-Gas');
Insert into category
values (cat_seq.nextval, 'F-Gas');
Insert into category
values (cat_seq.nextval, 'H-Gas');
Insert into category
values (cat_seq.nextval, 'R-Gas');
Insert into category
values (cat_seq.nextval, 'U-Gas');
Insert into product
values (pro_seq.nextval, 'Advocate', 1, 'Product desc here');
Insert into product
values (pro_seq.nextval, 'Asset', 2, 'Product desc here');
Insert into product
values (pro_seq.nextval, 'Burnout', 3, 'Product desc here');
Insert into product
values (pro_seq.nextval, 'Diesel', 4, 'Product desc here');
Insert into product
values (pro_seq.nextval, 'Direct', 5, 'Product desc here');
Insert into product
values (pro_seq.nextval, 'Flag', 6, 'Product desc here');
Insert into product
values (pro_seq.nextval, 'Gasaro', 7, 'Product desc here');
Insert into product
values (pro_seq.nextval, 'Gasoryx', 8, 'Product desc here');
Insert into product
values (pro_seq.nextval, 'Gasque', 9, 'Product desc here');
Insert into product
values (pro_seq.nextval, 'Gastastic', 10, 'Product desc here');
Insert into product
values (pro_seq.nextval, 'Harley', 11, 'Product desc here');
Insert into product
values (pro_seq.nextval, 'Hustle', 1, 'Product desc here');
Insert into product
values (pro_seq.nextval, 'Phantom', 2, 'Product desc here');
Insert into product
values (pro_seq.nextval, 'Pit', 3, 'Product desc here');
Insert into product
values (pro_seq.nextval, 'Platinum', 4, 'Product desc here');
Insert into product
values (pro_seq.nextval, 'Power', 5, 'Product desc here');
Insert into product
values (pro_seq.nextval, 'Reliable', 6, 'Product desc here');
Insert into product
values (pro_seq.nextval, 'Repair', 7, 'Product desc here');
Insert into product
values (pro_seq.nextval, 'Response', 8, 'Product desc here');
Insert into product
values (pro_seq.nextval, 'Sense', 9, 'Product desc here');
Insert into product
values (pro_seq.nextval, 'Snap', 10, 'Product desc here');
Insert into product
values (pro_seq.nextval, 'Spur', 11, 'Product desc here');
Insert into product
values (pro_seq.nextval, 'Universal', 1, 'Product desc here');
Insert into product
values (pro_seq.nextval, 'Volt', 2, 'Product desc here');

commit;