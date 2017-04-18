/*==============================================================*/
/* DBMS name:      PGSQL9                                       */
/* Created on:     03-03-2017 23:19:11                          */
/*==============================================================*/


drop table if exists ACCESO_USUARIO;

drop table if exists ACCION_USUARIO_BENEFICIO;

drop table if exists BENEFICIO;

drop table if exists BENEFICIO_DESCUENTO;

drop table if exists BENEFICIO_IMAGEN;

drop table if exists BENEFICIO_PRODUCTO;

drop table if exists CARGO;

drop table if exists CATEGORIA;

drop table if exists COMUNA;

drop table if exists CONTACTO;

drop table if exists DIRECCION;

drop table if exists EMPRESA;

drop table if exists ESTADO_USUARIO;

drop table if exists FECHA_ACCION_BENEFICIO;

drop table if exists INTERES_USUARIO;

drop table if exists KEY_WORDS;

drop table if exists KEY_WORD_BENEFICIO;

drop table if exists PAIS;

drop table if exists PERFIL;

drop table if exists PERFIL_USUARIO;

drop table if exists PERMISO;

drop table if exists PERMISO_PERFIL;

drop table if exists PROVEEDOR;

drop table if exists RATING_REVIEW;

drop table if exists REGION;

drop table if exists STOP_WORD;

drop table if exists SUCURSAL_PROVEEDOR;

drop table if exists TESAURUS;

drop table if exists TIPO_BENEFICIO;

drop table if exists TIPO_USUARIO_EMPRESA;

drop table if exists USUARIO;

drop table if exists USUARIO_BENEFICIO;

drop table if exists USUARIO_EMPRESA;

drop table if exists USUARIO_PROVEEDOR;

drop table if exists plantilla_correo;

drop table if exists parametro_sistema;

