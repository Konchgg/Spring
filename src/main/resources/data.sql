-- Удаление таблиц, если они существуют
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS knives;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS manufacturers;


-- Создание таблиц
CREATE TABLE manufacturers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

-- Создание таблицы заказов
CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(50),
    address VARCHAR(255),
    status VARCHAR(50)  -- статус заказа
);


CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE knives (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    manufacturer_id INT REFERENCES manufacturers(id),
    category_id INT REFERENCES categories(id),
    price DECIMAL NOT NULL,
    description TEXT,
    quantity INT,
    manufacture_date DATE
);

CREATE TABLE reviews (
    id SERIAL PRIMARY KEY,
    knife_id INT REFERENCES knives(id) ON DELETE CASCADE,
    review_text TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL
);

-- Вставка данных
INSERT INTO manufacturers (name) VALUES ('Cold Steel'), ('Spyderco'), ('Buck Knives');
INSERT INTO categories (name) VALUES ('Тактические'), ('Спортивные'), ('Охотничьи');
INSERT INTO knives (name, manufacturer_id, category_id, price, description, quantity, manufacture_date)
VALUES
('Recon 1', 1, 1, 100.0, 'Тактический нож Cold Steel Recon 1', 10, '2022-01-15'),
('Paramilitary 2', 2, 1, 150.0, 'Тактический нож Spyderco Paramilitary 2', 5, '2023-03-20'),
('305', 3, 2, 120.0, 'Специальный нож 305', 7, '2023-03-20');

-- Вставка отзывов
INSERT INTO reviews (knife_id, review_text, created_at) VALUES
(1, 'Отличный нож для тактических операций. Прочный и надежный.', '2024-01-01 10:00:00'),
(1, 'Легкий и эргономичный, но требует частой заточки.', '2024-01-15 14:30:00'),
(2, 'Лучший выбор для ежедневного использования. Замечательное качество.', '2024-02-10 17:45:00'),
(2, 'Удобен в руке, но клинок может слегка люфтить.', '2024-03-05 09:15:00'),
(3, 'Классический дизайн и отличная цена. Рекомендую.', '2024-03-20 12:00:00'),
(3, 'Доволен покупкой. Однако качество сборки могло бы быть лучше.', '2024-04-01 16:20:00');