CREATE TABLE documents (
    id              bigint NOT NULL,
    name            varchar,
    author          varchar,
    uploaded_at     timestamptz,
    updated_at      timestamptz,
    bin_content     bytea,
    content         text,
    key_words       varchar
)