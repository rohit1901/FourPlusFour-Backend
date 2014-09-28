CREATE TABLE `heroku_4265740aecd0c5d`.`level` (
  `email` VARCHAR(50) NOT NULL,
  `testLevel` INT NULL,
  `learnLevel` INT NULL,
  `subject` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`email`));

ALTER TABLE `heroku_4265740aecd0c5d`.`level` 
DROP PRIMARY KEY;
