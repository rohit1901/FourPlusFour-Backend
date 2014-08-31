CREATE TABLE `heroku_4265740aecd0c5d`.`maths` (
  `testLevel` INT NOT NULL,
  `question` VARCHAR(100) NULL,
  `answer` VARCHAR(100) NULL,
  PRIMARY KEY (`testLevel`))
COMMENT = 'maths test questions and answers table.';

ALTER TABLE `heroku_4265740aecd0c5d`.`maths` 
DROP PRIMARY KEY;