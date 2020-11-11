drop sequence cat_seq;
drop sequence pro_seq;
create sequence cat_seq start with 1 increment by 1 cache 100;
create sequence pro_seq start with 1 increment by 1 cache 100;

drop table product;
drop table category;
create table category
(
    id int primary key ,
    name varchar2(50) unique
);

create table product
(
    id          int primary key,
    name        varchar2(50),
    category_id int not null constraint fk_category references category(id) on delete cascade,
    description varchar2(500) default 'Description not Provided'
)


insert into category values (cat_seq.nextval, 'Gas');
insert into product(id, name, category_id) values (pro_seq.nextval, 'LPG10-KG', (select id from category where name='Gas'))

select * from product;
select * from category;

commit

