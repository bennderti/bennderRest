/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.mapper;

import cl.bennder.bennderservices.model.Categoria;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author dyanez
 */
public interface CategoriaMapper {
    
   @Select("SELECT ID_CATEGORIA AS idCategoria,NOMBRE FROM CATEGORIA") 
   public List<Categoria> getCategorias();
   
    
}
