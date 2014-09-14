CREATE TABLE `heroku_4265740aecd0c5d`.`child` (
  `name` VARCHAR(50) NOT NULL,
  `age` INT NOT NULL,
  `school` VARCHAR(50) NOT NULL,
  `address` VARCHAR(50) NOT NULL,
  `email` VARCHAR(50) NOT NULL,
  `bio` VARCHAR(200) NOT NULL,
  `type` VARCHAR(5) NOT NULL,
  `testLevel` INT NULL,
  PRIMARY KEY (`email`))
COMMENT = 'Child profile table.';

ALTER TABLE `heroku_4265740aecd0c5d`.`child` 
CHANGE COLUMN `testLevel` `testLevel` INT(11) NOT NULL ,
ADD COLUMN `password` VARCHAR(50) NOT NULL AFTER `testLevel`;
