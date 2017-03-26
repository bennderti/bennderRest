/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.mapper;

import cl.bennder.entitybennderwebrest.model.BeneficioImagen;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

/**
 *
 * @author dyanez
 */
public interface CargadorMapper {
    
    
    /***
     * Encargado de guardar una imagen asociada a beneficio
     * @param beneficioImagen Datos de imagen asociada a beneficio seleccionado
     */
    @Insert("INSERT INTO BENEFICIO_IMAGEN(ID_BENEFICIO,IMAGEN,ORDEN,NOMBRE) "
            + "VALUES(#{idBeneficio},#{imagen},#{orden},#{nombre})")
    public void guardaImagenBeneficio(BeneficioImagen beneficioImagen);
    
    
    @Delete("DELETE FROM BENEFICIO_IMAGEN WHERE ID_BENEFICIO = #{idBeneficio}")
    public void eliminarImagenesBeneficio(Integer idBeneficio);
    
}   
