CREATE SCHEMA IF NOT EXISTS runcity DEFAULT CHARACTER SET utf8;

CREATE TABLE IF NOT EXISTS runcity.blob_content (
  id INT(11) NOT NULL,
  content LONGBLOB NOT NULL,
  PRIMARY KEY (id))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS runcity.category (
  id INT(11) NOT NULL,
  bgcolor VARCHAR(6) NOT NULL,
  color VARCHAR(6) NOT NULL,
  prefix VARCHAR(6) NULL DEFAULT NULL,
  PRIMARY KEY (id))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS runcity.consumer (
  id INT(11) NOT NULL,
  is_active BIT(1) NOT NULL,
  credentials VARCHAR(32) NOT NULL,
  email VARCHAR(255) NOT NULL,
  locale VARCHAR(16) NULL DEFAULT NULL,
  passhash LONGTEXT NOT NULL,
  username VARCHAR(32) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE INDEX consumer_email (email ASC),
  UNIQUE INDEX consumer_username (username ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS runcity.consumer_role (
  id INT(11) NOT NULL,
  code VARCHAR(32) NOT NULL,
  consumer__id INT(11) NOT NULL,
  PRIMARY KEY (id),
  INDEX consumer_role_consumer (consumer__id ASC),
  CONSTRAINT FK_consumer_role_consumer
    FOREIGN KEY (consumer__id)
    REFERENCES runcity.consumer (id))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS runcity.game (
  id INT(11) NOT NULL,
  city VARCHAR(32) NOT NULL,
  country VARCHAR(32) NOT NULL,
  game_date DATETIME NOT NULL,
  locale VARCHAR(32) NOT NULL,
  name VARCHAR(32) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE INDEX game_game_date (game_date ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS runcity.control_point (
  id INT(11) NOT NULL,
  description LONGTEXT NOT NULL,
  idt VARCHAR(16) NOT NULL,
  image INT(18) NULL DEFAULT NULL,
  name VARCHAR(255) NOT NULL,
  game__id INT(11) NOT NULL,
  control_point__id INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (id),
  INDEX control_point_game (game__id ASC),
  INDEX control_point_parent (control_point__id ASC),
  CONSTRAINT FK_control_point_game
    FOREIGN KEY (game__id)
    REFERENCES runcity.game (id),
  CONSTRAINT FK_control_point_parent
    FOREIGN KEY (control_point__id)
    REFERENCES runcity.control_point (id))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS runcity.game_category (
  game__id INT(11) NOT NULL,
  category__id INT(11) NOT NULL,
  PRIMARY KEY (game__id, category__id),
  INDEX game_category_category (category__id ASC),
  INDEX game_category_game (game__id ASC),
  CONSTRAINT FK_game_category_category
    FOREIGN KEY (category__id)
    REFERENCES runcity.category (id),
  CONSTRAINT FK_game_category_game
    FOREIGN KEY (game__id)
    REFERENCES runcity.game (id))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS runcity.persistent_logins (
  token VARCHAR(64) NOT NULL,
  last_used DATETIME NOT NULL,
  series VARCHAR(64) NOT NULL,
  username VARCHAR(64) NOT NULL,
  PRIMARY KEY (token))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS runcity.token (
  id INT(11) NOT NULL,
  date_from DATETIME NOT NULL,
  date_to DATETIME NOT NULL,
  token VARCHAR(32) NOT NULL,
  consumer__id INT(11) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE INDEX token_token (token ASC),
  INDEX token_consumer (consumer__id ASC),
  CONSTRAINT FK_token_consumer
    FOREIGN KEY (consumer__id)
    REFERENCES runcity.consumer (id))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS runcity.translation (
  id INT(11) NOT NULL,
  content VARCHAR(4000) NULL DEFAULT NULL,
  locale VARCHAR(32) NOT NULL,
  ref_column VARCHAR(32) NOT NULL,
  ref_record INT(11) NULL DEFAULT NULL,
  ref_table VARCHAR(32) NOT NULL,
  PRIMARY KEY (id),
  INDEX translation_ref (ref_record ASC, ref_column ASC, ref_table ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

insert into runcity.consumer(id, username, credentials, email, is_active, passhash) values (1, 'admin', 'Administrator', 'admin@runcity.org', 1, '$2a$10$mVfrrJmajq8eud2fKg4UrupgG/FoPMin1Vk067IHqKaSxyvQWzpiG');
insert into runcity.consumer_role(id, code, consumer__id) values (1, 'ADMIN', 1);

select * from runcity.consumer;
