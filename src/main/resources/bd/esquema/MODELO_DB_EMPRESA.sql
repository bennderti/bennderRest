/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     28-04-2017 0:28:00                           */
/*==============================================================*/
--TODO: CAMBIAR NOMBRE DE ESQUEMA A EMPRESA CORRESPONDIENTE

--CREATE SCHEMA BENNDER
  --AUTHORIZATION bennder;

--CAMBIANDO CONEXION A ESQUEMA DE EMPRESA;
--TODO: CAMBIAR NOMBRE DE ESQUEMA A EMPRESA CORRESPONDIENTE
--ET SEARCH_PATH TO BENNDER;

/*==============================================================*/
/* DBMS name:      PGSQL9                                       */
/* Created on:     03-03-2017 23:19:11                          */
/*==============================================================*/


drop table if exists ACCESO_USUARIO;

drop table if exists ACCION_USUARIO_BENEFICIO;

drop table if exists CARGO;

drop table if exists COMUNA;

drop table if exists CONTACTO;

drop table if exists DIRECCION;

drop table if exists ESTADO_USUARIO;

drop table if exists FECHA_ACCION_BENEFICIO;

drop table if exists INTERES_USUARIO;

drop table if exists PAIS;

drop table if exists PERFIL;

drop table if exists PERFIL_USUARIO;

drop table if exists PERMISO;

drop table if exists PERMISO_PERFIL;

drop table if exists RATING_REVIEW;

drop table if exists REGION;

drop table if exists TIPO_USUARIO_EMPRESA;

drop table if exists USUARIO;

drop table if exists USUARIO_BENEFICIO;

drop table if exists plantilla_correo;

drop table if exists parametro_sistema;

/*==============================================================*/
/* Table: ACCESO_USUARIO                                        */
/*==============================================================*/
create table ACCESO_USUARIO (
  id_usuario integer,
  fecha_acceso timestamp without time zone NOT NULL DEFAULT now()
);

/*==============================================================*/
/* Table: ACCION_USUARIO_BENEFICIO                              */
/*==============================================================*/
create table ACCION_USUARIO_BENEFICIO (
  ID_ACCION_BENEFICIO  INT4                 not null,
  NOMBRE               VARCHAR(15)             null,
  constraint PK_ACCION_USUARIO_BENEFICIO primary key (ID_ACCION_BENEFICIO)
);

/*==============================================================*/
/* Table: CARGO                                                 */
/*==============================================================*/
create table CARGO (
  ID_CARGO             SERIAL               not null,
  NOMBRE               VARCHAR(20)             not null,
  constraint PK_CARGO primary key (ID_CARGO)
);

/*==============================================================*/
/* Table: COMUNA                                                */
/*==============================================================*/
create table COMUNA (
  ID_COMUNA            INT4                 not null,
  ID_REGION            INT4                 null,
  NOMBRE               VARCHAR(50)             not null,
  constraint PK_COMUNA primary key (ID_COMUNA)
);

/*==============================================================*/
/* Table: CONTACTO                                              */
/*==============================================================*/
create table CONTACTO (
  ID_CONTACTO          SERIAL               not null,
  CELULAR              INT4                 null,
  TELEFONO_FIJO        INT4                 null,
  CORREO               VARCHAR(50)             null,
  constraint PK_CONTACTO primary key (ID_CONTACTO)
);

/*==============================================================*/
/* Table: DIRECCION                                             */
/*==============================================================*/
create table DIRECCION (
  ID_DIRECCION         SERIAL               not null,
  ID_COMUNA            INT4                 null,
  CALLE                VARCHAR(20)             not null,
  NUMERO               VARCHAR(10)             null,
  DEPARTAMENTO         VARCHAR(10)             null,
  VILLA                VARCHAR(20)             null,
  constraint PK_DIRECCION primary key (ID_DIRECCION)
);


/*==============================================================*/
/* Table: ESTADO_USUARIO                                        */
/*==============================================================*/
create table ESTADO_USUARIO (
  ID_ESTADO_USUARIO    INT4                 not null,
  NOMBRE               VARCHAR(15)             not null,
  constraint PK_ESTADO_USUARIO primary key (ID_ESTADO_USUARIO)
);

