create table customers(
    id BIGSERIAL primary key ,
    full_name varchar(50) not null ,
    email varchar(50) not null ,
    password_hash varchar(225) not null ,
    phone_number varchar(20) not null ,
    is_active boolean not null default true,
    role varchar(20) not null ,
    created_at timestamp not null ,
    update_at timestamp not null
);


create unique index  idx_customers_email_lower
on customers (lower(email));