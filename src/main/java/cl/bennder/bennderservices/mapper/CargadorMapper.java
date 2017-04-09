/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.mapper;

import cl.bennder.entitybennderwebrest.model.BeneficioImagen;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author dyanez
 */
public interface CargadorMapper {
    
    
    /***
     * Encargado de guardar una imagen asociada a beneficio
     * @param beneficioImagen Datos de imagen asociada a beneficio seleccionado
     */
    @Insert("INSERT INTO BENEFICIO_IMAGEN(ID_BENEFICIO,PATH,ORDEN,NOMBRE,ID_IMAGEN) "
            + "VALUES(#{idBeneficio},#{path},#{orden},#{nombre},#{idImagen})")
    public void guardaImagenBeneficio(BeneficioImagen beneficioImagen);
    
    
    @Delete("DELETE FROM BENEFICIO_IMAGEN WHERE ID_BENEFICIO = #{idBeneficio}")
    public void eliminarImagenesBeneficio(Integer idBeneficio);
    
    @Select("SELECT nextval('beneficio_imagen_id_imagen_seq')")
    public Integer getSeqIdImagen();
    
}   