/*==============================================================*/
/* Table: FECHA_ACCION_BENEFICIO                                */
/*==============================================================*/
create table FECHA_ACCION_BENEFICIO (
  ID_USUARIO           INT4                 not null,
  ID_BENEFICIO         INT4                 not null,
  ID_ACCION_BENEFICIO  INT4                 not null,
  FECHA                timestamp                 not null,
  ID_VENDEDOR_POS INT4,
  ID_SUCURSAL_CANJE INT4
);

/*==============================================================*/
/* Table: INTERES_USUARIO                                       */
/*==============================================================*/
create table INTERES_USUARIO (
  ID_CATEGORIA         INT4                 not null,
  ID_USUARIO           INT4                 not null,
  constraint PK_INTERES_USUARIO primary key (ID_CATEGORIA, ID_USUARIO)
);

/*==============================================================*/
/* Table: PAIS                                                  */
/*==============================================================*/
create table PAIS (
  ID_PAIS              INT4                 not null,
  NOMBRE               VARCHAR(20)             not null,
  constraint PK_PAIS primary key (ID_PAIS)
);

/*==============================================================*/
/* Table: PERFIL                                                */
/*==============================================================*/
create table PERFIL (
  ID_PERFIL            INT4                 not null,
  NOMBRE               VARCHAR(20)             not null,
  HABILITADO           BOOL                 not null,
  constraint PK_PERFIL primary key (ID_PERFIL)
);

/*==============================================================*/
/* Table: PERFIL_USUARIO                                        */
/*==============================================================*/
create table PERFIL_USUARIO (
  ID_PERFIL            INT2                 not null,
  ID_USUARIO           INT4                 not null,
  constraint PK_PERFIL_USUARIO primary key (ID_PERFIL, ID_USUARIO)
);

/*==============================================================*/
/* Table: PERMISO                                               */
/*==============================================================*/
create table PERMISO (
  ID_PERMISO           INT4                 not null,
  NOMBRE               VARCHAR(20)             not null,
  HABILITADO           BOOL                 not null,
  constraint PK_PERMISO primary key (ID_PERMISO)
);

/*==============================================================*/
/* Table: PERMISO_PERFIL                                        */
/*==============================================================*/
create table PERMISO_PERFIL (
  ID_PERMISO           INT4                 not null,
  ID_PERFIL            INT4                 not null,
  constraint PK_PERMISO_PERFIL primary key (ID_PERMISO, ID_PERFIL)
);

/*==============================================================*/
/* Table: RATING_REVIEW                                         */
/*==============================================================*/
create table RATING_REVIEW (
  ID_BENEFICIO         INT4                 not null,
  ID_USUARIO           INT4                 not null,
  COMENTARIO           VARCHAR(50)             null,
  CALIFICACION         INT2                 not null,
  constraint PK_RATING_REVIEW primary key (ID_BENEFICIO, ID_USUARIO)
);

/*==============================================================*/
/* Table: REGION                                                */
/*==============================================================*/
create table REGION (
  ID_REGION            INT4                 not null,
  ID_PAIS              INT4                 null,
  NOMBRE               VARCHAR(50)             not null,
  constraint PK_REGION primary key (ID_REGION)
);

/*==============================================================*/
/* Table: TIPO_USUARIO_EMPRESA                                  */
/*==============================================================*/
create table TIPO_USUARIO_EMPRESA (
  ID_TIPO              INT4                 not null,
  NOMBRE               VARCHAR(15)             null,
  constraint PK_TIPO_USUARIO_EMPRESA primary key (ID_TIPO)
);

/*==============================================================*/
/* Table: USUARIO                                               */
/*==============================================================*/
create table USUARIO (
  ID_USUARIO           INT4                 not null,
  ID_CONTACTO          INT4                 not null,
  ID_DIRECCION         INT4                 null,
  ID_ESTADO_USUARIO    INT4                 null,
  PASSWORD             VARCHAR(100)         not null,
  USUARIO              VARCHAR(50)          not null,
  NOMBRES              VARCHAR(50)          not null,
  APELLIDO_P           VARCHAR(50)          not null,
  APELLIDO_M           VARCHAR(50)          not null,
  FECHA_NACIMIENTO     DATE                 not null,
  HABILITADO           BOOLEAN              not null DEFAULT false,
  ES_PASSWORD_TEMP           BOOLEAN        not null DEFAULT true,
  constraint PK_USUARIO primary key (ID_USUARIO)
);

