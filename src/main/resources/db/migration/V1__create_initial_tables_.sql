CREATE TABLE IF NOT EXISTS public.payment (
    id                          uuid PRIMARY KEY,
    status                      VARCHAR(50) NOT NULL,
    created_date                timestamp default current_timestamp,
    last_modified_date          timestamp default current_timestamp
);

CREATE TABLE IF NOT EXISTS public.orders (
    id                          uuid PRIMARY KEY,
    status                      VARCHAR(50),
    amount                      DOUBLE PRECISION NOT NULL,
    payment_id                  uuid UNIQUE,
    payment_method_customer_id  uuid,
    date                        timestamp default current_timestamp,
    created_date                timestamp default current_timestamp,
    last_modified_date          timestamp default current_timestamp,
    FOREIGN KEY (payment_id) REFERENCES payment(id)
);

CREATE TABLE IF NOT EXISTS public.product (
    id                          uuid PRIMARY KEY,
    product_name                VARCHAR(50) NOT NULL,
    description                 VARCHAR(255) NOT NULL,
    amount                      DOUBLE PRECISION NOT NULL,
    status                      VARCHAR(50) NOT NULL,
    created_date                timestamp default current_timestamp,
    last_modified_date          timestamp default current_timestamp
);

CREATE TABLE IF NOT EXISTS public.order_item (
    id                          uuid PRIMARY KEY,
    order_id                    uuid NOT NULL,
    product_id                  uuid NOT NULL,
    quantity                    integer NOT NULL,
    created_date                timestamp default current_timestamp,
    last_modified_date          timestamp default current_timestamp,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES product(id),
    CONSTRAINT uk_item_product UNIQUE (order_id, product_id)
);

CREATE TABLE IF NOT EXISTS public.payments_history (
    id                          uuid PRIMARY KEY,
    order_id                    uuid NOT NULL,
    payment_id                  uuid NOT NULL,
    status                      VARCHAR(50) NOT NULL,
    amount                      DOUBLE PRECISION NOT NULL,
    description                 VARCHAR(50),
    created_date                timestamp default current_timestamp,
    last_modified_date          timestamp default current_timestamp,
    CONSTRAINT uk_payment_status UNIQUE (order_id, status)

);
