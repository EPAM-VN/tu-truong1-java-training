CREATE TABLE customers
(
    id            UUID NOT NULL         DEFAULT uuidv7(),
    first_name    VARCHAR(255),
    last_name     VARCHAR(255),
    date_of_birth TIMESTAMP WITH TIME ZONE,
    identifier    VARCHAR(255),
    email         VARCHAR(255) NOT NULL,
    is_active     BOOLEAN      NOT NULL DEFAULT FALSE,
    membership    VARCHAR(255) NOT NULL,
    CONSTRAINT pk_customers PRIMARY KEY (id)
);

CREATE UNIQUE INDEX idx_customers_email ON customers (email);

CREATE TABLE addresses
(
    id          UUID NOT NULL   DEFAULT uuidv7(),
    street      VARCHAR(255),
    city        VARCHAR(255),
    state       VARCHAR(255),
    country     VARCHAR(255),
    postal_code VARCHAR(255),
    type        VARCHAR(255),
    customer_id UUID,
    CONSTRAINT pk_addresses PRIMARY KEY (id)
);

ALTER TABLE addresses
    ADD CONSTRAINT FK_ADDRESSES_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customers (id);

CREATE TABLE order_has_promotions
(
    order_id     UUID NOT NULL,
    promotion_id UUID NOT NULL,
    CONSTRAINT pk_order_has_promotions PRIMARY KEY (order_id, promotion_id)
);

CREATE TABLE promotions
(
    id             UUID                                                          NOT NULL   DEFAULT uuidv7(),
    code           VARCHAR(255)                                                  NOT NULL,
    name           VARCHAR(255)                                                  NOT NULL,
    description    VARCHAR(255),
    status         VARCHAR(255)                                                  NOT NULL,
    effective_from BIGINT DEFAULT (EXTRACT(EPOCH FROM CURRENT_TIMESTAMP) * 1000) NOT NULL,
    effective_to   BIGINT,
    CONSTRAINT pk_promotions PRIMARY KEY (id)
);

CREATE UNIQUE INDEX idx_promotions_code ON promotions (code);

CREATE TABLE orders
(
    id              UUID         NOT NULL   DEFAULT uuidv7(),
    notes           VARCHAR(255),
    status          VARCHAR(255) NOT NULL,
    payment_method  VARCHAR(255) NOT NULL,
    promotion_codes VARCHAR(255),
    customer_id     UUID,
    CONSTRAINT pk_orders PRIMARY KEY (id)
);

ALTER TABLE orders
    ADD CONSTRAINT FK_ORDERS_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customers (id);

ALTER TABLE order_has_promotions
    ADD CONSTRAINT fk_ordhaspro_on_order FOREIGN KEY (order_id) REFERENCES orders (id);

ALTER TABLE order_has_promotions
    ADD CONSTRAINT fk_ordhaspro_on_promotion FOREIGN KEY (promotion_id) REFERENCES promotions (id);