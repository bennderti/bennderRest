/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.mapper;

import cl.bennder.bennderservices.model.Usuario;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author dyanez
 */
public interface UsuarioMapper {
    
   @Select("SELECT USUARIO FROM B_USUARIO") 
   public List<Integer> getUsers();
   
   /**
   * Valida la existencia de usuario con password/usuario
   * @param usuario Indica el usuario ingresado
   * @param password Contraseña del usuaio
   * @return Retorna 0 si usuario no existe, caso contrario existe
   */
   @Select("SELECT COUNT(1) FROM USUARIO WHERE USUARIO = #{u} AND PASSWORD =#{p}")
   public Integer validaUsuario(@Param("u") String usuario,@Param("p") String password);
   
    
    /**
   * Obtiene el id del cliente que es RUT sin DV
   * @param usuario Indica el usuario ingresado
   * @return Retorna el idUsuario que es rut sin DV, utilizado como el identificador de sesion para cliente
   */
   @Select("SELECT ID_USUARIO FROM USUARIO WHERE USUARIO = #{u}")
   public Integer getIdUsuario(@Param("u") String usuario);
   
   
   /**
    * Obtiene el estado del usuario el cual puede ser 1:Nuevo, 2:Activo, 3:Inactivo
    * @param idUsuario
    * @return idEstadoUsuario
    * @author mgutierrez
    */
   @Select("SELECT ID_ESTADO_USUARIO FROM USUARIO WHERE ID_USUARIO = #{idUsuario}")
   public Integer getEstadoUasuario(@Param("idUsuario") Integer idUsuario);
   
   
   /**
    * Obtiene los siguiente datos del usuario: nombres, apellidos, direccion y datos de contacto
    * @param idUsuario
    * @return Usuario 
    * @author mgutierrez
    */
   @Select("SELECT U.NOMBRES, U.APELLIDO_P, U.APELLIDO_M, D.ID_COMUNA, D.CALLE, D.NUMERO, D.DEPARTAMENTO, D.VILLA, C.CELULAR, C.TELEFONO_FIJO, C.CORREO "
           + "FROM USUARIO U "
           + "INNER JOIN DIRECCION D ON U.ID_DIRECCION = D.ID_DIRECCION "
           + "INNER JOIN CONTACTO C ON U.ID_CONTACTO = C.ID_CONTACTO "
           + "WHERE U.ID_USUARIO = #{idUsuario}")
   public Usuario getDatosUsuario(@Param("idUsuario") Integer idUsuario);
    
}
