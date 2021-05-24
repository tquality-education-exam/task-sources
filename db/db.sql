DROP SCHEMA IF EXISTS union_reporting;
CREATE SCHEMA IF NOT EXISTS union_reporting;
USE union_reporting;
SET NAMES utf8;
-- Reference tables
CREATE TABLE IF NOT EXISTS status (
	id INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL COMMENT 'Status name (255 symbols)',
	PRIMARY KEY(id)
);
INSERT INTO status (name) VALUES ('PASSED');
INSERT INTO status (name) VALUES ('FAILED');
INSERT INTO status (name) VALUES ('SKIPPED');

-- Test project info
CREATE TABLE IF NOT EXISTS project (
	id BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(1000) NOT NULL COMMENT 'Project name (1000 symbols)',
	PRIMARY KEY(id),
	CONSTRAINT project_name_u UNIQUE(name)
);

-- Test author info
CREATE TABLE IF NOT EXISTS author (
	id BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(1000) NOT NULL COMMENT 'Author name',
	login VARCHAR(1000) NOT NULL COMMENT 'Author login',
	email VARCHAR(1000) NOT NULL COMMENT 'Author email',
	PRIMARY KEY(id),
	CONSTRAINT author_login_u UNIQUE(login)
);

-- Current session for test execution (for group entries in `test` table by executions)
CREATE TABLE IF NOT EXISTS session (
	id BIGINT NOT NULL AUTO_INCREMENT,
	session_key VARCHAR(1000) NOT NULL COMMENT 'Session key of current test running',
	created_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Test start time',
	build_number BIGINT NOT NULL COMMENT 'Build number',
	PRIMARY KEY(id)
);

-- Test execution info
CREATE TABLE IF NOT EXISTS test (
	id BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(10000) NOT NULL COMMENT 'Test name (10000 symbols)',
	status_id INT(1) COMMENT 'Test execution status (status.id)',
	method_name VARCHAR(10000) NOT NULL COMMENT 'Test method name (10000 symbols)',
	project_id BIGINT NOT NULL COMMENT 'Project ID (project.id)',
	session_id BIGINT NOT NULL COMMENT 'ID of current test execution session (session.id)',
	start_time TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Test start time',
	end_time TIMESTAMP NULL COMMENT 'Test end time',
	env VARCHAR(255) NOT NULL COMMENT 'Node name where tests are executed (255 symbols)',
	browser VARCHAR(255) NULL DEFAULT '' COMMENT 'Browser used for tests execution (255 symbols)',
	author_id BIGINT NULL COMMENT 'Author info ID (author.id)',
	PRIMARY KEY(id),
	FOREIGN KEY (project_id) REFERENCES project (id) ON DELETE CASCADE,
	FOREIGN KEY (session_id) REFERENCES session (id) ON DELETE CASCADE,
	FOREIGN KEY (author_id) REFERENCES author (id) ON DELETE SET NULL,
	FOREIGN KEY (status_id) REFERENCES status (id) ON DELETE RESTRICT
);

-- Test execution attachments (screenshots, page sources, etc...)
CREATE TABLE IF NOT EXISTS attachment (
	id BIGINT NOT NULL AUTO_INCREMENT,
	content LONGBLOB NOT NULL COMMENT 'Content in base64',
	content_type VARCHAR(255) NOT NULL COMMENT 'Content type (255 symbols)',
	test_id BIGINT NOT NULL COMMENT 'Test ID (test.id)',
	PRIMARY KEY (id),
	FOREIGN KEY (test_id) REFERENCES test (id) ON DELETE CASCADE
);

-- Test execution logs
CREATE TABLE IF NOT EXISTS log (
	id BIGINT NOT NULL AUTO_INCREMENT,
	content LONGTEXT  NOT NULL COMMENT 'Logs of current test',
	is_exception BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Is current log test exception?',
	test_id BIGINT NOT NULL COMMENT 'Test ID (test.id)',
	PRIMARY KEY(id),
	FOREIGN KEY (test_id) REFERENCES test (id) ON DELETE CASCADE
);

-- Table with auth keys of systems which able to calling API
CREATE TABLE IF NOT EXISTS api_key (
	id BIGINT NOT NULL AUTO_INCREMENT,
	`key` VARCHAR(255) NOT NULL COMMENT 'API key which available for writing test info',
	key_info VARCHAR(10000) NOT NULL COMMENT 'Key info (external resource name project name, whatever)',
	PRIMARY KEY(id)
);

-- Test development info
CREATE TABLE IF NOT EXISTS dev_info (
	id BIGINT NOT NULL AUTO_INCREMENT,
	dev_time REAL COMMENT 'Test development time',
	test_id BIGINT NOT NULL COMMENT 'Test ID (test.id)',
	PRIMARY KEY(id),
	FOREIGN KEY (test_id) REFERENCES test (id) ON DELETE NO ACTION
);

