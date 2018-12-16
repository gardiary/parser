CREATE SCHEMA parser ;

CREATE TABLE parser.log (
  id INT NOT NULL AUTO_INCREMENT,
  date_time DATETIME,
  ip VARCHAR(20),
  request VARCHAR(32),
  status INT,
  user_agent VARCHAR(320),
  job_id VARCHAR(100),
  PRIMARY KEY (id));

CREATE TABLE parser.blocked_ip (
  id INT NOT NULL AUTO_INCREMENT,
  created_date DATETIME NULL,
  ip VARCHAR(20) NOT NULL,
  count INT NULL,
  start_date DATETIME NULL,
  end_date DATETIME NULL,
  threshold INT NULL,
  reason VARCHAR(64) NULL,
  PRIMARY KEY (id));


