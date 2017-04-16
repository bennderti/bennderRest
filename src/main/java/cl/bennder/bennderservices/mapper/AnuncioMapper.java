/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.mapper;

import java.util.List;
import cl.bennder.entitybennderwebrest.model.Anuncio;
import org.apache.ibatis.annotations.*;

/**
 *
 * @author Marcos
 */
public interface AnuncioMapper {
    
    /**
     * Método para obtener todos los anuncios activos de bennder y de la empresa, correspondiente a la fecha del momento en que se realiza la consulta.
     * MG - 04.06.2017
     * @return Lista de Anuncios
     */
    @Select("(SELECT ID_ANUNCIO as idAnuncio, TITULO, DESCRIPCION, LINK, IMAGEN "
            + "FROM ANUNCIO "
            + "WHERE HABILITADO = TRUE AND NOW() BETWEEN FECHA_INICIAL AND FECHA_TERMINO ORDER BY ORDEN)"
            + "UNION "
            + "(SELECT ID_ANUNCIO, TITULO, DESCRIPCION, LINK, IMAGEN "
            + "FROM ANUNCIO_EMPRESA "
            + "WHERE HABILITADO = TRUE AND NOW() BETWEEN FECHA_INICIAL AND FECHA_TERMINO ORDER BY ORDEN)")
    public List<Anuncio> obtenerAnuncios();
}
