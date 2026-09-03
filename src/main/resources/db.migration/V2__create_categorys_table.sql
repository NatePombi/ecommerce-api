create table categories(
    id BIGSERIAL primary key ,
    name varchar(50) unique not null
)