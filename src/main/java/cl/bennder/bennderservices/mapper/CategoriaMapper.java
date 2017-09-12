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
            "FROM proveedor.categoria c1 WHERE c1.id_categoria in( " +
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
    

    @Select("SELECT id_categoria as idCategoria, nombre, id_categoria_padre AS idCategoriaPadre FROM proveedor.categoria WHERE id_categoria = #{idCategoria}")
    Categoria obtenerCategoriaPorId(Integer idCategoria);

    /**
     * Obtiene lista de todas las categorías con sus subcategorias
     * MG - 04/09/2017
     * @return List<Categoria>
     */
    @Select("SELECT c.id_categoria, c.nombre " +
            "FROM proveedor.categoria c " +
            "INNER JOIN proveedor.categoria c2 on c.id_categoria = c2.id_categoria_padre " +
            "WHERE c.id_categoria_padre = -1 " +
            "GROUP BY c.id_categoria " +
            "ORDER BY c.nombre")
    @Results(value = {
           @Result (property = "idCategoria", column = "id_categoria"),
           @Result (property = "nombre", column = "nombre"),
           @Result (property = "subCategorias", column = "id_categoria", javaType=List.class, many = @Many(select = "obtenerTodasSubCategorias"))
    })
    List<Categoria> obtenerTodasCategorias();
    
    /**
     * Obtiene lista de todas las subcategorías por id_categoria_padre
     * MG - 04/09/2017
     * @return List<Categoria>
     */
    @Select("SELECT id_categoria AS idCategoria, nombre, id_categoria_padre AS idCategoriaPadre " +
            "FROM proveedor.categoria " +
            "WHERE id_categoria_padre = #{idCategoriaPadre}" +
            "ORDER BY nombre")
    List<Categoria> obtenerTodasSubCategorias(Integer idCategoriaPadre);
    
    /**
     * Inserta la categoria preferida de un usuario en específico
     * MG - 11/09/2017
     * @param idCategoria
     * @param idUsuario 
     */
    @Insert("INSERT INTO INTERES_USUARIO (ID_CATEGORIA, ID_USUARIO) VALUES (#{idCategoria}, #{idUsuario})")
    public void guardarInteresUsuario(@Param("idCategoria") Integer idCategoria, @Param("idUsuario")Integer idUsuario);
    
    /**
     * Elimina todas las categorias preferidas de un usuario
     * MG - 11/09/2017
     * @param idUsuario 
     */
    @Delete("DELETE FROM INTERES_USUARIO WHERE ID_USUARIO = #{idUsuario}")
    public void eliminarTodosInteresUsuario(Integer idUsuario);
}
