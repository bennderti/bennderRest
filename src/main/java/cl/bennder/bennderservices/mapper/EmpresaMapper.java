package cl.bennder.bennderservices.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by Diego on 21-05-2017.
 */
public interface EmpresaMapper {

    @Update("SET SESSION SEARCH_PATH TO ${empresa}")
    void cambiarEsquema(@Param("empresa")String empresa);
}
