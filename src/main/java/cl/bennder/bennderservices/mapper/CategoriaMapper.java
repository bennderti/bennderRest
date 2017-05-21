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

    @Select( "SELECT c.id_categoria, c.nombre" +
            " FROM proveedor.categoria c " +
            " INNER JOIN proveedor.categoria c2 on c.id_categoria = c2.id_categoria_padre" +
            " INNER JOIN proveedor.beneficio b ON b.id_categoria = c2.id_categoria " +
            " INNER JOIN proveedor.proveedor p ON p.id_proveedor = b.id_proveedor " +
            " WHERE c.id_categoria_padre = -1 AND p.habilitado = true AND b.habilitado = true" +
            " AND NOW() BETWEEN b.fecha_inicial AND b.fecha_expiracion " +
            " AND b.stock > 0" +
            " GROUP BY c.id_categoria")
    @Results(value = {
           @Result (property = "idCategoria", column = "id_categoria"),
           @Result (property = "nombre", column = "nombre"),
           @Result (property = "subCategorias", column = "id_categoria", javaType=List.class, many = @Many(select = "obtenerSubCategorias"))
    })
    List<Categoria> getCategorias();

    @Select("SELECT c1.id_categoria AS idCategoria, c1.nombre, c1.id_categoria_padre AS idCategoriaPadre " +
            "FROM categoria c1 WHERE c1.id_categoria in( " +
            "SELECT distinct c.id_categoria " +
            "FROM proveedor.categoria c " +
            "INNER JOIN proveedor.beneficio b ON b.id_categoria = c.id_categoria " +
            "INNER JOIN proveedor.proveedor p ON p.id_proveedor = b.id_proveedor " +
            "WHERE id_categoria_padre =#{idCategoriaPadre} " +
            "AND p.habilitado = true " +
            "AND b.habilitado = true" +
            " AND NOW() BETWEEN b.fecha_inicial AND b.fecha_expiracion " +
            " AND b.stock > 0" +
            " )")
    List<Categoria> obtenerSubCategorias(Integer idCategoriaPadre);

    @Select(" SELECT c.id_categoria AS idCategoria, c.nombre, c.id_categoria_padre AS idCategoriaPadre, COUNT(b.id_beneficio) as cantidadBeneficios" +
            " FROM proveedor.categoria c" +
            " INNER JOIN proveedor.beneficio b ON b.id_categoria = c.id_categoria" +
            " INNER JOIN proveedor.proveedor p ON p.id_proveedor = b.id_proveedor" +
            " WHERE id_categoria_padre = #{idCategoriaPadre}" +
            " AND p.habilitado = true" +
            " AND b.habilitado = true" +
            " AND NOW() BETWEEN b.fecha_inicial AND b.fecha_expiracion " +
            " AND b.stock > 0" +
            " GROUP BY c.id_categoria")
    List<Categoria> obtenerSubCategoriasConCantidadBeneficios(Integer idCategoriaPadre);

    @Select(" SELECT id_categoria AS idCategoria, nombre, id_categoria_padre AS idCategoriaPadre " +
            " FROM proveedor.categoria WHERE nombre = #{nombreCategoria}")
    Categoria obtenerCategoriaPorNombre(String nombreCategoria);

    /***
    * Obtiene las categorias asociada a una categoria en especial
    * @param idCategoria
    * @return
    */

    @Select("SELECT id_categoria as idCategoria, nombre FROM proveedor.categoria WHERE id_categoria_padre = #{idCategoria}")
    List<Categoria> obtenerCategoriasById(Integer idCategoria);
    
    /***
     * Pérmite obtener las subcategorias de las categorias de beneficios habilitados para el proveedor
     * @param idCategoria identificador de categoria padre
     * @param idProveedor identificadr de proveedor
     * @return 
     */
    @Select("SELECT c.id_categoria as idCategoria, c.nombre FROM proveedor.categoria c " +
            "WHERE id_categoria_padre = #{idCat} and id_categoria in(select distinct id_categoria from beneficio where id_proveedor = #{idProv})")
    List<Categoria> obtenerSubCategoriasByIdCatProveedor(@Param("idCat") Integer idCategoria,@Param("idProv") Integer idProveedor);
    

    @Select("SELECT id_categoria as idCategoria, nombre FROM proveedor.categoria WHERE id_categoria = #{idCategoria}")
    Categoria obtenerCategoriaPorId(Integer idCategoria);


}
