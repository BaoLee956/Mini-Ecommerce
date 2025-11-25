-- ============================
-- V1__init.sql
-- Mini-Ecommerce Initial Schema + Sample Data
-- ============================

-- USERS
CREATE TABLE users
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255)              NOT NULL,
    email      VARCHAR(255)              NOT NULL UNIQUE,
    password   VARCHAR(255)              NOT NULL,
    created_at DATETIME                           DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME                           DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME                           DEFAULT NULL
);

-- ROLES
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);


-- PERMISSION
CREATE TABLE permissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);


-- USER ROLES
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_ur_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_ur_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- ROLE PERMISSION
CREATE TABLE role_permissions (
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    CONSTRAINT fk_rp_role FOREIGN KEY (role_id) REFERENCES roles(id),
    CONSTRAINT fk_rp_permission FOREIGN KEY (permission_id) REFERENCES permissions(id)
);


-- CATEGORIES
CREATE TABLE categories
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at  DATETIME DEFAULT NULL
);

-- PRODUCTS
CREATE TABLE products
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(255)   NOT NULL,
    description    TEXT,
    price          DECIMAL(12, 2) NOT NULL,
    stock_quantity INT            NOT NULL,
    category_id    BIGINT,
    created_at     DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at     DATETIME DEFAULT NULL,
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES categories (id)
);

-- ORDERS
CREATE TABLE orders
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT,
    total_amount DECIMAL(12, 2)                                            NOT NULL,
    status       ENUM ('PENDING','PAID','SHIPPED','DELIVERED','CANCELLED') NOT NULL DEFAULT 'PENDING',
    created_at   DATETIME                                                           DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME                                                           DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at   DATETIME                                                           DEFAULT NULL,
    CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES users (id)
);

-- ORDER ITEMS
CREATE TABLE order_items
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id   BIGINT,
    product_id BIGINT,
    quantity   INT            NOT NULL,
    unit_price DECIMAL(12, 2) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME DEFAULT NULL,
    CONSTRAINT fk_orderitem_order FOREIGN KEY (order_id) REFERENCES orders (id),
    CONSTRAINT fk_orderitem_product FOREIGN KEY (product_id) REFERENCES products (id)
);

-- PAYMENTS
CREATE TABLE payments
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id   BIGINT,
    amount     DECIMAL(12, 2)                   NOT NULL,
    method     VARCHAR(100)                     NOT NULL,
    status     ENUM ('PENDING','PAID','FAILED') NOT NULL DEFAULT 'PENDING',
    paid_at    DATETIME                                  DEFAULT NULL,
    created_at DATETIME                                  DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME                                  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME                                  DEFAULT NULL,
    CONSTRAINT fk_payment_order FOREIGN KEY (order_id) REFERENCES orders (id)
);

-- SHIPPING
CREATE TABLE shippings
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id    BIGINT,
    address     VARCHAR(255),
    city        VARCHAR(100),
    postal_code VARCHAR(50),
    country     VARCHAR(100),
    status      ENUM ('PENDING','SHIPPED','DELIVERED') NOT NULL DEFAULT 'PENDING',
    created_at  DATETIME                                        DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME                                        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at  DATETIME                                        DEFAULT NULL,
    CONSTRAINT fk_shipping_order FOREIGN KEY (order_id) REFERENCES orders (id)
);

-- ============================
-- SAMPLE DATA
-- ============================

-- USERS
INSERT INTO users (name, email, password)
VALUES ('Admin User', 'admin@example.com', 'admin123'),
       ('John Doe', 'john@example.com', 'password123'),
       ('Jane Smith', 'jane@example.com', 'password456');

-- ROLES
INSERT INTO roles (name, description)
VALUES ('ADMIN', 'Full admin access'),
       ('CUSTOMER', 'Customer with limited access'),
       ('STAFF', 'Employee who manages orders');

-- PERMISSION
INSERT INTO permissions (name, description)
VALUES
    ('USER_VIEW', 'View users'),
    ('USER_CREATE', 'Create users'),
    ('USER_DELETE', 'Delete users'),
    ('PRODUCT_VIEW', 'View product list'),
    ('PRODUCT_CREATE', 'Create product'),
    ('ORDER_MANAGE', 'Manage customer orders');

-- USER ROLES
INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1), (2, 2), (3, 3);

-- ROLE PERMISSIONS
INSERT INTO role_permissions (role_id, permission_id)
VALUES (1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6),
      (2, 4),
      (3, 4), (3, 5), (3, 6);

-- CATEGORIES
INSERT INTO categories (name, description)
VALUES ('Plants', 'Various kinds of plants'),
       ('Fish', 'Aquarium fish'),
       ('Accessories', 'Decorations and accessories');

-- PRODUCTS
INSERT INTO products (name, description, price, stock_quantity, category_id)
VALUES ('Aloe Vera', 'Fresh Aloe Vera plant', 100000, 50, 1),
       ('Peace Lily', 'Indoor plant, air purifier', 150000, 30, 1),
       ('Goldfish', 'Small freshwater fish', 50000, 100, 2),
       ('Betta Fish', 'Colorful fighting fish', 80000, 40, 2),
       ('Aquarium Rocks', 'Decorative stones for aquarium', 20000, 200, 3);

-- ORDERS
INSERT INTO orders (user_id, total_amount, status)
VALUES (2, 200000, 'PENDING'),
       (3, 130000, 'PAID');

-- ORDER ITEMS
INSERT INTO order_items (order_id, product_id, quantity, unit_price)
VALUES (1, 1, 2, 100000),
       (2, 3, 2, 50000),
       (2, 5, 1, 30000);

-- PAYMENTS
INSERT INTO payments (order_id, amount, method, status, paid_at)
VALUES (2, 130000, 'CASH', 'PAID', NOW());

-- SHIPPINGS
INSERT INTO shippings (order_id, address, city, postal_code, country, status)
VALUES (1, '123 Green St', 'Hanoi', '100000', 'Vietnam', 'PENDING'),
       (2, '456 Blue Rd', 'Ho Chi Minh City', '700000', 'Vietnam', 'DELIVERED');
