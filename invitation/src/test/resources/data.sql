
DROP TABLE IF EXISTS MEMBER;



CREATE TABLE MEMBER
(
  MEMBER_ID varchar(100) PRIMARY Key not null ,
  MEMBER_NAME varchar(100) not null ,
  MEMBER_PHONE varchar(20) not null ,
  MEMBER_EMAIL varchar (100) not null ,
  MEMBER_STATUS varchar (20),
  INVITE_CODE varchar (50),
  INVITE_CODE_STATUS varchar (20),
  CREATED_DATE TIMESTAMP,
  UDATED_DATE TIMESTAMP

);

