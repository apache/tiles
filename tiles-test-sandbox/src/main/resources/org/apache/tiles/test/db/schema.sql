
CREATE TABLE public.Definition (
                id NUMERIC(12,0) NOT NULL,
                name VARCHAR(255) NOT NULL,
                preparer VARCHAR(1000) NULL,
                template VARCHAR(1000) NULL,
                Customization_id NUMERIC(12,0) NOT NULL,
                parent_name VARCHAR(255) NULL,
                CONSTRAINT Definition_pk PRIMARY KEY (id)
);

CREATE INDEX public.Definition_idx
 ON public.Definition
 ( name ASC );

CREATE TABLE public.Attribute (
                id NUMERIC(12,0) NOT NULL,
                name VARCHAR(255) NOT NULL,
                type VARCHAR(255) NULL,
                value VARCHAR(1000) NOT NULL,
                cascade_attribute BOOLEAN NOT NULL,
                Definition_id NUMERIC(12,0) NULL,
                Parent_id NUMERIC(12,0) NULL,
                CONSTRAINT Attribute_pk PRIMARY KEY (id)
);

CREATE TABLE public.Role (
                id NUMERIC(12,0) NOT NULL,
                name VARCHAR(255) NOT NULL,
                CONSTRAINT Role_pk PRIMARY KEY (id)
);

CREATE INDEX public.Role_idx
 ON public.Role
 ( name ASC );

CREATE TABLE public.Customization (
                id NUMERIC(12,0) NOT NULL,
                Parent_id NUMERIC(12,0) NULL,
                name VARCHAR(255) NOT NULL,
                CONSTRAINT Customization_pk PRIMARY KEY (id)
);

CREATE INDEX public.Customization_idx
 ON public.Customization
 ( name ASC );

CREATE INDEX public.Customization_idx1
 ON public.Customization
 ( name ASC );

CREATE TABLE public.Visible_for (
                Definition_id NUMERIC(12,0) NOT NULL,
                Role_id NUMERIC(12,0) NOT NULL,
                CONSTRAINT Visible_for_pk PRIMARY KEY (Definition_id, Role_id)
);

CREATE TABLE public.Attribute_visible_for (
                Attribute_id NUMERIC(12,0) NOT NULL,
                Role_id NUMERIC(12,0) NOT NULL,
                CONSTRAINT Attribute_visible_for_pk PRIMARY KEY (Attribute_id, Role_id)
);


ALTER TABLE public.Attribute ADD CONSTRAINT Definition_Attribute_fk
FOREIGN KEY (Definition_id)
REFERENCES public.Definition (id)
;


ALTER TABLE public.Visible_for ADD CONSTRAINT Definition_Visible_for_fk
FOREIGN KEY (Definition_id)
REFERENCES public.Definition (id)
;


ALTER TABLE public.Attribute ADD CONSTRAINT Attribute_Attribute_fk
FOREIGN KEY (Parent_id)
REFERENCES public.Attribute (id)
;


ALTER TABLE public.Attribute_visible_for ADD CONSTRAINT Attribute_Attribute_visible_for_fk
FOREIGN KEY (Attribute_id)
REFERENCES public.Attribute (id)
;


ALTER TABLE public.Visible_for ADD CONSTRAINT Role_Visible_for_fk
FOREIGN KEY (Role_id)
REFERENCES public.Role (id)
;


ALTER TABLE public.Attribute_visible_for ADD CONSTRAINT Role_Attribute_visible_for_fk
FOREIGN KEY (Role_id)
REFERENCES public.Role (id)
;


ALTER TABLE public.Definition ADD CONSTRAINT Customization_Definition_fk
FOREIGN KEY (Customization_id)
REFERENCES public.Customization (id)
;


ALTER TABLE public.Customization ADD CONSTRAINT Customization_Customization_fk
FOREIGN KEY (Parent_id)
REFERENCES public.Customization (id)
;

INSERT INTO "PUBLIC"."CUSTOMIZATION" (ID,PARENT_ID,NAME) VALUES (0,null,'');
INSERT INTO "PUBLIC"."CUSTOMIZATION" (ID,PARENT_ID,NAME) VALUES (1,0,'en');
INSERT INTO "PUBLIC"."CUSTOMIZATION" (ID,PARENT_ID,NAME) VALUES (2,1,'en_GB');
INSERT INTO "PUBLIC"."CUSTOMIZATION" (ID,PARENT_ID,NAME) VALUES (3,1,'en_US');
INSERT INTO "PUBLIC"."CUSTOMIZATION" (ID,PARENT_ID,NAME) VALUES (4,0,'fr');
INSERT INTO "PUBLIC"."CUSTOMIZATION" (ID,PARENT_ID,NAME) VALUES (5,0,'it');

INSERT INTO "PUBLIC"."DEFINITION" (ID,PARENT_NAME,NAME,PREPARER,TEMPLATE,CUSTOMIZATION_ID) VALUES (0,null,'test.localized.definition',null,'/layout.jsp',0);
INSERT INTO "PUBLIC"."DEFINITION" (ID,PARENT_NAME,NAME,PREPARER,TEMPLATE,CUSTOMIZATION_ID) VALUES (2,null,'test.localized.definition',null,'/layout.jsp',2);
INSERT INTO "PUBLIC"."DEFINITION" (ID,PARENT_NAME,NAME,PREPARER,TEMPLATE,CUSTOMIZATION_ID) VALUES (3,null,'test.localized.definition',null,'/layout.jsp',3);
INSERT INTO "PUBLIC"."DEFINITION" (ID,PARENT_NAME,NAME,PREPARER,TEMPLATE,CUSTOMIZATION_ID) VALUES (4,null,'test.localized.definition',null,'/layout.jsp',4);
INSERT INTO "PUBLIC"."DEFINITION" (ID,PARENT_NAME,NAME,PREPARER,TEMPLATE,CUSTOMIZATION_ID) VALUES (5,null,'test.localized.definition',null,'/layout.jsp',5);

