/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.mapper;

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
    
}
