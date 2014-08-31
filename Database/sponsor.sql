CREATE TABLE `heroku_4265740aecd0c5d`.`sponsor` (
  `name` VARCHAR(50) NOT NULL,
  `email` VARCHAR(50) NOT NULL,
  `bio` VARCHAR(200) NOT NULL,
  `amount` INT NOT NULL,
  PRIMARY KEY (`email`))
COMMENT = 'sponsor profile table.';