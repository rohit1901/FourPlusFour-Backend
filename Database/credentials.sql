CREATE TABLE `heroku_4265740aecd0c5d`.`credentials` (
  `username` VARCHAR(50) NOT NULL,
  `password` VARCHAR(50) NOT NULL,
  `type` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`username`))
COMMENT = 'user credentials along with type (role) of the user.';
