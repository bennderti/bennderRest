/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.mapper;

import cl.bennder.entitybennderwebrest.model.Comuna;
import cl.bennder.entitybennderwebrest.model.Contacto;
import cl.bennder.entitybennderwebrest.model.Direccion;
import cl.bennder.entitybennderwebrest.model.Region;
import cl.bennder.entitybennderwebrest.model.Sucursal;
import cl.bennder.entitybennderwebrest.model.Usuario;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.IntegerTypeHandler;

/**
 *
 * @author ext_dayanez
 */
public interface PerfilMapper {
    
    
    @Select("select id_region as idRegion,nombre from region order by nombre")
    public List<Region> getRegiones();
    
    
    @Select("select id_comuna as idComuna,nombre,id_region from comuna order by nombre")
     @Results({
            @Result(property="region.idRegion", column="id_region", javaType = Region.class,typeHandler = IntegerTypeHandler.class)})
    public List<Comuna> getComunas();
    
    
    @Select("select id_usuario as idUsuario,id_contacto,id_direccion,nombres,apellido_p as apellidoP, "
            + "apellido_m as apellidoM,fecha_nacimiento as fechaNacimiento,usuario " +
            "from usuario " +
            "where id_usuario = #{idUsuario}") 
            @Results({
                @Result(property="direccion.idDireccion", column="id_direccion", javaType = Direccion.class,typeHandler = IntegerTypeHandler.class),
                @Result(property="contacto.idContacto", column="id_contacto", javaType = Contacto.class,typeHandler = IntegerTypeHandler.class)
            }) 
    public Usuario getDatosPerfil(Integer idUsuario);
    
    
    @Select("select d.id_direccion as idDireccion,d.calle,d.numero,d.departamento,d.villa,c.id_comuna,r.id_region from direccion d " +
            "inner join comuna c on c.id_comuna=d.id_comuna inner join region r on r.id_region=c.id_region " +
            "where d.id_direccion = #{idDireccion}")
        @Results({
          @Result(property="comuna.idComuna", column="id_comuna", javaType = Comuna.class,typeHandler = IntegerTypeHandler.class),
          @Result(property="comuna.region.idRegion", column="id_region", javaType = Region.class,typeHandler = IntegerTypeHandler.class)
      }) 
    public Direccion getDireccion(Integer idDireccion);
    
    
    @Select("select id_contacto as idContacto,celular,telefono_fijo as fonoFijo,correo from contacto where id_contacto = #{idContacto}")
    public Contacto getContacto(Integer idContacto);
    
    
    @Select("SELECT nextval('contacto_id_contacto_seq')")
    public Integer getSeqIdContacto();
    
    
    @Update("update contacto set celular=#{celular},telefono_fijo = #{fonoFijo,jdbcType = NULL},"
            + "correo = #{correo,jdbcType = NULL} where id_contacto = #{idContacto}")
    public void updateContacto(Contacto contacto);
    
      @Insert("insert into direccion(id_contacto,celular,telefono_fijo,correo) " +
            "values(#{idContacto},#{celular},#{fonoFijo,jdbcType = NULL},#{correo,jdbcType = NULL})")
    public void insertContacto(Contacto contacto);
    
    @Select("SELECT nextval('direccion_id_direccion_seq')")
    public Integer getSeqIdDireccion();
    
    @Insert("insert into direccion(id_direccion,id_comuna,calle,numero,departamento,villa) " +
            "values(#{idDireccion},#{comuna.idComuna},#{calle},#{numero,jdbcType = NULL},#{departamento,jdbcType = NULL},#{villa,jdbcType = NULL})")
    public void insertDireccion(Direccion direccion);
    
    
    @Update("update direccion set id_comuna = #{comuna.idComuna},calle = #{calle}, numero = #{numero,jdbcType = NULL} "
            + ", departamento = #{departamento,jdbcType = NULL}, villa = #{villa,jdbcType = NULL}"
            + "where id_direccion = #{idDireccion}")
    public void updateDireccion(Direccion direccion);
    
    
    @Update("update usuario set id_contacto=#{contacto.idContacto},id_direccion=#{direccion.idDireccion},"
            + " nombres=#{nombres},apellido_p=#{apellidoP},apellido_m=#{apellidoM}, "
            + "fecha_nacimiento = #{fechaNacimiento} where id_usuario = #{idUsuario}")
    public void updateDatosPerfil(Usuario usuario);
    

    
    
    
    
    
    
}
