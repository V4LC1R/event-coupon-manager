CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE table events (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    title VARCHAR(128) NOT NULL,
    description VARCHAR(256) NOT NULL,
    imageUrl VARCHAR(256) NOT NULL,
    eventUrl VARCHAR(256) NOT NULL,
    remote BOOLEAN NOT NULL,
    date TIMESTAMP NOT NULL
);

CREATE table coupons (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    code VARCHAR(128) NOT NULL,
    discount INTEGER NOT NULL ,
    valid TIMESTAMP NOT NULL,
    eventId UUID,

    FOREIGN KEY (eventId) references events(id) on delete cascade
);

CREATE table addresses
(
    id      UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    city    VARCHAR(64) NOT NULL,
    uf      VARCHAR(64) NOT NULL,
    eventId UUID,

    FOREIGN KEY (eventId) references events (id) on delete cascade
);