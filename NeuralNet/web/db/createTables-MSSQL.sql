--
--  Copyright (C) 2013 Plush Pay Software. All rights reserved.
--  @author Terry Packer
--

-- TODO THIS IS NOT MS-SQL it is MySQL

-- Make sure that everything get created with utf8 as the charset.
alter database default character set utf8;

create table neuralNetworks (
  id int not null auto_increment,
  xid varchar(50) not null,
  enabled char(1) not null,
  name varchar(40) not null,
) engine=InnoDB;
