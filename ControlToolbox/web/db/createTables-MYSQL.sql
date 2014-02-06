--
--  Copyright (C) 2013 PlushPay Software. All rights reserved.
--  @author Terry Packer
--

-- Make sure that everything get created with utf8 as the charset.
alter database default character set utf8;

create table neuralNetworks (
  id int not null auto_increment,
  xid varchar(50) not null,
  enabled char(1) not null,
  name varchar(40) not null,
  transferFunctionType int not null,
  learningRate double not null,
  maxError double not null,
  learningMaxIterations int not null,
  trainingPeriodStart bigint not null,
  trainingPeriodEnd bigint not null,
  properties varchar(200) not null,
  primary key (id)
) engine=InnoDB;

alter table neuralNetworks add constraint neuralNetworksUn1 unique (xid);

create table neuralPoints (
  id int not null auto_increment,
  xid varchar(50) not null,
  enabled char(1) not null,
  name varchar(40) not null,
  pointType int not null,
  delay int not null,
  dataPointId int not null,
  trainingDataPointId int not null,
  networkId int not null,
  primary key (id)
) engine=InnoDB;

alter table neuralPoints add constraint neuralPointsUn1 unique (xid);
alter table neuralPoints add constraint neuralPointsFk1 foreign key (dataPointId) references dataPoints(id);
alter table neuralPoints add constraint neuralPointsFk2 foreign key (networkId) references neuralNetworks(id);

create table neuralHiddenLayers (
  id int not null auto_increment,
  xid varchar(50) not null,
  name varchar(40) not null,
  layerNumber int not null,
  numberOfNeurons int not null,
  networkId int not null,
  primary key (id)
) engine=InnoDB;

alter table neuralHiddenLayers add constraint neuralHiddenLayersUn1 unique (xid);
alter table neuralHiddenLayers add constraint neuralHiddenLayersFk2 foreign key (networkId) references neuralNetworks(id);

