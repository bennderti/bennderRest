/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.mapper;


import cl.bennder.entitybennderwebrest.model.Categoria;
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

    @Select(" SELECT c.id_categoria AS idCategoria, nombre, id_categoria_padre AS idCategoriaPadre, COUNT(b.id_beneficio) as cantidadBeneficios" +
            " FROM categoria c" +
            " LEFT JOIN beneficio b ON b.id_categoria = c.id_categoria" +
            " WHERE id_categoria_padre = #{idCategoriaPadre}" +
            " GROUP BY c.id_categoria")
    List<Categoria> obtenerSubCategorias(Integer idCategoriaPadre);

    @Select("SELECT id_categoria AS idCategoria, nombre, id_categoria_padre AS idCategoriaPadre FROM categoria WHERE nombre = #{nombreCategoria}")
    Categoria obtenerCategoriaPorNombre(String nombreCategoria);

    /***
    * Obtiene las categorias asociada a una categoria en especial
    * @param idCategoria
    * @return
    */

    @Select("SELECT id_categoria as idCategoria, nombre FROM categoria WHERE id_categoria_padre = #{idCategoria}")
    List<Categoria> obtenerCategoriasById(Integer idCategoria);

    @Select("SELECT id_categoria as idCategoria, nombre FROM categoria WHERE id_categoria = #{idCategoria}")
    Categoria obtenerCategoriaPorId(Integer idCategoria);


}
