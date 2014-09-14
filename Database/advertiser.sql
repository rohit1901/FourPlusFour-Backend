CREATE TABLE `heroku_4265740aecd0c5d`.`advertiser` (
  `name` VARCHAR(50) NOT NULL,
  `company` VARCHAR(50) NOT NULL,
  `email` VARCHAR(50) NOT NULL,
  `products` VARCHAR(200) NOT NULL,
  `plan` VARCHAR(50) NOT NULL,
  `bio` VARCHAR(200) NOT NULL,
  PRIMARY KEY (`email`))
COMMENT = 'advertiser profile table.';

ALTER TABLE `heroku_4265740aecd0c5d`.`advertiser` 
ADD COLUMN `password` VARCHAR(50) NOT NULL AFTER `bio`;