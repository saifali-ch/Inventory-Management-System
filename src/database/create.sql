drop sequence cat_seq;
drop sequence pro_seq;
drop sequence stock_seq;
create sequence cat_seq start with 1 increment by 1 cache 100;
create sequence pro_seq start with 1 increment by 1 cache 100;
create sequence stock_seq start with 1 increment by 1 cache 100;

drop view product_view;

create or replace view product_view as
select p.id id, p.name name, c.name category, description
from product p
         inner join category c on p.category_id = c.id
order by 1;


drop table stock;
drop table product;
drop table category;
create table category
(
    id   number primary key,
    name varchar2(50) unique
);

create table product
(
    id          number primary key,
    name        varchar2(50),
    category_id not null references category (id) on delete cascade,
    description varchar2(500) default 'Description not Provided'
);

create table stock
(
    id          number primary key,
    pro_id      number references product (id),
    date_added  date,
    notify_on   int,
    total_price int,
    quantity    int
);

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

commit

select *
from product_view;