-- Fail reason
CREATE TABLE IF NOT EXISTS fail_reason (
	id BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(1000) NOT NULL COMMENT 'Fail reason name (1000 symbols)',
	is_global BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Is current reason global for all projects?',
	is_unremovable BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Is current reason cant be deleted?',
	is_unchangeable BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Is current reason cant be changed to other reason?',
	is_stats_ignored BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Is current reason will be ignored in statistic count?',
	is_session BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Is current reason available for session?',
	is_test BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Is current reason available for test?',
	CONSTRAINT fail_reason_name_u UNIQUE(name),
	PRIMARY KEY(id)
);
INSERT INTO fail_reason (name, is_global, is_unremovable, is_unchangeable, is_stats_ignored, is_session, is_test) VALUES ('Debug', TRUE, TRUE, TRUE, TRUE, TRUE, TRUE);
INSERT INTO fail_reason (name, is_global, is_unremovable, is_unchangeable, is_stats_ignored, is_session, is_test) VALUES ('Bug', TRUE, TRUE, FALSE, FALSE, TRUE, TRUE);

-- Relation table for test and fail reason
CREATE TABLE IF NOT EXISTS rel_fail_reason_test (
	id BIGINT NOT NULL AUTO_INCREMENT,
	fail_reason_id BIGINT NOT NULL COMMENT 'Fail reason ID (fail_reason.id)',
	test_id BIGINT NOT NULL COMMENT 'Test ID (test.id)',
	comment VARCHAR(10000) COMMENT 'Fail reason comment (10000 symbols)',
	FOREIGN KEY (fail_reason_id) REFERENCES fail_reason (id) ON DELETE NO ACTION,
	FOREIGN KEY (test_id) REFERENCES test (id) ON DELETE NO ACTION,
	PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS `variant` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `is_dynamic_version_in_footer` tinyint(1) NOT NULL DEFAULT 0,
  `use_ajax_for_tests_page` tinyint(1) NOT NULL DEFAULT 0,
  `use_frame_for_new_project` tinyint(1) NOT NULL DEFAULT 0,
  `use_new_tab_for_new_project` tinyint(1) NOT NULL DEFAULT 0,
  `use_geolocation_for_new_project` tinyint(1) NOT NULL DEFAULT 0,
  `use_alert_for_new_project` tinyint(1) NOT NULL DEFAULT 0,
  `grammar_mistake_on_save_project` tinyint(1) NOT NULL DEFAULT 0,
  `grammar_mistake_on_save_test` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `variant_id_uindex` (`id`)
);

INSERT INTO union_reporting.variant (is_dynamic_version_in_footer, use_ajax_for_tests_page, use_frame_for_new_project, use_new_tab_for_new_project, use_geolocation_for_new_project, use_alert_for_new_project, grammar_mistake_on_save_project, grammar_mistake_on_save_test) VALUES (1, 0, 1, 0, 0, 0, 1, 0);
INSERT INTO union_reporting.variant (is_dynamic_version_in_footer, use_ajax_for_tests_page, use_frame_for_new_project, use_new_tab_for_new_project, use_geolocation_for_new_project, use_alert_for_new_project, grammar_mistake_on_save_project, grammar_mistake_on_save_test) VALUES (0, 1, 0, 1, 0, 0, 0, 1);
INSERT INTO union_reporting.variant (is_dynamic_version_in_footer, use_ajax_for_tests_page, use_frame_for_new_project, use_new_tab_for_new_project, use_geolocation_for_new_project, use_alert_for_new_project, grammar_mistake_on_save_project, grammar_mistake_on_save_test) VALUES (1, 0, 0, 0, 1, 0, 1, 0);
INSERT INTO union_reporting.variant (is_dynamic_version_in_footer, use_ajax_for_tests_page, use_frame_for_new_project, use_new_tab_for_new_project, use_geolocation_for_new_project, use_alert_for_new_project, grammar_mistake_on_save_project, grammar_mistake_on_save_test) VALUES (0, 1, 0, 0, 0, 1, 1, 0);

CREATE TABLE IF NOT EXISTS `token` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `value` varchar(32) NOT NULL,
  `variant_id` int(11) NOT NULL,
  `creation_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `token_id_uindex` (`id`),
  UNIQUE KEY `token_value_uindex` (`value`),
  KEY `token_variant_id_fk` (`variant_id`),
  CONSTRAINT `token_variant_id_fk` FOREIGN KEY (`variant_id`) REFERENCES `variant` (`id`)
);

INSERT INTO union_reporting.token (`value`, `variant_id`, `creation_time`) VALUES ("var1", 1, "2028-10-10 00:00:00");
INSERT INTO union_reporting.token (`value`, `variant_id`, `creation_time`) VALUES ("var2", 2, "2028-10-10 00:00:00");
INSERT INTO union_reporting.token (`value`, `variant_id`, `creation_time`) VALUES ("var3", 3, "2028-10-10 00:00:00");
INSERT INTO union_reporting.token (`value`, `variant_id`, `creation_time`) VALUES ("var4", 4, "2028-10-10 00:00:00");

SET GLOBAL event_scheduler=1;

CREATE EVENT `delete old token`
  ON SCHEDULE EVERY 1 WEEK STARTS CURRENT_TIMESTAMP
  ON COMPLETION NOT PRESERVE
  ENABLE
  COMMENT 'delete old tokens'  DO
  DELETE FROM token WHERE creation_time <= now() -  INTERVAL 6 DAY;