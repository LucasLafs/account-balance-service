CREATE TABLE IF NOT EXISTS accounts (
    account_id      VARCHAR(36)                     NOT NULL,
    owner           VARCHAR(36)                     NOT NULL,
    status          VARCHAR(15)                      NOT NULL,
    amount          NUMERIC                         NOT NULL,
    currency        VARCHAR(9)                      NOT NULL,

    created_at      TIMESTAMP WITH TIME ZONE        NOT NULL,
    updated_at      TIMESTAMP WITH TIME ZONE        NOT NULL,

    PRIMARY KEY (account_id)
);

COMMENT ON COLUMN accounts.account_id            IS 'ACCOUNT IDENTIFIER RECEIVED BY CORE BAKING';
COMMENT ON COLUMN accounts.owner                 IS 'OWNER IDENTIFIER OF ACCOUNT RECEIVED BY CORE BAKING';
COMMENT ON COLUMN accounts.status                IS 'ACCOUNT STATUS RECEIVED BY CORE BAKING';
COMMENT ON COLUMN accounts.amount                IS 'CURRENT BALANCE AMOUNT OF ACCOUNT RECEIVED BY CORE BAKING';
COMMENT ON COLUMN accounts.created_at            IS 'DATE OF CREATE REGISTRY';
COMMENT ON COLUMN accounts.updated_at            IS 'DATE OF UPDATE REGISTRY';
