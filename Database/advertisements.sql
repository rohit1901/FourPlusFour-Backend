CREATE TABLE `heroku_4265740aecd0c5d`.`advertisements` (
  `email` VARCHAR(50) NOT NULL,
  `date` DATE NOT NULL,
  `plan` VARCHAR(50) NOT NULL,
  `product` VARCHAR(50) NOT NULL,
  `usedAt` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`email`));

  ALTER TABLE `heroku_4265740aecd0c5d`.`advertisements` 
CHANGE COLUMN `date` `date` VARCHAR(50) NOT NULL ;

ALTER TABLE `heroku_4265740aecd0c5d`.`advertisements` 
DROP PRIMARY KEY;
