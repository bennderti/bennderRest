/*==============================================================*/
/* TABLAS UTILIZADAS PARA GUARDAR DATOS DE PARAMETROS DE SISTEMA
/*==============================================================*/

CREATE TABLE parametro_sistema
(
  id_parametro_sistema serial NOT NULL,
  tipo_parametro character(30) NOT NULL,
  clave character(30) NOT NULL,
  valor_a character(100) NOT NULL,
  valor_b character(100) NOT NULL  
);