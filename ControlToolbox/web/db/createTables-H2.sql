--
--  Copyright (C) 2013 PlushPay Software. All rights reserved.
--  @author Terry Packer
--

create table controlControllers (
  id int not null auto_increment,
  xid varchar(50) not null,
  enabled char(1) not null,
  name varchar(40) not null,
  algorithmId int not null,
  primary key (id)
);

alter table controlControllers add constraint controlControllersUn1 unique (xid);


create table controlPoints (
  id int not null auto_increment,
  xid varchar(50) not null,
  enabled char(1) not null,
  name varchar(40) not null,
  pointType int not null,
  delay int not null,
  dataPointId int not null,
  controllerId int not null,
  highLimit double not null,
  lowLimit double not null,
  primary key (id)
);

alter table controlPoints add constraint controlPointsUn1 unique (xid);
alter table controlPoints add constraint controlPointsFk1 foreign key (dataPointId) references dataPoints(id);
alter table controlPoints add constraint controlPointsFk2 foreign key (controllerId) references controlControllers(id);

create table controlAlgorithms (
  id int not null auto_increment,
  xid varchar(50) not null,
  name varchar(40) not null,
  algorithmType int not null,
  data blob not null,
  primary key(id)
);


alter table controlAlgorithms add constraint controlAlgorithmsUn1 unique (xid);
