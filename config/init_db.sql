--- [ drop all tables ] ------------------------------------------------------------------------------------------------

DROP TABLE IF EXISTS resume, contact, section;

--- [ resume ] ---------------------------------------------------------------------------------------------------------

CREATE TABLE resume
(
  uuid      VARCHAR(36) PRIMARY KEY NOT NULL,
  full_name TEXT                    NOT NULL
);

--- [ contact ] --------------------------------------------------------------------------------------------------------

CREATE TABLE contact
(
  id   SERIAL,
  uuid VARCHAR(36) NOT NULL REFERENCES resume (uuid) ON DELETE CASCADE,
  type TEXT        NOT NULL,
  name TEXT,
  url  TEXT
);

CREATE UNIQUE INDEX contact_uuid_type_index
  ON contact (uuid, type);

--- [ section ] --------------------------------------------------------------------------------------------------------

CREATE TABLE section
(
  id   SERIAL,
  uuid VARCHAR(36) NOT NULL REFERENCES resume (uuid) ON DELETE CASCADE,
  type TEXT        NOT NULL,
  data TEXT        NOT NULL
);

CREATE UNIQUE INDEX section_uuid_type_index
  ON section (uuid, type);
