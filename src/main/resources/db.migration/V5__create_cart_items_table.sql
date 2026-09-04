create table cart_items(
    id BIGSERIAL primary key ,
    cart_id BIGINT references carts(id) not null ,
    product_id BIGINT references products(id) not null ,
    quantity int not null
)