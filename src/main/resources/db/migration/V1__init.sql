CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE table events (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    title VARCHAR(128) NOT NULL,
    description VARCHAR(256) NOT NULL,
    image_url VARCHAR(256) NOT NULL,
    event_url VARCHAR(256) NOT NULL,
    remote BOOLEAN NOT NULL,
    date TIMESTAMP NOT NULL
);

CREATE table coupons (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    code VARCHAR(128) NOT NULL,
    discount INTEGER NOT NULL ,
    valid TIMESTAMP NOT NULL,
    event_id UUID,

    FOREIGN KEY (event_id) references events(id) on delete cascade
);

CREATE table addresses
(
    id      UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    city    VARCHAR(64) NOT NULL,
    uf      VARCHAR(64) NOT NULL,
    event_id UUID,

    FOREIGN KEY (event_id) references events (id) on delete cascade
);