ALTER TABLE `madeinw`.`users_admin` 
ADD COLUMN `Role_ID` INT NULL AFTER `UserEmail`;

CREATE TABLE `madeinw`.`user_role` (
  `Role_ID` INT NOT NULL,
  `Role_NAME` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`Role_ID`));

CREATE TABLE `madeinw`.`admin_pages` (
  `Page_ID` INT NOT NULL,
  `Page_NAME` VARCHAR(255) NOT NULL,
  `Page_PATTERN` VARCHAR(255) NOT NULL,
  `Page_showInMenu` BIT(1) NOT NULL,
  PRIMARY KEY (`Page_ID`));

  
CREATE TABLE `madeinw`.`user_authentication` (
  `Role_ID` INT NOT NULL,
  `Page_ID` INT NOT NULL,
  PRIMARY KEY (`Role_ID`, `Page_ID`));

  
ALTER TABLE `madeinw`.`users_admin` 
ADD INDEX `FK_ROLE_ID_idx` (`Role_ID` ASC);
ALTER TABLE `madeinw`.`users_admin` 
ADD CONSTRAINT `FK_ROLE_ID`
  FOREIGN KEY (`Role_ID`)
  REFERENCES `madeinw`.`user_role` (`Role_ID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

  
ALTER TABLE `madeinw`.`user_authentication` 
ADD CONSTRAINT `FK_ROLE_ID_AUTH`
  FOREIGN KEY (`Role_ID`)
  REFERENCES `madeinw`.`user_role` (`Role_ID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `FK_PAGE_ID_AUTH`
  FOREIGN KEY (`Page_ID`)
  REFERENCES `madeinw`.`admin_pages` (`Page_ID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
  
INSERT INTO `madeinw`.`admin_pages` (`Page_ID`, `Page_NAME`, `Page_PATTERN`, `Page_showInMenu`) VALUES ('1', 'Login', '/admin/login' , b'0');
INSERT INTO `madeinw`.`admin_pages` (`Page_ID`, `Page_NAME`, `Page_PATTERN`, `Page_showInMenu`) VALUES ('2', 'My Dashboard', '/admin/dashboard' , b'0');
INSERT INTO `madeinw`.`admin_pages` (`Page_ID`, `Page_NAME`, `Page_PATTERN`, `Page_showInMenu`) VALUES ('3', 'Product Activation', '/admin/products/activate',b'1');
INSERT INTO `madeinw`.`admin_pages` (`Page_ID`, `Page_NAME`, `Page_PATTERN`, `Page_showInMenu`) VALUES ('4', 'Product Review', '/admin/products/review/(.*)',b'0');
INSERT INTO `madeinw`.`admin_pages` (`Page_ID`, `Page_NAME`, `Page_PATTERN`, `Page_showInMenu`) VALUES ('5', 'Product Edit', '/admin/products/edit/(.*)',b'0');
INSERT INTO `madeinw`.`admin_pages` (`Page_ID`, `Page_NAME`, `Page_PATTERN`, `Page_showInMenu`) VALUES ('6', 'Product Translation', '/admin/products/translate/(.*)',b'0');
INSERT INTO `madeinw`.`admin_pages` (`Page_ID`, `Page_NAME`, `Page_PATTERN`, `Page_showInMenu`) VALUES ('7', 'Translation Activation', '/admin/products/translationApprove/(.*)',b'0');
INSERT INTO `madeinw`.`admin_pages` (`Page_ID`, `Page_NAME`, `Page_PATTERN`, `Page_showInMenu`) VALUES ('8', 'Product Management', '/admin/products/managment',b'1');
INSERT INTO `madeinw`.`admin_pages` (`Page_ID`, `Page_NAME`, `Page_PATTERN`, `Page_showInMenu`) VALUES ('9', 'User Managment', '/admin/user/managment',b'1');
INSERT INTO `madeinw`.`admin_pages` (`Page_ID`, `Page_NAME`, `Page_PATTERN`, `Page_showInMenu`) VALUES ('10', 'Product Of The Day', '/admin/productOfTheDay',b'1');
INSERT INTO `madeinw`.`admin_pages` (`Page_ID`, `Page_NAME`, `Page_PATTERN`, `Page_showInMenu`) VALUES ('11', 'Product Translation', '/admin/translation/pending',b'1');
INSERT INTO `madeinw`.`admin_pages` (`Page_ID`, `Page_NAME`, `Page_PATTERN`, `Page_showInMenu`) VALUES ('12', 'Translation Activation', '/admin/translation/activation',b'1');
INSERT INTO `madeinw`.`admin_pages` (`Page_ID`, `Page_NAME`, `Page_PATTERN`, `Page_showInMenu`) VALUES ('13', 'Trader Info', '/admin/tradersInfo',b'1');


INSERT INTO `madeinw`.`user_role` (`Role_ID`, `Role_NAME`) VALUES ('1', 'SuperAdmin');
INSERT INTO `madeinw`.`user_role` (`Role_ID`, `Role_NAME`) VALUES ('2', 'Admin');
INSERT INTO `madeinw`.`user_role` (`Role_ID`, `Role_NAME`) VALUES ('3', 'Translator');



-- Super Admin (Role id = 1) has access to all pages
INSERT INTO `madeinw`.`user_authentication`(`Role_ID`,`Page_ID`) VALUES ('1','1');
INSERT INTO `madeinw`.`user_authentication`(`Role_ID`,`Page_ID`) VALUES ('1','2');
INSERT INTO `madeinw`.`user_authentication`(`Role_ID`,`Page_ID`) VALUES ('1','3');
INSERT INTO `madeinw`.`user_authentication`(`Role_ID`,`Page_ID`) VALUES ('1','4');
INSERT INTO `madeinw`.`user_authentication`(`Role_ID`,`Page_ID`) VALUES ('1','5');
INSERT INTO `madeinw`.`user_authentication`(`Role_ID`,`Page_ID`) VALUES ('1','6');
INSERT INTO `madeinw`.`user_authentication`(`Role_ID`,`Page_ID`) VALUES ('1','7');
INSERT INTO `madeinw`.`user_authentication`(`Role_ID`,`Page_ID`) VALUES ('1','8');
INSERT INTO `madeinw`.`user_authentication`(`Role_ID`,`Page_ID`) VALUES ('1','9');
INSERT INTO `madeinw`.`user_authentication`(`Role_ID`,`Page_ID`) VALUES ('1','10');
INSERT INTO `madeinw`.`user_authentication`(`Role_ID`,`Page_ID`) VALUES ('1','11');
INSERT INTO `madeinw`.`user_authentication`(`Role_ID`,`Page_ID`) VALUES ('1','12');
INSERT INTO `madeinw`.`user_authentication`(`Role_ID`,`Page_ID`) VALUES ('1','13');


-- Admin (Role Id = 2) has access to all pages except the Product Translation Page (pageid = 7)
INSERT INTO `madeinw`.`user_authentication`(`Role_ID`,`Page_ID`) VALUES ('2','1');
INSERT INTO `madeinw`.`user_authentication`(`Role_ID`,`Page_ID`) VALUES ('2','2');
INSERT INTO `madeinw`.`user_authentication`(`Role_ID`,`Page_ID`) VALUES ('2','3');
INSERT INTO `madeinw`.`user_authentication`(`Role_ID`,`Page_ID`) VALUES ('2','4');
INSERT INTO `madeinw`.`user_authentication`(`Role_ID`,`Page_ID`) VALUES ('2','5');
INSERT INTO `madeinw`.`user_authentication`(`Role_ID`,`Page_ID`) VALUES ('2','7');
INSERT INTO `madeinw`.`user_authentication`(`Role_ID`,`Page_ID`) VALUES ('2','8');
INSERT INTO `madeinw`.`user_authentication`(`Role_ID`,`Page_ID`) VALUES ('2','9');
INSERT INTO `madeinw`.`user_authentication`(`Role_ID`,`Page_ID`) VALUES ('2','10');
INSERT INTO `madeinw`.`user_authentication`(`Role_ID`,`Page_ID`) VALUES ('2','12');
INSERT INTO `madeinw`.`user_authentication`(`Role_ID`,`Page_ID`) VALUES ('2','13');

-- Translator (role id = 3) has access to only 3 pages (page id = 1,2,7)
INSERT INTO `madeinw`.`user_authentication`(`Role_ID`,`Page_ID`) VALUES ('3','1');
INSERT INTO `madeinw`.`user_authentication`(`Role_ID`,`Page_ID`) VALUES ('3','2');
INSERT INTO `madeinw`.`user_authentication`(`Role_ID`,`Page_ID`) VALUES ('3','6');
INSERT INTO `madeinw`.`user_authentication`(`Role_ID`,`Page_ID`) VALUES ('3','11');


INSERT INTO `madeinw`.`changelog`(`change_number`,`complete_dt`,`applied_by`,`description`) VALUES('14',NOW(),'madeinw','014_AddUserAuth.sql');
