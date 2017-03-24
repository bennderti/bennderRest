/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.mapper;

import cl.bennder.entitybennderwebrest.model.Categoria;
import cl.bennder.entitybennderwebrest.model.Proveedor;
import java.util.List;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author dyanez
 */
public interface ProveedorMapper {
    
    /***
     * Obtiene los proveedor habilitados
     * @return lista de proveedores
     * @author dyanez
     */
    @Select("SELECT ID_PROVEEDOR AS idProveedor,NOMBRE,RUT FROM PROVEEDOR WHERE HABILITADO=TRUE")
    public List<Proveedor> obtenerProveedorHabilitados();
    
    
   /***
    * Obtiene las categorias del proveedor (se trabaja a dos niveles, categoria-subcategoria)
    * @param idProveedor identificador de proveedor
    * @return Categorias de proveedor
    */
   @Select("SELECT CAT.ID_CATEGORIA AS idCategoria,CAT.NOMBRE FROM CATEGORIA CAT INNER JOIN( " +
            "SELECT  DISTINCT C1.ID_CATEGORIA_PADRE FROM CATEGORIA C1 INNER JOIN(  " +
            "SELECT DISTINCT ID_CATEGORIA FROM BENEFICIO WHERE ID_PROVEEDOR = #{idProveedor}) B1 " +
            "ON B1.ID_CATEGORIA = C1.ID_CATEGORIA)  SUB ON SUB.ID_CATEGORIA_PADRE=CAT.ID_CATEGORIA ")
    public List<Categoria> obtenerCategoriaProveedor(Integer idProveedor);
}
