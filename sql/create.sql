
  CREATE TABLE "DATADICTIONARY" 
   (	"ID" NUMBER(11,0) NOT NULL ENABLE, 
	"DATATYPESCODE" VARCHAR2(8) NOT NULL ENABLE, 
	"DATATYPESNAME" VARCHAR2(32) NOT NULL ENABLE, 
	"DATATYPECODE" VARCHAR2(8), 
	"DATATYPENAME" VARCHAR2(32) NOT NULL ENABLE, 
	"ISUSE" NUMBER(1,0) DEFAULT 1 NOT NULL ENABLE, 
	"MEMO" VARCHAR2(128), 
	"CREATETIME" DATE DEFAULT sysdate, 
	 CONSTRAINT "PK_DATADICTIONARY" PRIMARY KEY ("ID"));
  
 	CREATE TABLE "KNOWLEDGEPOINT" 
   (	"ID" NUMBER(11,0) NOT NULL ENABLE, 
	"NAME" VARCHAR2(128), 
	"MENU" VARCHAR2(1024), 
	"PAGELINK" VARCHAR2(128), 
	"JAVAPATH" VARCHAR2(128), 
	"POINTTYPE" NUMBER(11,0), 
	"CREATETIME" DATE DEFAULT sysdate, 
	"PICTURE" BLOB, 
	"ARTICLE" CLOB, 
	 CONSTRAINT "PK_KNOWLEDGEPOINT" PRIMARY KEY ("ID"));
	 
create sequence S_KNOWLEDGEPOINT_ID
minvalue 1000
maxvalue 999999999999999999999999999
start with 1000
increment by 1
cache 20;