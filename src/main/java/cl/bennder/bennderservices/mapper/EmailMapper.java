/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.mapper;

import cl.bennder.bennderservices.model.PlantillaCorreo;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author dyanez
 */
public interface EmailMapper {
    
    /***
    * Método encargado de los datos básicos de una plantilla de correo
    *@param idPlantilla 
    *@return Datos de la plantilla de correo 
    */
    @Select("SELECT nombre,asunto FROM PLANTILLA_CORREO WHERE ID_PLANTILLA = #{idPlantilla}")
    public PlantillaCorreo getDatosPlantillaCorreo(Integer idPlantilla);
    
    /***
     * Método que valida si existe usuario registrado con ese correo
     * @author dyanez 18.03.2017
     * @param correoUsuario Usuario correo
     * @return 0 sino existe, > 0 caso contrario
     */
    @Select("SELECT COUNT(1) FROM USUARIO WHERE USUARIO = #{correoUsuario}")
    public Integer existeUsuarioCorreo(String correoUsuario);
    
    /****
     * @author dyanez 18.03.2017
     * Método encargado de obtener la contraseña del usuario
     * @param correoUsuario correo utilizado como usuario
     * @return Contraseña del usuario
     */
    @Select("SELECT password FROM USUARIO WHERE USUARIO = #{correoUsuario}")
    public String getPasswordByUsuario(String correoUsuario);
   
}
