-- ==========================================================
-- V1: 初期テーブル作成
-- Character / Conversation / Message
-- ==========================================================

-- ----------------------------------------------------------
-- characters: キャラクター（司書、文豪、探偵など）
-- ----------------------------------------------------------
CREATE TABLE characters (
    id              BIGSERIAL    PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    page_path       VARCHAR(200) NOT NULL UNIQUE,
    description     TEXT,
    system_prompt   TEXT         NOT NULL,
    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE characters IS 'チャットキャラクター。ページごとに1キャラを紐付ける';
COMMENT ON COLUMN characters.page_path IS 'このキャラが担当するページのパス（例: /, /literature/classic）';

-- ----------------------------------------------------------
-- conversations: 会話セッション
-- ----------------------------------------------------------
CREATE TABLE conversations (
    id              BIGSERIAL    PRIMARY KEY,
    character_id    BIGINT       NOT NULL REFERENCES characters(id),
    session_id      VARCHAR(100) NOT NULL,
    title           VARCHAR(200),
    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_conversations_session_id ON conversations(session_id);
CREATE INDEX idx_conversations_character_id ON conversations(character_id);

COMMENT ON TABLE conversations IS '1ユーザー × 1キャラの会話セッション';
COMMENT ON COLUMN conversations.session_id IS 'ブラウザCookieに発行するUUID';

-- ----------------------------------------------------------
-- messages: 個々のメッセージ
-- ----------------------------------------------------------
CREATE TABLE messages (
    id                BIGSERIAL    PRIMARY KEY,
    conversation_id   BIGINT       NOT NULL REFERENCES conversations(id) ON DELETE CASCADE,
    role              VARCHAR(20)  NOT NULL,
    content           TEXT         NOT NULL,
    created_at        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_message_role CHECK (role IN ('USER', 'ASSISTANT', 'SYSTEM'))
);

CREATE INDEX idx_messages_conversation_id ON messages(conversation_id);
CREATE INDEX idx_messages_created_at ON messages(created_at);

COMMENT ON TABLE messages IS '会話を構成する個別メッセージ';
COMMENT ON COLUMN messages.role IS 'メッセージの送信者（USER/ASSISTANT/SYSTEM）';