/*==============================================================*/
/* Table: ACCESO_USUARIO                                        */
/*==============================================================*/
create table ACCESO_USUARIO (
   id_usuario integer,
   fecha_acceso timestamp  DEFAULT NOW()
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
/* Table: BENEFICIO                                             */
/*==============================================================*/
create table BENEFICIO (
   ID_BENEFICIO         SERIAL               not null,
   TITULO               VARCHAR(50)             not null,
   DESCRIPCION          VARCHAR(1000)           not null,
   FECHA_CREACION       date                 not null default now(),
   FECHA_INICIAL        date                 not null default now(),
   FECHA_EXPIRACION     date                 not null,
   CONDICION            VARCHAR(50)             null,
   HABILITADO           BOOL                 not null default false,
   CALIFICACION         INT4                 not null default 0,
   STOCK                INT4                 not null,
   ID_PROVEEDOR         INT4                 not null,
   ID_CATEGORIA         INT4                 not null,
   ID_TIPO_BENEFICIO    INT4                 null,
   LIMITE_STOCK         INT4                 null,
   VISITAS_GENERAL      INT4                 not null default 0,
   constraint PK_BENEFICIO primary key (ID_BENEFICIO)
);

/*==============================================================*/
/* Table: BENEFICIO_DESCUENTO                                   */
/*==============================================================*/
create table BENEFICIO_DESCUENTO (
   ID_BENEFICIO         INT4                 not null,
   PORCENTAJE_DESCUENTO INT4                 null,
   constraint PK_BENEFICIO_DESCUENTO primary key (ID_BENEFICIO)
);

/*==============================================================*/
/* Table: BENEFICIO_IMAGEN                                      */
/*==============================================================*/
create table BENEFICIO_IMAGEN (
    id_beneficio integer NOT NULL,
    orden integer NOT NULL,
    nombre character varying(30) NOT NULL,
    path character varying(200),
    id_imagen integer,
   constraint PK_BENEFICIO_IMAGEN primary key (ID_BENEFICIO, ORDEN)
);

/*==============================================================*/
/* Table: BENEFICIO_PRODUCTO                                    */
/*==============================================================*/
create table BENEFICIO_PRODUCTO (
   ID_BENEFICIO         INT4                 not null,
   PRECIO_NORMAL        INT4                 null,
   PRECIO_OFERTA        INT4                 null,
   constraint PK_BENEFICIO_PRODUCTO primary key (ID_BENEFICIO)
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
/* Table: CATEGORIA                                             */
/*==============================================================*/
create table CATEGORIA (
   ID_CATEGORIA         INT4               not null,
   ID_CATEGORIA_PADRE   INT4                 not null default -1,
   NOMBRE               varchar(50)          not null,
   DESCRIPCION          varchar(500)         not null,
   constraint PK_CATEGORIA primary key (ID_CATEGORIA)
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
   CORREO               VARCHAR(20)             null,
   constraint PK_CONTACTO primary key (ID_CONTACTO)
);

/*==============================================================*/
/* Table: DIRECCION                                             */
/*==============================================================*/
create table DIRECCION (
   ID_DIRECCION         SERIAL               not null,
   ID_COMUNA            INT4                 null,
   CALLE                VARCHAR(20)             not null,
   NUMERO               VARCHAR(10)             not null,
   DEPARTAMENTO         VARCHAR(10)             null,
   VILLA                VARCHAR(20)             null,
   constraint PK_DIRECCION primary key (ID_DIRECCION)
);

/*==============================================================*/
/* Table: EMPRESA                                               */
/*==============================================================*/
create table EMPRESA (
   ID_EMPRESA           SERIAL               not null,
   ID_DIRECCION         INT4                 null,
   NOMBRE               VARCHAR(50)             not null,
   constraint PK_EMPRESA primary key (ID_EMPRESA)
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
   ID_VENDEDOR_POS INT4
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
/* Table: KEY_WORDS                                             */
/*==============================================================*/
create table KEY_WORDS (
   ID_KEY_WORD          VARCHAR(20)             not null,
   constraint PK_KEY_WORDS primary key (ID_KEY_WORD)
);

/*==============================================================*/
/* Table: KEY_WORD_BENEFICIO                                    */
/*==============================================================*/
create table KEY_WORD_BENEFICIO (
   ID_BENEFICIO         INT4                 not null,
   ID_KEY_WORD          VARCHAR(20)             not null,
   constraint PK_KEY_WORD_BENEFICIO primary key (ID_BENEFICIO, ID_KEY_WORD)
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
/* Table: PROVEEDOR                                             */
/*==============================================================*/
create table PROVEEDOR (
   ID_PROVEEDOR         INT4                 not null,
   NOMBRE               VARCHAR(50)             not null,
   RUT                  INT4                 not null,
   FECHA_INGRESO        DATE                 not null,
   FECHA_SALIDA         DATE                 null,
   HABILITADO           BOOL                 not null,
   constraint PK_PROVEEDOR primary key (ID_PROVEEDOR)
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
/* Table: STOP_WORD                                             */
/*==============================================================*/
create table STOP_WORD (
   ID_STOP_WORD         VARCHAR(20)             not null,
   constraint PK_STOP_WORD primary key (ID_STOP_WORD)
);

/*==============================================================*/
/* Table: SUCURSAL_PROVEEDOR                                    */
/*==============================================================*/
create table SUCURSAL_PROVEEDOR (
   ID_PROVEEDOR         INT4                 not null,
   ID_DIRECCION         INT4                 not null,
   PASSWORD             VARCHAR(25)           not null,
   constraint PK_SUCURSAL_PROVEEDOR primary key (ID_PROVEEDOR, ID_DIRECCION)
);

/*==============================================================*/
/* Table: TESAURUS                                              */
/*==============================================================*/
create table TESAURUS (
   KEY_WORD             VARCHAR(20)             not null,
   KEY_WORD_REL         VARCHAR(20)             not null,
   constraint PK_TESAURUS primary key (KEY_WORD, KEY_WORD_REL)
);

/*==============================================================*/
/* Table: TIPO_BENEFICIO                                        */
/*==============================================================*/
create table TIPO_BENEFICIO (
   ID_TIPO_BENEFICIO    INT4                 not null,
   NOMBRE               VARCHAR(15)             null,
   constraint PK_TIPO_BENEFICIO primary key (ID_TIPO_BENEFICIO)
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
   PASSWORD             VARCHAR(15)          not null,
   USUARIO              VARCHAR(50)          not null,
   NOMBRES              VARCHAR(50)          not null,
   APELLIDO_P           VARCHAR(50)          not null,
   APELLIDO_M           VARCHAR(50)          not null,
   FECHA_NACIMIENTO     DATE                 not null,
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
/* Table: USUARIO_EMPRESA                                       */
/*==============================================================*/
create table USUARIO_EMPRESA (
   ID_EMPRESA           INT4                 null,
   ID_TIPO              INT4                 null,
   ID_CARGO             INT4                 not null,
   ID_USUARIO           INT4                 not null,
   CORREO               VARCHAR(20)             null,
   constraint PK_USUARIO_EMPRESA primary key (ID_CARGO, ID_USUARIO)
);

/*==============================================================*/
/* Table: USUARIO_PROVEEDOR                                     */
/*==============================================================*/
create table USUARIO_PROVEEDOR (
   ID_PROVEEDOR         INT4                 not null,
   ID_USUARIO           INT4                 not null,
   ID_TIPO              INT4                 not null,
   CORREO               VARCHAR(20)             not null,
   constraint PK_USUARIO_PROVEEDOR primary key (ID_PROVEEDOR, ID_USUARIO)
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
  valor_b varchar(100) NOT NULL
);

alter table ACCESO_USUARIO
   add constraint FK_ACCESO_U_REFERENCE_USUARIO foreign key (ID_USUARIO)
      references USUARIO (ID_USUARIO)
      on delete restrict on update restrict;

alter table BENEFICIO
   add constraint FK_BENEFICI_REFERENCE_PROVEEDO foreign key (ID_PROVEEDOR)
      references PROVEEDOR (ID_PROVEEDOR)
      on delete restrict on update restrict;

alter table BENEFICIO
   add constraint FK_BENEFICI_REFERENCE_TIPO_BEN foreign key (ID_TIPO_BENEFICIO)
      references TIPO_BENEFICIO (ID_TIPO_BENEFICIO)
      on delete restrict on update restrict;

alter table BENEFICIO
   add constraint FK_BENEFICI_REFERENCE_CATEGORI foreign key (ID_CATEGORIA)
      references CATEGORIA (ID_CATEGORIA)
      on delete restrict on update restrict;

alter table BENEFICIO_DESCUENTO
   add constraint FK_BENEFICI_REFERENCE_BENEFICI foreign key (ID_BENEFICIO)
      references BENEFICIO (ID_BENEFICIO)
      on delete restrict on update restrict;

alter table BENEFICIO_IMAGEN
   add constraint FK_BENEFICI_REFERENCE_BENEFICI foreign key (ID_BENEFICIO)
      references BENEFICIO (ID_BENEFICIO)
      on delete restrict on update restrict;

alter table BENEFICIO_PRODUCTO
   add constraint FK_BENEFICI_REFERENCE_BENEFICI foreign key (ID_BENEFICIO)
      references BENEFICIO (ID_BENEFICIO)
      on delete restrict on update restrict;

alter table COMUNA
   add constraint FK_COMUNA_REFERENCE_REGION foreign key (ID_REGION)
      references REGION (ID_REGION)
      on delete restrict on update restrict;

alter table DIRECCION
   add constraint FK_DIRECCIO_REFERENCE_COMUNA foreign key (ID_COMUNA)
      references COMUNA (ID_COMUNA)
      on delete restrict on update restrict;

alter table EMPRESA
   add constraint FK_EMPRESA_REFERENCE_DIRECCIO foreign key (ID_DIRECCION)
      references DIRECCION (ID_DIRECCION)
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
      references CATEGORIA (ID_CATEGORIA)
      on delete restrict on update restrict;

alter table INTERES_USUARIO
   add constraint FK_INTERES__REFERENCE_USUARIO foreign key (ID_USUARIO)
      references USUARIO (ID_USUARIO)
      on delete restrict on update restrict;

alter table KEY_WORD_BENEFICIO
   add constraint FK_KEY_WORD_REFERENCE_BENEFICI foreign key (ID_BENEFICIO)
      references BENEFICIO (ID_BENEFICIO)
      on delete restrict on update restrict;

alter table KEY_WORD_BENEFICIO
   add constraint FK_KEY_WORD_REFERENCE_KEY_WORD foreign key (ID_KEY_WORD)
      references KEY_WORDS (ID_KEY_WORD)
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
      references BENEFICIO (ID_BENEFICIO)
      on delete restrict on update restrict;

alter table RATING_REVIEW
   add constraint FK_RATING_R_REFERENCE_USUARIO foreign key (ID_USUARIO)
      references USUARIO (ID_USUARIO)
      on delete restrict on update restrict;

alter table REGION
   add constraint FK_REGION_REFERENCE_PAIS foreign key (ID_PAIS)
      references PAIS (ID_PAIS)
      on delete restrict on update restrict;

alter table SUCURSAL_PROVEEDOR
   add constraint FK_SUCURSAL_REFERENCE_PROVEEDO foreign key (ID_PROVEEDOR)
      references PROVEEDOR (ID_PROVEEDOR)
      on delete restrict on update restrict;

alter table SUCURSAL_PROVEEDOR
   add constraint FK_SUCURSAL_REFERENCE_DIRECCIO foreign key (ID_DIRECCION)
      references DIRECCION (ID_DIRECCION)
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
      references BENEFICIO (ID_BENEFICIO)
      on delete restrict on update restrict;

alter table USUARIO_EMPRESA
   add constraint FK_USUARIO__REFERENCE_EMPRESA foreign key (ID_EMPRESA)
      references EMPRESA (ID_EMPRESA)
      on delete restrict on update restrict;

alter table USUARIO_EMPRESA
   add constraint FK_USUARIO__REFERENCE_TIPO_USU foreign key (ID_TIPO)
      references TIPO_USUARIO_EMPRESA (ID_TIPO)
      on delete restrict on update restrict;

alter table USUARIO_EMPRESA
   add constraint FK_USUARIO__REFERENCE_CARGO foreign key (ID_CARGO)
      references CARGO (ID_CARGO)
      on delete restrict on update restrict;

alter table USUARIO_EMPRESA
   add constraint FK_USUARIO__REFERENCE_USUARIO foreign key (ID_USUARIO)
      references USUARIO (ID_USUARIO)
      on delete restrict on update restrict;

alter table USUARIO_PROVEEDOR
   add constraint FK_USUARIO__REFERENCE_USUARIO foreign key (ID_USUARIO)
      references USUARIO (ID_USUARIO)
      on delete restrict on update restrict;

alter table USUARIO_PROVEEDOR
   add constraint FK_USUARIO__REFERENCE_PROVEEDO foreign key (ID_PROVEEDOR)
      references PROVEEDOR (ID_PROVEEDOR)
      on delete restrict on update restrict;


/*==============================================================*/
/* SECUENCIAS                                                */
/*==============================================================*/
CREATE SEQUENCE beneficio_imagen_id_imagen_seq START 1;




CREATE TABLE anuncio
(
    id_anuncio integer NOT NULL DEFAULT nextval('"Anuncio_id_anuncio_seq"'::regclass),
    titulo character(50) COLLATE pg_catalog."default",
    descripcion character(200) COLLATE pg_catalog."default",
    fecha_inicial date NOT NULL,
    fecha_termino date NOT NULL,
    orden integer NOT NULL,
    habilitado boolean NOT NULL,
    link character(200) COLLATE pg_catalog."default",
    imagen character(20) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "Anuncio_pkey" PRIMARY KEY (id_anuncio)
)

COMMENT ON TABLE anuncio
    IS 'Anuncios de Bennder para los trabajadores';

CREATE TABLE anuncio_empresa
(
    id_anuncio integer NOT NULL DEFAULT nextval('"Anuncio_id_anuncio_seq"'::regclass),
    titulo character(50) COLLATE pg_catalog."default",
    descripcion character(200) COLLATE pg_catalog."default",
    fecha_inicial date NOT NULL,
    fecha_termino date NOT NULL,
    orden integer NOT NULL,
    habilitado boolean NOT NULL,
    link character(200) COLLATE pg_catalog."default",
    imagen character(20) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "Anuncio_pkey" PRIMARY KEY (id_anuncio)
);

COMMENT ON TABLE anuncio
    IS 'Anuncios de Bennder para los trabajadores';

CREATE TABLE anuncio_empresa
(
    id_anuncio integer NOT NULL DEFAULT nextval('anuncio_empresa_id_anuncio_seq'::regclass),
    titulo character(50) COLLATE pg_catalog."default",
    descripcion character(200) COLLATE pg_catalog."default",
    fecha_inicial date NOT NULL,
    fecha_termino date NOT NULL,
    habilitado boolean NOT NULL,
    link character(200) COLLATE pg_catalog."default",
    imagen character(20) COLLATE pg_catalog."default" NOT NULL,
    orden integer,
    CONSTRAINT anuncio_empresa_pkey PRIMARY KEY (id_anuncio)
);

COMMENT ON TABLE anuncio_empresa
    IS 'Anuncios de la Empresa para los trabajadores';

CREATE TABLE beneficio_gancho
(
    id_beneficio integer NOT NULL,
    gancho character(1) COLLATE pg_catalog."default",
    CONSTRAINT beneficio_gancho_pkey PRIMARY KEY (id_beneficio),
    CONSTRAINT beneficio_gancho_fk1 FOREIGN KEY (id_beneficio)
        REFERENCES beneficio (id_beneficio) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);