CREATE TABLE IF NOT EXISTS transactions (
    transaction_id          VARCHAR(36)                     NOT NULL,
    account_id              VARCHAR(36)                     NOT NULL,
    type                    VARCHAR(15)                     NOT NULL,
    status                  VARCHAR(15)                     NOT NULL,
    amount                  NUMERIC                         NOT NULL,
    currency                VARCHAR(10)                     NOT NULL,

    created_at      TIMESTAMP WITH TIME ZONE        NOT NULL,

    PRIMARY KEY (transaction_id),
    CONSTRAINT uq_transaction_account UNIQUE (transaction_id, account_id)
);

COMMENT ON COLUMN transactions.transaction_id           IS 'TRANSACTION IDENTIFIER RECEIVED BY CORE BAKING';
COMMENT ON COLUMN transactions.account_id               IS 'ACCOUNT IDENTIFIER RESPONSIBLE FOR THE TRANSACTION RECEIVED BY CORE BAKING';
COMMENT ON COLUMN transactions.status                   IS 'TRANSACTION STATUS RECEIVED BY CORE BAKING';
COMMENT ON COLUMN transactions.amount                   IS 'CURRENT BALANCE AMOUNT OF ACCOUNT RECEIVED BY CORE BAKING';
COMMENT ON COLUMN transactions.created_at               IS 'DATE OF CREATE REGISTRY';