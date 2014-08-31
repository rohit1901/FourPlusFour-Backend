CREATE TABLE `heroku_4265740aecd0c5d`.`music` (
  `testLevel` int(11) NOT NULL,
  `question` varchar(100) DEFAULT NULL,
  `answer` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`testLevel`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='music test questions and answers table.';

ALTER TABLE `heroku_4265740aecd0c5d`.`music` 
DROP PRIMARY KEY;