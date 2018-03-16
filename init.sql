SET GLOBAL time_zone = '+0:00';
CREATE SCHEMA runcity DEFAULT CHARACTER SET utf8;

CREATE TABLE runcity.blob_content (
  id INT(11) NOT NULL,
  content LONGBLOB NOT NULL,
  PRIMARY KEY (id))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE runcity.category (
  id INT(11) NOT NULL,
  bgcolor VARCHAR(6) NOT NULL,
  color VARCHAR(6) NOT NULL,
  prefix VARCHAR(6) NULL DEFAULT NULL,
  PRIMARY KEY (id))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE runcity.consumer (
  id INT(11) NOT NULL,
  is_active BIT(1) NOT NULL,
  credentials VARCHAR(32) NOT NULL,
  email VARCHAR(255) NOT NULL,
  locale VARCHAR(16) NULL DEFAULT NULL,
  passhash LONGTEXT NULL DEFAULT NULL,
  username VARCHAR(32) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE INDEX consumer_email (email ASC),
  UNIQUE INDEX consumer_username (username ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE runcity.consumer_role (
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

CREATE TABLE runcity.game (
  id INT(11) NOT NULL,
  city VARCHAR(32) NOT NULL,
  country VARCHAR(32) NOT NULL,
  timezone VARCHAR(32) NOT NULL,
  date_from DATETIME NOT NULL,
  date_to DATETIME NOT NULL,
  locale VARCHAR(32) NOT NULL,
  name VARCHAR(32) NOT NULL,
  delay INT(11) DEFAULT NULL,
  PRIMARY KEY (id),
  INDEX game_date (date_from DESC, date_to DESC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE runcity.control_point (
  id INT(11) NOT NULL,
  description LONGTEXT NOT NULL,
  idt VARCHAR(16) NOT NULL,
  image INT(18) DEFAULT NULL,
  name VARCHAR(255) DEFAULT NULL,
  mode VARCHAR(1) NOT NULL,
  type VARCHAR(1) NOT NULL,
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

CREATE TABLE runcity.route (
  id INT(11) NOT NULL,
  game__id INT(11) NOT NULL,
  category__id INT(11) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE INDEX route_game (game__id ASC, category__id ASC),
  CONSTRAINT FK_route_category
    FOREIGN KEY (category__id)
    REFERENCES runcity.category (id),
  CONSTRAINT FK_route_game
    FOREIGN KEY (game__id)
    REFERENCES runcity.game (id))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE runcity.route_item (
  id int(11) NOT NULL,
  control_point__id int(11) NOT NULL,
  route__id int(11) NOT NULL,
  leg_num int(11) DEFAULT NULL,
  PRIMARY KEY (id),
  INDEX route_item_route (route__id ASC),
  INDEX route_item_control_point (control_point__id ASC),
  CONSTRAINT FK_route_item_route FOREIGN KEY (route__id) REFERENCES route (id),
  CONSTRAINT FK_route_item_control_point FOREIGN KEY (control_point__id) REFERENCES control_point (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE runcity.persistent_logins (
  token VARCHAR(64) NOT NULL,
  last_used DATETIME NOT NULL,
  series VARCHAR(64) NOT NULL,
  username VARCHAR(64) NOT NULL,
  PRIMARY KEY (token))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE runcity.token (
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

CREATE TABLE runcity.translation (
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

CREATE TABLE runcity.volunteer (
  id INT(11) NOT NULL,
  consumer__id INT(11) NOT NULL,
  control_point__id INT(11) NULL,
  game__id INT(11) NULL,
  date_from DATETIME NOT NULL,
  date_to DATETIME NOT NULL,
  PRIMARY KEY (id),
  INDEX volunteer_date (date_from DESC, date_to DESC),
  INDEX volunteer_consumer (consumer__id ASC),
  CONSTRAINT FK_volunteer_consumer
    FOREIGN KEY (consumer__id)
    REFERENCES runcity.consumer (id),
  INDEX volunteer_control_point (control_point__id ASC),
  CONSTRAINT FK_volunteer_control_point
    FOREIGN KEY (control_point__id)
    REFERENCES runcity.control_point (id),
  INDEX volunteer_game (game__id ASC),
  CONSTRAINT FK_volunteer_game
    FOREIGN KEY (game__id)
    REFERENCES runcity.game (id))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE runcity.team (
  id INT(11) NOT NULL,
  route__id INT(11) NOT NULL,
  team_number VARCHAR(32) NOT NULL,
  name VARCHAR(255) NOT NULL,
  start_date DATETIME NOT NULL,
  contact VARCHAR(255) NOT NULL,
  add_data VARCHAR(4000) DEFAULT NULL,
  status VARCHAR(2) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE INDEX team_number (team_number ASC, route__id ASC),
  INDEX team_route (route__id ASC),
  CONSTRAINT FK_team_route
    FOREIGN KEY (route__id)
    REFERENCES runcity.route (id))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE runcity.event (
  id INT(11) NOT NULL,
  type VARCHAR(1) NOT NULL,
  status VARCHAR(1) NOT NULL,
  date_from DATETIME NOT NULL,
  date_to DATETIME DEFAULT NULL,
  volunteer__id INT(11) NOT NULL,
  team__id INT(11) DEFAULT NULL,
  PRIMARY KEY (id),
  INDEX event_volunteer (volunteer__id ASC, type ASC, status ASC),
  CONSTRAINT FK_event_volunteer
    FOREIGN KEY (volunteer__id)
    REFERENCES runcity.volunteer (id),
  INDEX event_team (team__id ASC, type ASC, status ASC),
  CONSTRAINT FK_event_team
    FOREIGN KEY (team__id)
    REFERENCES runcity.team (id))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

insert into runcity.consumer(id, username, credentials, email, is_active, passhash) values (1, 'admin', 'Administrator', 'admin@runcity.org', 1, '$2a$10$mVfrrJmajq8eud2fKg4UrupgG/FoPMin1Vk067IHqKaSxyvQWzpiG');
insert into runcity.consumer_role(id, code, consumer__id) values (1, 'ADMIN', 1);