/*==============================================================*/
/* Table: USUARIO_BENEFICIO                                     */
/*==============================================================*/
create table USUARIO_BENEFICIO (
  ID_BENEFICIO         INT4                 not null,
  ID_USUARIO           INT4                 not null,
  ID_ACCION_BENEFICIO  INT4                 null,
  CANTIDAD             INT4                 not null,
  CODIGO_BENEFICIO     VARCHAR(50)             null,
  CODIGO_BENEFICIO_ENCRIPTADO     VARCHAR(100)             null,
  constraint PK_USUARIO_BENEFICIO primary key (ID_BENEFICIO, ID_USUARIO)
);

/*==============================================================*/
/* Table: PLANTILLA_CORREO, PARA DEFINIR PLANTILLAS DE CORREO   */
/*==============================================================*/
CREATE TABLE plantilla_correo
(
  id_plantilla integer NOT NULL,
  nombre character varying(100) NOT NULL,
  asunto character varying(100) NOT NULL,
  descripcion character varying(100) NOT NULL,
  constraint pk_plantilla_correo primary key (id_plantilla)
);


/*==============================================================*/
/* Table: parametro_sistema UTILIZADAS PARA GUARDAR DATOS DE PARAMETROS DE SISTEMA */
/*==============================================================*/

CREATE TABLE parametro_sistema
(
  id_parametro_sistema serial NOT NULL,
  tipo_parametro varchar(30) NOT NULL,
  clave varchar(30) NOT NULL,
  valor_a varchar(100) NOT NULL,
  valor_b varchar(100) NOT NULL,
  valor_c varchar(100) NOT NULL,
  CONSTRAINT pk_param_sistema PRIMARY KEY (tipo_parametro, clave)
);

CREATE TABLE anuncio
(
  id_anuncio SERIAL NOT NULL,
  titulo VARCHAR(50),
  descripcion VARCHAR(200),
  fecha_inicial date NOT NULL,
  fecha_termino date NOT NULL,
  orden INT4 NOT NULL,
  habilitado boolean NOT NULL,
  link VARCHAR(200),
  imagen VARCHAR(20) NOT NULL,
  CONSTRAINT "Anuncio_pkey" PRIMARY KEY (id_anuncio)
);

COMMENT ON TABLE anuncio
IS 'Anuncios de Bennder para los trabajadores';

CREATE TABLE anuncio_empresa
(
  id_anuncio SERIAL NOT NULL,
  titulo VARCHAR(50),
  descripcion VARCHAR(200),
  fecha_inicial date NOT NULL,
  fecha_termino date NOT NULL,
  habilitado boolean NOT NULL,
  link VARCHAR(200),
  imagen VARCHAR(20) NOT NULL,
  orden INT4,
  CONSTRAINT anuncio_empresa_pkey PRIMARY KEY (id_anuncio)
);

COMMENT ON TABLE anuncio_empresa
IS 'Anuncios de la Empresa para los trabajadores';

/*==============================================================*/
/* FOREIGN KEYS */
/*==============================================================*/

alter table ACCESO_USUARIO
  add constraint FK_ACCESO_U_REFERENCE_USUARIO foreign key (ID_USUARIO)
references USUARIO (ID_USUARIO)
on delete restrict on update restrict;

alter table COMUNA
  add constraint FK_COMUNA_REFERENCE_REGION foreign key (ID_REGION)
references REGION (ID_REGION)
on delete restrict on update restrict;

alter table DIRECCION
  add constraint FK_DIRECCIO_REFERENCE_COMUNA foreign key (ID_COMUNA)
references COMUNA (ID_COMUNA)
on delete restrict on update restrict;

alter table FECHA_ACCION_BENEFICIO
  add constraint FK_FECHA_AC_REFERENCE_USUARIO_ foreign key (ID_BENEFICIO, ID_USUARIO)
references USUARIO_BENEFICIO (ID_BENEFICIO, ID_USUARIO)
on delete restrict on update restrict;

