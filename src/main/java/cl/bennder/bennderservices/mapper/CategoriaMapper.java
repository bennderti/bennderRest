/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.mapper;

import cl.bennder.bennderservices.model.Categoria;
import java.util.List;

import org.apache.ibatis.annotations.*;

/**
 *
 * @author dyanez
 */
public interface CategoriaMapper {

   @Select("SELECT id_categoria, nombre FROM categoria WHERE id_categoria_padre = -1")
   @Results(value = {
		   @Result (property = "idCategoria", column = "id_categoria"),
		   @Result (property = "nombre", column = "nombre"),
		   @Result (property = "subCategorias", column = "id_categoria", javaType=List.class, many = @Many(select = "obtenerSubCategorias"))
   })
   List<Categoria> getCategorias();

   @Select("SELECT id_categoria AS idCategoria, nombre, id_categoria_padre AS idCategoriaPadre FROM categoria WHERE id_categoria_padre = #{idCategoriaPadre}")
   List<Categoria> obtenerSubCategorias(Integer idCategoriaPadre);

   @Select("SELECT id_categoria AS idCategoria, nombre, id_categoria_padre AS idCategoriaPadre FROM categoria WHERE nombre = #{nombreCategoria}")
   Categoria obtenerCategoriaPorNombre(String nombreCategoria);


}
