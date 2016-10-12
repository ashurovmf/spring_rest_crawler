CREATE DATABASE loginauth WITH ENCODING 'UTF8';
GRANT ALL PRIVILEGES ON DATABASE loginauth to clnnode;

CREATE TABLE IF NOT EXISTS app_user (
  id SERIAL PRIMARY KEY,
  enabled smallint NOT NULL,
  username varchar(150) NOT NULL,
  password varchar(50) NOT NULL,
  email varchar(150) NOT NULL,
  provider varchar(50) NOT NULL,
  CONSTRAINT uniq_username UNIQUE (username)
) WITH (
    OIDS=FALSE
);
ALTER TABLE app_user OWNER TO clnnode;
GRANT ALL ON TABLE app_user TO clnnode;
GRANT ALL ON TABLE app_user TO public;

CREATE TABLE IF NOT EXISTS user_authority (
  id SERIAL PRIMARY KEY,
  user_id integer NOT NULL,
  authority varchar(50) NOT NULL,
  CONSTRAINT uniq_credent_auth UNIQUE (user_id, authority),
  CONSTRAINT fk_authorities_users FOREIGN KEY (user_id) REFERENCES app_user (id)
  MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
) WITH (
     OIDS=FALSE
);
ALTER TABLE user_authority OWNER TO clnnode;
GRANT ALL ON TABLE user_authority TO clnnode;
GRANT ALL ON TABLE user_authority TO public;

CREATE INDEX ind_user_authority
  ON user_authority
  USING btree
(user_id);

CREATE INDEX ind_user_name
  ON app_user
  USING btree
(username);

INSERT INTO app_user (username, password, enabled) VALUES
	('alex', '21a4ed0a0cf607e77e93bf7604e2bb1ad07757c5', 1),
	('bob', '904752ad9c4ae4186c4b4897321c517de0618702', 1);

INSERT INTO user_authority (user_id, authority) VALUES
	(1, 'ROLE_ADMIN'),
	(2, 'ROLE_USER');

CREATE TABLE persistent_logins (
	username VARCHAR(150) NOT NULL,
	series VARCHAR(64) NOT NULL PRIMARY KEY,
	token VARCHAR(64) NOT NULL,
	last_used TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) WITH (
    OIDS=FALSE
);
ALTER TABLE persistent_logins OWNER TO clnnode;
GRANT ALL ON TABLE persistent_logins TO clnnode;
GRANT ALL ON TABLE persistent_logins TO public;

CREATE TABLE oauth_access_token (
  token_id VARCHAR(256),
  token bytea,
  authentication_id VARCHAR(256),
  user_name VARCHAR(256),
  client_id VARCHAR(256),
  authentication bytea,
  refresh_token VARCHAR(256)
)  WITH (
    OIDS=FALSE
);
ALTER TABLE oauth_access_token OWNER TO clnnode;
GRANT ALL ON TABLE oauth_access_token TO clnnode;
GRANT ALL ON TABLE oauth_access_token TO public;

CREATE TABLE oauth_client_details (
  client_id VARCHAR(256) PRIMARY KEY,
  resource_ids VARCHAR(256),
  client_secret VARCHAR(256),
  scope VARCHAR(256),
  authorized_grant_types VARCHAR(256),
  web_server_redirect_uri VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additional_information VARCHAR(4096),
  autoapprove VARCHAR(256)
)  WITH (
    OIDS=FALSE
);
ALTER TABLE oauth_client_details OWNER TO clnnode;
GRANT ALL ON TABLE oauth_client_details TO clnnode;
GRANT ALL ON TABLE oauth_client_details TO public;

CREATE TABLE oauth_client_token (
  token_id VARCHAR(256),
  token bytea,
  authentication_id VARCHAR(256),
  user_name VARCHAR(256),
  client_id VARCHAR(256)
)  WITH (
    OIDS=FALSE
);
ALTER TABLE oauth_client_token OWNER TO clnnode;
GRANT ALL ON TABLE oauth_client_token TO clnnode;
GRANT ALL ON TABLE oauth_client_token TO public;

CREATE TABLE oauth_code (
  code VARCHAR(256),
  authentication bytea
)  WITH (
    OIDS=FALSE
);
ALTER TABLE oauth_code OWNER TO clnnode;
GRANT ALL ON TABLE oauth_code TO clnnode;
GRANT ALL ON TABLE oauth_code TO public;


CREATE TABLE oauth_refresh_token (
  token_id VARCHAR(256),
  token bytea,
  authentication bytea
)  WITH (
    OIDS=FALSE
);
ALTER TABLE oauth_refresh_token OWNER TO clnnode;
GRANT ALL ON TABLE oauth_refresh_token TO clnnode;
GRANT ALL ON TABLE oauth_refresh_token TO public;


CREATE TABLE oauth_approvals (
  userId VARCHAR(256),
  clientId VARCHAR(256),
  scope VARCHAR(256),
  status VARCHAR(10),
  expiresAt TIMESTAMP,
  lastModifiedAt TIMESTAMP
)  WITH (
    OIDS=FALSE
);
ALTER TABLE oauth_approvals OWNER TO clnnode;
GRANT ALL ON TABLE oauth_approvals TO clnnode;
GRANT ALL ON TABLE oauth_approvals TO public;