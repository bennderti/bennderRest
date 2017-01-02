/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author dyanez
 */
public interface UsuarioMapper {
    
   @Select("SELECT USUARIO FROM B_USUARIO") 
   public List<Integer> getUsers();
    
}
