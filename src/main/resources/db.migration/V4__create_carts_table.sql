create table carts(
    id BIGSERIAL primary key ,
    customer_id BIGINT references customers(id) not null
)