CREATE DATABASE IF NOT EXISTS expresstix
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_unicode_ci;


-- ticket detail (item) table
CREATE TABLE IF NOT EXISTS `expresstix`.`ticket_item` (
    `id` BIGINT(20)  NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
    `name` VARCHAR(50) NOT NULL,
    `description` TEXT,
    `stock_initial` INT(11) NOT NULL DEFAULT 0,
    `stock_available` INT(11) NOT NULL DEFAULT 0,
    `is_stock_prepared` BOOLEAN NOT NULL DEFAULT 0,
    `start_time` DATETIME NOT NULL,
    `end_time` DATETIME NOT NULL,
    `start_station` VARCHAR(100) NOT NULL,
    `end_station` VARCHAR(100) NOT NULL,
    `price` DECIMAL(10, 2) NOT NULL,
    `status` INT(11) NOT NULL DEFAULT 1, -- Trạng thái của vé (ví dụ: hoạt động/không hoạt động)
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_status` (`status`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'Table for ticket details';

-- INSERT MOCK DATA
INSERT INTO `expresstix`.`ticket_item` (`name`, `description`,
                                        `stock_initial`, `stock_available`, `is_stock_prepared`,
                                        `start_station`, `end_station`, `price`,
                                        `start_time`, `end_time`,
                                        `status`, `updated_at`, `created_at`)
VALUES
    ('Hạng Phổ Thông', 'Vé phổ thông',
     1000, 1000, 0,
     'Hà Nội', 'TPHCM', 1290000,
     '2024-12-12 00:00:00', '2024-12-12 23:59:59',
     1,  CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Hạng Thương Gia', 'Vé thương gia',
     500, 500, 0,
     'Hà Nội', 'TPHCM', 1290000,
     '2024-12-12 00:00:00', '2024-12-12 23:59:59',
     1,  CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    -- Ticket items for the 01/01 event
    ('Hạng Phổ Thông', 'Vé phổ thông',
     2000, 2000, 0,
     'Hà Nội', 'TPHCM', 1290000,
     '2025-01-01 00:00:00', '2025-01-01 23:59:59',
     1,CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Hạng Thương Gia', 'Vé thương gia',
     1000, 1000, 0,
     'Hà Nội', 'TPHCM', 1290000,
     '2025-01-02 00:00:00', '2025-01-02 23:59:59',
     1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