INSERT INTO "PUBLIC"."ATTRIBUTE" (ID,NAME,TYPE,VALUE,CASCADE_ATTRIBUTE,DEFINITION_ID,PARENT_ID) VALUES (0,'title',null,'Default locale',0,0,null);
INSERT INTO "PUBLIC"."ATTRIBUTE" (ID,NAME,TYPE,VALUE,CASCADE_ATTRIBUTE,DEFINITION_ID,PARENT_ID) VALUES (1,'header',null,'/header.jsp',0,0,null);
INSERT INTO "PUBLIC"."ATTRIBUTE" (ID,NAME,TYPE,VALUE,CASCADE_ATTRIBUTE,DEFINITION_ID,PARENT_ID) VALUES (2,'body',null,'/defaultlocale_db.jsp',0,0,null);
INSERT INTO "PUBLIC"."ATTRIBUTE" (ID,NAME,TYPE,VALUE,CASCADE_ATTRIBUTE,DEFINITION_ID,PARENT_ID) VALUES (3,'title',null,'British English locale',0,2,null);
INSERT INTO "PUBLIC"."ATTRIBUTE" (ID,NAME,TYPE,VALUE,CASCADE_ATTRIBUTE,DEFINITION_ID,PARENT_ID) VALUES (4,'header',null,'/header.jsp',0,2,null);
INSERT INTO "PUBLIC"."ATTRIBUTE" (ID,NAME,TYPE,VALUE,CASCADE_ATTRIBUTE,DEFINITION_ID,PARENT_ID) VALUES (5,'body',null,'/defaultlocale_db.jsp',0,2,null);
INSERT INTO "PUBLIC"."ATTRIBUTE" (ID,NAME,TYPE,VALUE,CASCADE_ATTRIBUTE,DEFINITION_ID,PARENT_ID) VALUES (6,'title',null,'American English locale',0,3,null);
INSERT INTO "PUBLIC"."ATTRIBUTE" (ID,NAME,TYPE,VALUE,CASCADE_ATTRIBUTE,DEFINITION_ID,PARENT_ID) VALUES (7,'header',null,'/header.jsp',0,3,null);
INSERT INTO "PUBLIC"."ATTRIBUTE" (ID,NAME,TYPE,VALUE,CASCADE_ATTRIBUTE,DEFINITION_ID,PARENT_ID) VALUES (8,'body',null,'/defaultlocale_db.jsp',0,3,null);
INSERT INTO "PUBLIC"."ATTRIBUTE" (ID,NAME,TYPE,VALUE,CASCADE_ATTRIBUTE,DEFINITION_ID,PARENT_ID) VALUES (9,'title',null,'French locale',0,4,null);
INSERT INTO "PUBLIC"."ATTRIBUTE" (ID,NAME,TYPE,VALUE,CASCADE_ATTRIBUTE,DEFINITION_ID,PARENT_ID) VALUES (10,'header',null,'/header.jsp',0,4,null);
INSERT INTO "PUBLIC"."ATTRIBUTE" (ID,NAME,TYPE,VALUE,CASCADE_ATTRIBUTE,DEFINITION_ID,PARENT_ID) VALUES (11,'body',null,'/defaultlocale_db.jsp',0,4,null);
INSERT INTO "PUBLIC"."ATTRIBUTE" (ID,NAME,TYPE,VALUE,CASCADE_ATTRIBUTE,DEFINITION_ID,PARENT_ID) VALUES (12,'title',null,'Italian locale',0,5,null);
INSERT INTO "PUBLIC"."ATTRIBUTE" (ID,NAME,TYPE,VALUE,CASCADE_ATTRIBUTE,DEFINITION_ID,PARENT_ID) VALUES (13,'header',null,'/header.jsp',0,5,null);
INSERT INTO "PUBLIC"."ATTRIBUTE" (ID,NAME,TYPE,VALUE,CASCADE_ATTRIBUTE,DEFINITION_ID,PARENT_ID) VALUES (14,'body',null,'/defaultlocale_db.jsp',0,5,null);

INSERT INTO "PUBLIC"."DEFINITION" (ID,PARENT_NAME,NAME,PREPARER,TEMPLATE,CUSTOMIZATION_ID) VALUES (6,null,'test.definition',null,'/layout.jsp',0);
INSERT INTO "PUBLIC"."ATTRIBUTE" (ID,NAME,TYPE,VALUE,CASCADE_ATTRIBUTE,DEFINITION_ID,PARENT_ID) VALUES (15,'title',null,'This is the title.',0,6,null);
INSERT INTO "PUBLIC"."ATTRIBUTE" (ID,NAME,TYPE,VALUE,CASCADE_ATTRIBUTE,DEFINITION_ID,PARENT_ID) VALUES (16,'header',null,'/header.jsp',0,6,null);
INSERT INTO "PUBLIC"."ATTRIBUTE" (ID,NAME,TYPE,VALUE,CASCADE_ATTRIBUTE,DEFINITION_ID,PARENT_ID) VALUES (17,'body',null,'/body.jsp',0,6,null);
INSERT INTO "PUBLIC"."DEFINITION" (ID,PARENT_NAME,NAME,PREPARER,TEMPLATE,CUSTOMIZATION_ID) VALUES (7,'test.definition','test.definition.extended',null,null,0);
INSERT INTO "PUBLIC"."ATTRIBUTE" (ID,NAME,TYPE,VALUE,CASCADE_ATTRIBUTE,DEFINITION_ID,PARENT_ID) VALUES (18,'title',null,'This is an extended definition.',0,7,null);
