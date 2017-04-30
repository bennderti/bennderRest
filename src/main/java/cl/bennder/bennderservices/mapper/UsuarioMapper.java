/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.mapper;

import cl.bennder.entitybennderwebrest.model.Contacto;
import cl.bennder.entitybennderwebrest.model.Direccion;
import cl.bennder.entitybennderwebrest.model.Perfil;
import cl.bennder.entitybennderwebrest.model.Usuario;

import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.IntegerTypeHandler;
import org.apache.ibatis.type.StringTypeHandler;

/**
 * @author dyanez
 */
public interface UsuarioMapper {

    /***
     * Método que registra acceso de usuario
     * @param idUsuario identificador de usuario
     */
    @Insert("insert into acceso_usuario(id_usuario) values(#{idUsuario});")
    public void registraAccesoUsuario(Integer idUsuario);

    /***
     * Obtención de correo que utiliza el usuario para login
     * @param idUsuario rut de usuario sin dv
     * @author dyanez
     * @return correo de login de usuario
     */
    @Select("select  USUARIO from usuario WHERE ID_USUARIO = #{idUsuario}")
    public String getUsuarioCorreo(Integer idUsuario);

    /**
     * Valida la existencia de usuario con password/usuario
     *
     * @param usuario  Indica el usuario ingresado
     * @param password Contraseña del usuaio
     * @return Retorna 0 si usuario no existe, caso contrario existe
     */
    @Select("SELECT COUNT(1) FROM USUARIO WHERE USUARIO = #{u} AND PASSWORD =#{p}")
    public Integer validaUsuario(@Param("u") String usuario, @Param("p") String password);


    /**
     * Obtiene el id del cliente que es RUT sin DV
     *
     * @param usuario Indica el usuario ingresado
     * @return Retorna el idUsuario que es rut sin DV, utilizado como el identificador de sesion para cliente
     */
    @Select("SELECT ID_USUARIO FROM USUARIO WHERE USUARIO = #{u}")
    public Integer getIdUsuario(@Param("u") String usuario);


    /**
     * Obtiene el estado del usuario el cual puede ser 1:Nuevo, 2:Activo, 3:Inactivo
     *
     * @param idUsuario
     * @return idEstadoUsuario
     * @author mgutierrez
     */
    @Select("SELECT ID_ESTADO_USUARIO FROM USUARIO WHERE ID_USUARIO = #{idUsuario}")
    public Integer getEstadoUasuario(@Param("idUsuario") Integer idUsuario);


    /**
     * Obtiene los siguiente datos del usuario: nombres, apellidos, direccion y datos de contacto
     *
     * @param idUsuario
     * @return Usuario
     * @author mgutierrez
     */
    @Select("SELECT U.NOMBRES, U.APELLIDO_P AS APELLIDOP, U.APELLIDO_M AS APELLIDOM, D.ID_COMUNA, D.CALLE, D.NUMERO, D.DEPARTAMENTO, D.VILLA, C.CELULAR, C.TELEFONO_FIJO, C.CORREO "
            + "FROM USUARIO U "
            + "INNER JOIN DIRECCION D ON U.ID_DIRECCION = D.ID_DIRECCION "
            + "INNER JOIN CONTACTO C ON U.ID_CONTACTO = C.ID_CONTACTO "
            + "WHERE U.ID_USUARIO = #{idUsuario}")
    @Results({
            @Result(property = "direccion.idComuna", column = "ID_COMUNA", javaType = Direccion.class, typeHandler = IntegerTypeHandler.class),
            @Result(property = "direccion.calle", column = "CALLE", javaType = Direccion.class, typeHandler = StringTypeHandler.class),
            @Result(property = "direccion.numero", column = "NUMERO", javaType = Direccion.class, typeHandler = StringTypeHandler.class),
            @Result(property = "direccion.departamento", column = "DEPARTAMENTO", javaType = Direccion.class, typeHandler = StringTypeHandler.class),
            @Result(property = "direccion.villa", column = "VILLA", javaType = Direccion.class, typeHandler = StringTypeHandler.class),
            @Result(property = "contacto.celular", column = "CELULAR", javaType = Contacto.class, typeHandler = IntegerTypeHandler.class),
            @Result(property = "contacto.fonoFijo", column = "TELEFONO_FIJO", javaType = Contacto.class, typeHandler = IntegerTypeHandler.class),
            @Result(property = "contacto.correo", column = "CORREO", javaType = Contacto.class, typeHandler = StringTypeHandler.class)
    })
    public Usuario getDatosUsuario(@Param("idUsuario") Integer idUsuario);

    /**
     * Método que valida si existe el usuario con el password indicado. Retorna un idUsuario con su estado
     *
     * @param usuario, password
     * @return Usuario
     * @author mgutierrez
     */
    @Select("SELECT ID_USUARIO AS IDUSUARIO, ID_ESTADO_USUARIO AS IDESTADO  FROM USUARIO WHERE USUARIO = #{usuario} AND PASSWORD =#{pass}")
    public Usuario getUsuarioValidacion(@Param("usuario") String usuario, @Param("pass") String password);

    /**
     * Metodo para realizar la autenticacion de usuarios a traves de spring security con Jwt
     * @param usuario
     * @return Usuario
     * @author driveros
     */
    @Select(" SELECT ID_USUARIO AS IDUSUARIO, ID_USUARIO, PASSWORD, USUARIO AS USUARIO, NOMBRES, APELLIDO_P AS APELLIDOP, APELLIDO_M AS APELLIDOM, ID_ESTADO_USUARIO IDESTADO, HABILITADO  " +
            " FROM USUARIO u" +
            " WHERE USUARIO = #{usuario}")
    @Results(value = {
            @Result (property = "perfiles", column = "ID_USUARIO", javaType=List.class, many = @Many(select = "obtenerPerfilesUsuario"))
    })
    public Usuario getUsuarioByUsername(@Param("usuario") String usuario);

    @Select(" SELECT p.id_perfil, p.nombre" +
            " FROM perfil p " +
            " INNER JOIN perfil_usuario pu ON pu.id_perfil = p.id_perfil" +
            " WHERE pu.id_usuario = #{idUsuario}")
    List<Perfil> obtenerPerfilesUsuario(Integer idUsuario);

}
