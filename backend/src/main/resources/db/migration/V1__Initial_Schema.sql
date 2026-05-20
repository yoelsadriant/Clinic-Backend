-- Lookup / reference tables (no foreign keys)
CREATE TABLE occupation (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE type_app (
    id       BIGSERIAL PRIMARY KEY,
    app_type VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE type_payment (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE type_income (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE status_client (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Staff depends on occupation
CREATE TABLE staff (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    address    VARCHAR(500) NOT NULL,
    phone      VARCHAR(50)  NOT NULL,
    workplace  VARCHAR(255) NOT NULL,
    active     BOOLEAN      NOT NULL DEFAULT TRUE,
    occupation_id BIGINT    NOT NULL REFERENCES occupation(id)
);

-- Login depends on type_app
CREATE TABLE login (
    id          BIGSERIAL PRIMARY KEY,
    username    VARCHAR(255) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    type_app_id BIGINT       NOT NULL REFERENCES type_app(id)
);

-- Client depends on staff
CREATE TABLE client (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    sex             VARCHAR(20)  NOT NULL,
    address         VARCHAR(500) NOT NULL,
    phone           VARCHAR(50)  NOT NULL,
    email           VARCHAR(255) NOT NULL,
    registered_date DATE         NOT NULL,
    staff_id        BIGINT       NOT NULL REFERENCES staff(id)
);

-- Beautician depends on staff
CREATE TABLE beautician (
    id       BIGSERIAL PRIMARY KEY,
    staff_id BIGINT NOT NULL REFERENCES staff(id),
    quantity INT    NOT NULL DEFAULT 0
);

-- Service package (renamed from package to avoid SQL keyword conflict)
CREATE TABLE service_package (
    id    BIGSERIAL PRIMARY KEY,
    name  VARCHAR(255)   NOT NULL,
    price NUMERIC(15, 2) NOT NULL
);

-- Treatment depends on nothing
CREATE TABLE treatment (
    id    BIGSERIAL PRIMARY KEY,
    name  VARCHAR(255)   NOT NULL,
    price NUMERIC(15, 2) NOT NULL
);

-- Product depends on nothing
CREATE TABLE product (
    id       BIGSERIAL PRIMARY KEY,
    name     VARCHAR(255)   NOT NULL,
    quantity INT            NOT NULL DEFAULT 0,
    price    NUMERIC(15, 2) NOT NULL,
    weight   VARCHAR(50)    NOT NULL
);

-- Notification depends on nothing
CREATE TABLE notification (
    id          BIGSERIAL PRIMARY KEY,
    date_time   TIMESTAMP    NOT NULL,
    description VARCHAR(1000) NOT NULL
);

-- Package detail depends on treatment and service_package
CREATE TABLE package_detail (
    id           BIGSERIAL PRIMARY KEY,
    treatment_id BIGINT NOT NULL REFERENCES treatment(id),
    package_id   BIGINT NOT NULL REFERENCES service_package(id),
    quantity     INT    NOT NULL DEFAULT 1
);

-- Schedule depends on client, service_package, treatment, status_client
CREATE TABLE schedule (
    id           BIGSERIAL PRIMARY KEY,
    client_id    BIGINT NOT NULL REFERENCES client(id),
    package_id   BIGINT NOT NULL REFERENCES service_package(id),
    treatment_id BIGINT NOT NULL REFERENCES treatment(id),
    date         DATE   NOT NULL,
    time         TIME   NOT NULL,
    status_id    BIGINT NOT NULL REFERENCES status_client(id)
);

-- Sales depends on staff, type_payment, type_income, client
CREATE TABLE sales (
    id              BIGSERIAL PRIMARY KEY,
    staff_id        BIGINT         NOT NULL REFERENCES staff(id),
    date            DATE           NOT NULL,
    type_payment_id BIGINT         NOT NULL REFERENCES type_payment(id),
    type_income_id  BIGINT         NOT NULL REFERENCES type_income(id),
    client_id       BIGINT         NOT NULL REFERENCES client(id),
    price           NUMERIC(15, 2) NOT NULL,
    description     VARCHAR(1000)  NOT NULL
);

-- Installment depends on client
CREATE TABLE installment (
    id        BIGSERIAL PRIMARY KEY,
    client_id BIGINT         NOT NULL REFERENCES client(id),
    total     NUMERIC(15, 2) NOT NULL,
    date      DATE           NOT NULL,
    due_date  DATE           NOT NULL,
    remaining NUMERIC(15, 2) NOT NULL
);

-- Client package depends on client, service_package, schedule
CREATE TABLE client_package (
    id          BIGSERIAL PRIMARY KEY,
    client_id   BIGINT NOT NULL REFERENCES client(id),
    package_id  BIGINT NOT NULL REFERENCES service_package(id),
    schedule_id BIGINT NOT NULL REFERENCES schedule(id)
);

-- History installment depends on client
CREATE TABLE history_installment (
    id        BIGSERIAL PRIMARY KEY,
    client_id BIGINT         NOT NULL REFERENCES client(id),
    date      DATE           NOT NULL,
    payment   NUMERIC(15, 2) NOT NULL
);

-- Client package detail depends on client_package and package_detail
CREATE TABLE client_package_detail (
    id                BIGSERIAL PRIMARY KEY,
    client_package_id BIGINT NOT NULL REFERENCES client_package(id),
    package_detail_id BIGINT NOT NULL REFERENCES package_detail(id),
    quantity          INT    NOT NULL DEFAULT 1
);

-- History depends on staff, client_package, treatment
CREATE TABLE history (
    id                BIGSERIAL PRIMARY KEY,
    date              DATE   NOT NULL,
    staff_id          BIGINT NOT NULL REFERENCES staff(id),
    client_package_id BIGINT NOT NULL REFERENCES client_package(id),
    treatment_id      BIGINT NOT NULL REFERENCES treatment(id),
    note              VARCHAR(2000)
);

-- Seed data: default app types for role-based access
INSERT INTO type_app (app_type) VALUES ('ADMIN');
INSERT INTO type_app (app_type) VALUES ('DOCTOR');