alter table FECHA_ACCION_BENEFICIO
  add constraint FK_FECHA_AC_REFERENCE_ACCION_U foreign key (ID_ACCION_BENEFICIO)
references ACCION_USUARIO_BENEFICIO (ID_ACCION_BENEFICIO)
on delete restrict on update restrict;

alter table INTERES_USUARIO
  add constraint FK_INTERES__REFERENCE_CATEGORI foreign key (ID_CATEGORIA)
references PROVEEDOR.CATEGORIA (ID_CATEGORIA)
on delete restrict on update restrict;

alter table INTERES_USUARIO
  add constraint FK_INTERES__REFERENCE_USUARIO foreign key (ID_USUARIO)
references USUARIO (ID_USUARIO)
on delete restrict on update restrict;

alter table PERFIL_USUARIO
  add constraint FK_PERFIL_U_REFERENCE_PERFIL foreign key (ID_PERFIL)
references PERFIL (ID_PERFIL)
on delete restrict on update restrict;

alter table PERFIL_USUARIO
  add constraint FK_PERFIL_U_REFERENCE_USUARIO foreign key (ID_USUARIO)
references USUARIO (ID_USUARIO)
on delete restrict on update restrict;

alter table PERMISO_PERFIL
  add constraint FK_PERMISO__REFERENCE_PERMISO foreign key (ID_PERMISO)
references PERMISO (ID_PERMISO)
on delete restrict on update restrict;

alter table PERMISO_PERFIL
  add constraint FK_PERMISO__REFERENCE_PERFIL foreign key (ID_PERFIL)
references PERFIL (ID_PERFIL)
on delete restrict on update restrict;

alter table RATING_REVIEW
  add constraint FK_RATING_R_REFERENCE_BENEFICI foreign key (ID_BENEFICIO)
references PROVEEDOR.BENEFICIO (ID_BENEFICIO)
on delete restrict on update restrict;

alter table RATING_REVIEW
  add constraint FK_RATING_R_REFERENCE_USUARIO foreign key (ID_USUARIO)
references USUARIO (ID_USUARIO)
on delete restrict on update restrict;

alter table REGION
  add constraint FK_REGION_REFERENCE_PAIS foreign key (ID_PAIS)
references PAIS (ID_PAIS)
on delete restrict on update restrict;

alter table USUARIO
  add constraint FK_USUARIO_REFERENCE_CONTACTO foreign key (ID_CONTACTO)
references CONTACTO (ID_CONTACTO)
on delete restrict on update restrict;

alter table USUARIO
  add constraint FK_USUARIO_REFERENCE_DIRECCIO foreign key (ID_DIRECCION)
references DIRECCION (ID_DIRECCION)
on delete restrict on update restrict;

alter table USUARIO
  add constraint FK_USUARIO_REFERENCE_ESTADO_U foreign key (ID_ESTADO_USUARIO)
references ESTADO_USUARIO (ID_ESTADO_USUARIO)
on delete restrict on update restrict;

alter table USUARIO_BENEFICIO
  add constraint FK_USUARIO__REFERENCE_ACCION_U foreign key (ID_ACCION_BENEFICIO)
references ACCION_USUARIO_BENEFICIO (ID_ACCION_BENEFICIO)
on delete restrict on update restrict;

alter table USUARIO_BENEFICIO
  add constraint FK_USUARIO__REFERENCE_USUARIO foreign key (ID_USUARIO)
references USUARIO (ID_USUARIO)
on delete restrict on update restrict;

alter table USUARIO_BENEFICIO
  add constraint FK_USUARIO__REFERENCE_BENEFICI foreign key (ID_BENEFICIO)
references PROVEEDOR.BENEFICIO (ID_BENEFICIO)
on delete restrict on update restrict;



/*==============================================================*/
/* INSERTANDO EN ESQUEMA BACKOFFICE LA INFORMACION DE LA EMPRESA NUEVA */
/*==============================================================*/
--TODO: CAMBIAR NOMBRE DE ESQUEMA A EMPRESA CORRESPONDIENTE
INSERT INTO backoffice.empresa( nombre, esquema)
VALUES ('BENNDER', 'BENNDER');