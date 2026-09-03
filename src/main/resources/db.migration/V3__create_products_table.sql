create table products(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL ,
    category_id BIGINT REFERENCES categories(id) NOT NULL ,
    price DECIMAL(12,2) NOT NULL ,
    description VARCHAR(200) NOT NULL,
    image_url VARCHAR(50) NOT NULL ,
    stock int NOT NULL ,
    status VARCHAR(20) NOT NULL ,
    created_at TIMESTAMP NOT NULL ,
    updated_at TIMESTAMP NOT NULL
);

create unique index idx_products_name_lower
on products (lower(name));